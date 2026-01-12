package org.example;

import org.example.dto.AuthorDto;
import org.example.dto.BookDto;
import org.example.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() { bookRepository.deleteAll(); }

    @Test
    void createAndList() throws Exception {
        BookDto b = new BookDto(null, "T1", "X-1", new AuthorDto(null, "TAuthor"));
        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(b))).andExpect(status().isOk());

        mockMvc.perform(get("/api/books")).andExpect(status().isOk());
    }
}
