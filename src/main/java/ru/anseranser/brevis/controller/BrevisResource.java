package ru.anseranser.brevis.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.dto.BrevisDto;
import ru.anseranser.brevis.service.BrevisService;

@RestController
@RequestMapping("/brevis")
public class BrevisResource {

    private final BrevisService brevisService;
    private final boolean isRedirect;

    public BrevisResource(BrevisService brevisService,
                          @Value("${brevis.redirect:false}")boolean isRedirect) {
        this.brevisService = brevisService;
        this.isRedirect = isRedirect;
    }

    @PostMapping
    public BrevisDto create(@Validated @RequestBody BrevisCreateDTO brevisCreateDTO) {
        return brevisService.create(brevisCreateDTO);
    }

    @GetMapping("/{shortUrl}")
    public Object getShortUrl(@PathVariable String shortUrl) {
        return isRedirect
                ? new RedirectView(brevisService.getBrevisByShortURL(shortUrl).sourceURL())
                : brevisService.getBrevisByShortURL(shortUrl);
    }
}
