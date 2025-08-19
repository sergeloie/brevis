package ru.anseranser.brevis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.dto.BrevisDTO;
import ru.anseranser.brevis.mapper.BrevisMapper;
import ru.anseranser.brevis.model.Brevis;
import ru.anseranser.brevis.repository.BrevisRepository;

@Service
public class BrevisService {

    private final BrevisRepository brevisRepository;
    private final BrevisMapper brevisMapper;
    private final StringService stringService;
    private final RedisService redisService;
    private final BrevisPersistenceService brevisPersistenceService;
    private final String prefix;
    private final int maxAttempts;

    public BrevisService(BrevisRepository brevisRepository,
                         BrevisMapper brevisMapper,
                         StringService stringService, RedisService redisService,
                         BrevisPersistenceService brevisPersistenceService,
                         @Value("${brevis.prefix}") String prefix) {
        this.brevisRepository = brevisRepository;
        this.brevisMapper = brevisMapper;
        this.stringService = stringService;
        this.redisService = redisService;
        this.brevisPersistenceService = brevisPersistenceService;
        this.prefix = prefix;
        this.maxAttempts = 5;
    }

    public BrevisDTO getBrevisByShortURL(String shortURL) {

        String sourceUrl = redisService.getUrl(shortURL);
        Brevis brevis;
        if (sourceUrl != null) {
            brevis = new Brevis();
            brevis.setShortURL(shortURL);
            brevis.setSourceURL(sourceUrl);

        } else {
            brevis = brevisRepository.findByShortURL(shortURL)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(shortURL)));
            redisService.cacheUrl(shortURL, brevis.getSourceURL());
        }
        redisService.incrementHits(shortURL);
        return brevisMapper.toBrevisDto(brevis, prefix);
    }

    public BrevisDTO create(BrevisCreateDTO brevisCreateDTO) {
        Brevis brevis = brevisMapper.toEntity(brevisCreateDTO);
        int attempts = 0;

        while (attempts < maxAttempts) {
            String shortUrl = stringService.generateShortURL();
            brevis.setShortURL(shortUrl);

            try {
//                Brevis savedBrevis = brevisPersistenceService.save(brevis);
                Brevis savedBrevis = brevisRepository.save(brevis);
                return brevisMapper.toBrevisDto(savedBrevis, prefix);
            } catch (DataIntegrityViolationException e) {
                attempts++;
            }
        }
        throw new IllegalStateException("Failed to generate unique short link after %d attempts".formatted(maxAttempts));
    }
}
