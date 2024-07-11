package com.directa24.main.challenge.controller;

import com.directa24.main.challenge.model.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.directa24.main.challenge.services.MovieService;

import java.io.IOException;
import java.util.List;


@RestController
public class DirectorController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/api/directors")
    public List<Director> getDirectors(@RequestParam int threshold) throws Exception {
        try {
            return movieService.getDirectors(threshold);
        } catch (IOException e) {
            throw new IOException("An error occurred while parsing the data from movies: " + e.getMessage());
        } catch (Exception e){
            throw new Exception("An error occurred while fetching movies: " + e.getMessage());
        }
    }
}