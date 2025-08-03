package ru.anseranser.brevis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.anseranser.brevis.dto.BrevisDto;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.model.Brevis;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrevisMapper {
    Brevis toEntity(BrevisCreateDTO brevisCreateDTO);

    @Mapping(target = "shortURL", expression = "java(prefix + brevis.getShortURL())")
    BrevisDto toBrevisDto(Brevis brevis, String prefix);
}
