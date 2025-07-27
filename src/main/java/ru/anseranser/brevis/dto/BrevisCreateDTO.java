package ru.anseranser.brevis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;


/**
 * DTO for {@link ru.anseranser.brevis.model.Brevis}.
 * @param sourceURL the source URL string, must be a valid URL with max length 255 characters
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BrevisCreateDTO(

        @NotNull
        @Length(max = 255, message = "URLs must be 255 characters or less")
        @URL(message = "The string must be the correct URL")
        String sourceURL) {
}
