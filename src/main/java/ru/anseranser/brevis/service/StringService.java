package ru.anseranser.brevis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class StringService {

    private final int shortUrlLength;
    private final String alphabet;
    private final SecureRandom random;

    public StringService(@Value("${brevis.length}") int shortUrlLength,
                         @Value("${brevis.alphabet}") String alphabet) {
        this.shortUrlLength = shortUrlLength;
        this.alphabet = alphabet;
        random = new SecureRandom();
    }

    /**
     * @param length Длина строки для генерации
     * @return Случайная строка заданной длины из букв и цифр без неоднозначных символов
     */
    private String generateRansomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(randomIndex));
        }

        return sb.toString();
    }

    /**
     * @return Короткий URL длины brevis.length из application.properties
     */
    public String generateShortURL() {
        return generateRansomString(shortUrlLength);
    }

}
