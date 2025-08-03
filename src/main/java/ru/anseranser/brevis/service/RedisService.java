package ru.anseranser.brevis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import ru.anseranser.brevis.dto.BrevisStatsDTO;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String URL_KEY_PREFIX = "url:";
    private static final String HITS_KEY_PREFIX = "hits:";
    private static final String TOP_KEY = "top_urls";

    private static final Duration TTL = Duration.ofDays(30);


    /*
     * Сохраняем ссылку в Redis с TTL
     */
    public void cacheUrl(String shortUrl, String sourceUrl) {
        stringRedisTemplate.opsForValue()
                .set(URL_KEY_PREFIX + shortUrl, sourceUrl, TTL);
    }

    /*
     * Получаем ссылку по короткому коду
     */
    public String getUrl(String shortUrl) {
        return stringRedisTemplate.opsForValue()
                .get(URL_KEY_PREFIX + shortUrl);
    }

    /*
     * Инкрементируем счётчик переходов
     * и обновляем сортированное множество топ ссылок
     */
    public void incrementHits(String shortUrl) {
        Long hits = stringRedisTemplate.opsForValue()
                .increment(HITS_KEY_PREFIX + shortUrl);
        stringRedisTemplate.opsForZSet()
                .add(TOP_KEY, shortUrl, hits != null ? hits.doubleValue() : 1.0);
    }

    /*
     * Получаем количество переходов по ссылке
     */
    public Long getHits(String shortUrl) {
        String hitString = stringRedisTemplate.opsForValue()
                .get(HITS_KEY_PREFIX + shortUrl);
        return hitString != null ? Long.parseLong(hitString) : 0;
    }

    /*
     * Получаем топ N ссылок по количеству переходов
     */
    public List<BrevisStatsDTO> getTopLinks(long limit) {
        // 1. Получаем топ N ссылок с их счетчиками
        Set<ZSetOperations.TypedTuple<String>> topVisitedShortURLs =
                stringRedisTemplate.opsForZSet()
                        .reverseRangeWithScores(TOP_KEY, 0, limit - 1);

        if (topVisitedShortURLs == null || topVisitedShortURLs.isEmpty()) {
            return Collections.emptyList();
        }

        List<BrevisStatsDTO> brevisStatsDTOs = new ArrayList<>(topVisitedShortURLs.size());

        for (ZSetOperations.TypedTuple<String> tuple : topVisitedShortURLs) {
            String shortURL = tuple.getValue();
            Long clickCount = tuple.getScore() != null ? tuple.getScore().longValue() : 0L;
            brevisStatsDTOs.add(new BrevisStatsDTO(null, shortURL, clickCount));
        }

        // 2. Получаем пачкой исходные ссылки
        List<Object> results = stringRedisTemplate.executePipelined(
                new SessionCallback<List<String>>() {
                    @Override
                    public List<String> execute(RedisOperations operations) {
                        for (BrevisStatsDTO brevisStatsDTO : brevisStatsDTOs) {
                            operations.opsForValue().get(brevisStatsDTO.getShortURL());
                        }
                        return null;
                    }
                }
        );

        for (int i = 0; i < brevisStatsDTOs.size(); i++) {
            Object result = results.get(i);
            if (result instanceof String sourceURL) {
                brevisStatsDTOs.get(i).setSourceURL(sourceURL);
            } else {
                brevisStatsDTOs.get(i).setSourceURL(null);
            }
        }

        return brevisStatsDTOs;
    }
}
