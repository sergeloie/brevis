package ru.anseranser.brevis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link ru.anseranser.brevis.model.Brevis}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BrevisDto(String sourceURL, String shortURL) {
}