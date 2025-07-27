package ru.anseranser.brevis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.model.Brevis;
import ru.anseranser.brevis.repository.BrevisRepository;
import ru.anseranser.brevis.service.BrevisService;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = "brevis.length=1")
public class OverLoadTest {

    @Autowired
    private BrevisRepository brevisRepository;

    @Autowired
    private BrevisService brevisService;

    @Value("${brevis.alphabet}")
    private String alphabet;

    @Test
    void overTest01() {
        for (int i = 0; i < alphabet.length(); i++) {
            Brevis brevis = new Brevis();
            brevis.setShortURL(String.valueOf(alphabet.charAt(i)));
            brevis.setSourceURL("https://example.com");
            brevisRepository.save(brevis);
        }
        BrevisCreateDTO dto = new BrevisCreateDTO("https://example.com/overload");
        assertThrows(IllegalStateException.class, () -> brevisService.create(dto));
    }
}
