package ru.anseranser.brevis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.anseranser.brevis.model.Brevis;

import java.util.Optional;

public interface BrevisRepository extends JpaRepository<Brevis, Long> {
    Optional<Brevis> findByShortURL(String shortURL);

}