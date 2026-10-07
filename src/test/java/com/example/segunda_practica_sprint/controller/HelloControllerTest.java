package com.example.segunda_practica_sprint.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas unitarias para HelloController.
 * Se añade @WithMockUser para autenticar las peticiones a /home dado que SecurityFilterChain
 * ahora protege todas las rutas que no sean /api/info ni /actuator/health.
 */
@WebMvcTest(HelloController.class)
@WithMockUser
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 1. Test por defecto (sin enviar parámetros)
    @Test
    void testHomeDefault() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Hola Sahid!</h1>"));
    }

    // 2. Test con parámetros (enviando el parámetro nombre)
    @Test
    void testHomeConParametros() throws Exception {
        mockMvc.perform(get("/home").param("nombre", "Juan"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Hola Juan!</h1>"));
    }

    // 3. Test cambiando el idioma (enviando idioma=EN)
    @Test
    void testHomeCambiandoIdioma() throws Exception {
        mockMvc.perform(get("/home")
                        .param("idioma", "EN")
                        .param("nombre", "John"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Hello John!</h1>"));
    }
}
