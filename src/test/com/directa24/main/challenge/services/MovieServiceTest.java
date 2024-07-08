package com.directa24.main.challenge.services;

import com.directa24.main.challenge.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MovieServiceTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private MovieService movieService;

    @Test
    @ExtendWith(MockitoExtension.class)
    public void testGetMovies() throws IOException {
        String mockJsonResponse = "[{\"title\":\"Movie1\",\"director\":\"Director1\"}," +
                "{\"title\":\"Movie2\",\"director\":\"Director2\"}]";

        List<Movie> movies = new ArrayList<>();
        Movie movie1= new Movie();
        movie1.setTitle("Movie1");
        movie1.setDirector("Director1");
        Movie movie2= new Movie();
        movie1.setTitle("Movie2");
        movie1.setDirector("Director2");
        movies.add(movie1);
        movies.add(movie2);

        // Mock RestTemplate behavior
        when(restTemplate.getForObject(anyString(), any()))
                .thenReturn(mockJsonResponse);

        // Test the service method
        List<Movie> moviesData = movieService.getMovies(1);
        //TODO: Mocking and getting spected movies
         assertEquals(0, moviesData.size());
    }
}