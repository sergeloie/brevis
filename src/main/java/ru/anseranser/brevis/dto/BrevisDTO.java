package ru.anseranser.brevis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link ru.anseranser.brevis.model.Brevis}.
 * @param sourceURL
 * @param shortURL
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BrevisDTO(String sourceURL, String shortURL) {
}
