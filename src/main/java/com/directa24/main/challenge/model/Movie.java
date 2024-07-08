package com.directa24.main.challenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Movie {
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Director")
    private String Director;
    @JsonProperty("Released")
    private String Released;
}