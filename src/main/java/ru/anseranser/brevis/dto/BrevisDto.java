package ru.anseranser.brevis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for {@link ru.anseranser.brevis.model.Brevis}.
 * @param sourceURL the source URL string
 * @param shortURL the short URL string for the source URL
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BrevisDto(String sourceURL, String shortURL) {
}
