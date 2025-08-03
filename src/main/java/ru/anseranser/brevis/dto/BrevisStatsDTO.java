package ru.anseranser.brevis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@NoArgsConstructor
@AllArgsConstructor
public class BrevisStatsDTO {
    private String sourceURL;
    private String shortURL;
    private Long clickCount;
}
