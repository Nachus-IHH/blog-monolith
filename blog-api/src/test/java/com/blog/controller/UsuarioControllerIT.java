package com.blog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import com.config.ApiPaths;
import com.config.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.user.dto.RequestUsuarioDto;
import com.user.dto.ResponseUsuarioDto;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private RequestUsuarioDto requestDto;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        requestDto = new RequestUsuarioDto(
                "nachus_it",
                "securePassword",
                "nachus_it@example.com");
    }

    @Nested
    @DisplayName("POST /user")
    class PostUsuarioTests {

        @Test
        @DisplayName("Happy Path: Registra al usuario y Return 201 IS_CREATED")
        void postUsuario_CuandoJsonEsValido_Return201CreatedAndData() throws Exception {

            /* ARRANGE */
            String jsonRequest = objectMapper.writeValueAsString(requestDto);

            /* ACT */
            String jsonResponse = mockMvc.perform(post(ApiPaths.USER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            ResponseUsuarioDto respuesta = objectMapper.readValue(jsonResponse, ResponseUsuarioDto.class);

            /* ASSERT */
            assertNotNull(respuesta.id());
            assertEquals("nachus_it", respuesta.username());
            assertEquals("nachus_it@example.com", respuesta.email());
        }

        @Test
        @DisplayName("Return 400 BAD_REQUEST - email duplicado")
        void postUsuario_CuandoEmailDuplicado_LanzaEmailDuplicated() throws Exception {

            /* ARRANGE */
            String jsonRequest = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post(ApiPaths.USER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isCreated());

            /* ACT */
            String jsonResponse = mockMvc.perform(post(ApiPaths.USER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            ErrorResponseDto error = objectMapper.readValue(jsonResponse, ErrorResponseDto.class);

            /* ASSERT DEL ERROR */
            assertEquals("Este email ya esta en uso", error.message());
            assertEquals(400, error.statusCode());
            assertNotNull(error.timestamp());
        }
    }
}
