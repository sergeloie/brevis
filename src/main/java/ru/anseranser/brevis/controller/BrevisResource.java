package ru.anseranser.brevis.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.dto.BrevisDto;
import ru.anseranser.brevis.service.BrevisService;

import java.net.URI;

@RestController
@RequestMapping("/brevis")
public class BrevisResource {

    private final BrevisService brevisService;
    private final boolean isRedirect;

    public BrevisResource(BrevisService brevisService,
                          @Value("${brevis.redirect:false}") boolean isRedirect) {
        this.brevisService = brevisService;
        this.isRedirect = isRedirect;
    }

    @PostMapping
    public ResponseEntity<BrevisDto> create(@Validated @RequestBody BrevisCreateDTO brevisCreateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(brevisService.create(brevisCreateDTO));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> getShortUrl(@PathVariable String shortUrl) {
        BrevisDto brevisDto = brevisService.getBrevisByShortURL(shortUrl);
        return isRedirect
                ?
                ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(brevisDto.sourceURL()))
                        .build()
                :
                ResponseEntity.status(HttpStatus.OK)
                        .body(brevisDto);
    }
}
