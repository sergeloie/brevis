package ru.anseranser.brevis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.anseranser.brevis.dto.BrevisDto;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.model.Brevis;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrevisMapper {
    Brevis toEntity(BrevisCreateDTO brevisCreateDTO);
    BrevisDto toBrevisDto(Brevis brevis);
}