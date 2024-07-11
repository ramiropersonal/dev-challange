package com.directa24.main.challenge.services;

import com.directa24.main.challenge.model.Director;
import com.directa24.main.challenge.model.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class MovieServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDirectorsWhenPassingMovieShouldReturnOnlyTheOnesAfter2010() throws Exception {
        // Mock data
        Movie movie1 = new Movie();
        movie1.setDirector("Martin Scorsese");
        movie1.setReleased("10 Jun 2009");

        Movie movie2 = new Movie();
        movie2.setDirector("Woody Allen");
        movie2.setReleased("15 Jul 2012");

        Movie movie3 = new Movie();
        movie3.setDirector("Martin Scorsese");
        movie3.setReleased("20 Aug 2008");

        String mockJsonResponsePage1 = "{\"data\":[{\"director\":\"Martin Scorsese\",\"released\":\"10 Jun 2011\"},{\"director\":\"Woody Allen\",\"released\":\"15 Jul 2012\"}]}";
        String mockJsonResponsePage2 = "{\"data\":[{\"director\":\"Martin Scorsese\",\"released\":\"20 Aug 2013\"}]}";

        JsonNode rootNodePage1 = new ObjectMapper().readTree(mockJsonResponsePage1);
        JsonNode rootNodePage2 = new ObjectMapper().readTree(mockJsonResponsePage2);

        when(objectMapper.readTree(mockJsonResponsePage1)).thenReturn(rootNodePage1);
        when(objectMapper.readTree(mockJsonResponsePage2)).thenReturn(rootNodePage2);

        // Mock objectMapper readValue for moviesData
        List<LinkedHashMap> mockMoviesDataPage1 = new ArrayList<>();
        mockMoviesDataPage1.add(new LinkedHashMap() {{
            put("director", "Martin Scorsese");
            put("released", "10 Jun 2008");
        }});
        mockMoviesDataPage1.add(new LinkedHashMap() {{
            put("director", "Woody Allen");
            put("released", "15 Jul 2012");
        }});

        List<LinkedHashMap> mockMoviesDataPage2 = new ArrayList<>();
        mockMoviesDataPage2.add(new LinkedHashMap() {{
            put("director", "Martin Scorsese");
            put("released", "20 Aug 2009");
        }});

        // Mock the restTemplate and objectMapper behavior
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(mockJsonResponsePage1)
                .thenReturn(mockJsonResponsePage2)
                .thenReturn(null);

        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(mockMoviesDataPage1)
                .thenReturn(mockMoviesDataPage2);

        when(objectMapper.readValue(anyString(), eq(Movie.class)))
                .thenReturn(movie1)
                .thenReturn(movie2);

        when(objectMapper.writeValueAsString(any()))
                .thenReturn(mockJsonResponsePage1)
                .thenReturn(mockJsonResponsePage2);

        // Call the method to test
        List<Director> directors = movieService.getDirectors(1);

        // Validate the results
        assertEquals(1, directors.size());
        assertEquals("Woody Allen", directors.get(0).getName());
    }
}