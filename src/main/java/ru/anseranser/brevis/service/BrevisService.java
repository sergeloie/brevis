package ru.anseranser.brevis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.dto.BrevisDto;
import ru.anseranser.brevis.mapper.BrevisMapper;
import ru.anseranser.brevis.model.Brevis;
import ru.anseranser.brevis.repository.BrevisRepository;

@Service
public class BrevisService {

    private final BrevisRepository brevisRepository;
    private final BrevisMapper brevisMapper;
    private final StringService stringService;
    private final BrevisPersistenceService brevisPersistenceService;
    private final String prefix;
    private final int maxAttempts;

    public BrevisService(BrevisRepository brevisRepository,
                         BrevisMapper brevisMapper,
                         StringService stringService,
                         BrevisPersistenceService brevisPersistenceService,
                         @Value("${brevis.prefix}") String prefix) {
        this.brevisRepository = brevisRepository;
        this.brevisMapper = brevisMapper;
        this.stringService = stringService;
        this.brevisPersistenceService = brevisPersistenceService;
        this.prefix = prefix;
        this.maxAttempts = 5;
    }

    public BrevisDto getBrevisByShortURL(String shortURL) {
        Brevis brevis = brevisRepository.findByShortURL(shortURL)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(shortURL)));
        return brevisMapper.toBrevisDto(brevis, prefix);
    }

    public BrevisDto create(BrevisCreateDTO brevisCreateDTO) {
        Brevis brevis = brevisMapper.toEntity(brevisCreateDTO);
        int attempts = 0;

        while (attempts < maxAttempts) {
            String shortUrl = stringService.generateShortURL();
            brevis.setShortURL(shortUrl);

            try {
                Brevis savedBrevis = brevisPersistenceService.save(brevis);
                return brevisMapper.toBrevisDto(savedBrevis, prefix);
            } catch (DataIntegrityViolationException e) {
                attempts++;
            }
        }
        throw new IllegalStateException("Failed to generate unique short link after %d attempts".formatted(maxAttempts));
    }
}
