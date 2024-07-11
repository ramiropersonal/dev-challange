package com.directa24.main.challenge.services;

import com.directa24.main.challenge.model.Movie;

import java.io.IOException;
import java.util.List;


public interface MovieClient {

    List<Movie> getMoviesFromPage(int page) throws IOException;

}