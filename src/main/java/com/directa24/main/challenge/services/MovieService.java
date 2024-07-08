package com.directa24.main.challenge.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.directa24.main.challenge.model.Director;
import com.directa24.main.challenge.model.Movie;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService implements MovieClient{
    @Value("${movies.api.url}")
    private String moviesApiUrl;
    // Autowire ObjectMapper if needed
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public MovieService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Director> getDirectors(int threshold) {
        List<Movie> movies = new ArrayList<>();
        int page = 1;
        List<Movie> response = new ArrayList<>();

        // Paginate through all movies
        do {
            try {
                response = getMovies(page++);
                movies.addAll(response);
            }catch (Exception e){
                System.out.println("Error trying to get movies:" + Arrays.toString(e.getStackTrace()));
            }
        } while (!response.isEmpty());

        // Filter movies released after 2010
        List<Movie> filteredMovies = movies.stream()
                .filter(movie -> Integer.parseInt(movie.getReleased().split(" ")[2])> 2010)
                .collect(Collectors.toList());

        // Count movies per director
        Map<String, Long> directorCountMap = filteredMovies.stream()
                .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()));

        // Filter directors with movies greater than threshold and sort alphabetically
        return directorCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(entry -> {
                    Director director = new Director();
                    director.setName(entry.getKey());
                    director.setMovieCount(entry.getValue());
                    return director;
                })
                .sorted(Comparator.comparing(Director::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getMovies(int page) throws IOException {
        String url = moviesApiUrl + "/api/movies/search?page=" + page;

        // Make HTTP GET request and deserialize JSON response
        List<Movie> response = parseMovies(restTemplate.getForObject(url, String.class));

        if (response != null) {
            return response;
        } else {
            return null; // Handle case when response is null
        }
    }

    public List<Movie> parseMovies(String jsonString) throws IOException {
        List<Movie> result= new ArrayList<>();
        JsonNode root = objectMapper.readTree(jsonString);
        if(root!=null){
            JsonNode moviesNode = root.get("data");
            List<LinkedHashMap> moviesData = objectMapper.readValue(moviesNode.toString(), new TypeReference<>() {});
            moviesData.forEach(data -> {
                try {
                    result.add(objectMapper.readValue(objectMapper.writeValueAsString(data), Movie.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return result;

    }
}
