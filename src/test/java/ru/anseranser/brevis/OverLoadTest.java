package ru.anseranser.brevis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.model.Brevis;
import ru.anseranser.brevis.repository.BrevisRepository;
import ru.anseranser.brevis.service.BrevisService;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OverLoadTest {

    @Autowired
    private BrevisRepository brevisRepository;

    @Autowired
    private BrevisService brevisService;

    @Value("${brevis.alphabet}")
    private String alphabet;

    @Test
    void overTest01() {
        for (int i = 0; i < 56; i++) {
                Brevis brevis = new Brevis();
                brevis.setShortURL(String.valueOf(alphabet.charAt(i)));
                brevis.setSourceURL("https://example.com");
                brevisRepository.save(brevis);
        }
        BrevisCreateDTO dto = new BrevisCreateDTO("https://oper.ru");
        assertThrows(IllegalStateException.class, () -> brevisService.create(dto));
    }
}
