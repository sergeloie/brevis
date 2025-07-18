package ru.anseranser.brevis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.dto.BrevisDto;
import ru.anseranser.brevis.service.BrevisService;

@RestController
@RequestMapping("/brevis")
@RequiredArgsConstructor
public class BrevisResource {

    private final BrevisService brevisService;

    @PostMapping
    public BrevisDto create(@Validated @RequestBody BrevisCreateDTO brevisCreateDTO) {
        return brevisService.create(brevisCreateDTO);
    }

//    @GetMapping("/{shorturl}")
//    public BrevisDto getOne(@PathVariable String shorturl) {
//        return brevisService.getBrevisByShortURL(shorturl);
//    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        return new RedirectView(brevisService.getBrevisByShortURL(shortUrl).sourceURL());
    }
}
