package ru.anseranser.brevis.getshort;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.anseranser.brevis.TestConfig;
import ru.anseranser.brevis.dto.BrevisCreateDTO;
import ru.anseranser.brevis.dto.BrevisDTO;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = "brevis.redirect=false")
@AutoConfigureMockMvc
@ImportTestcontainers(TestConfig.class)
class JsonTest {

    @Value("${brevis.prefix}")
    private String prefixUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void jsonTest() throws Exception {
        String url = "https://example.com";

        BrevisCreateDTO brevisCreateDTO = new BrevisCreateDTO(url);
        String result = mockMvc.perform(post("/brevis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brevisCreateDTO)))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        BrevisDTO brevisDto = objectMapper.readValue(result, BrevisDTO.class);

        String response = mockMvc.perform(get(brevisDto.shortURL()))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(response).and(
                v -> v.node("sourceURL").isEqualTo(url),
                v -> v.node("shortURL").isEqualTo(brevisDto.shortURL())
        );
    }
}
