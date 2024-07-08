package com.directa24.main.challenge.controller;

import com.directa24.main.challenge.model.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.directa24.main.challenge.services.MovieService;

import java.util.List;


@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/api/directors")
    public List<Director> getDirectors(@RequestParam int threshold) {
        return movieService.getDirectors(threshold);
    }
}