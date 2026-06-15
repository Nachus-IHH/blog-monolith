package com.blog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.user.dto.RequestUsuarioDto;
import com.user.dto.ResponseUsuarioDto;
import com.user.exception.EmailAlreadyExistsException;
import com.user.domain.Usuario;
import com.user.domain.Role;
import com.user.UsuarioService;
import com.user.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService UsuarioService;

    private RequestUsuarioDto requestDto;
    private Usuario usuarioGuardado;

    @BeforeEach
    void setUp() {
        requestDto = new RequestUsuarioDto("nachus", "password", "nachus@example.com");

        usuarioGuardado = Usuario.builder()
                .id(1L)
                .username("nachus")
                .password("passwordEncriptadaBCrypt")
                .email("nachus@example.com")
                .role(Role.USER)
                .build();
    }

    /**
     * CrearUsuarioTest - Caminos para crear usuario
     */
    @Nested
    class CrearUsuarioTests {
        @Test
        @DisplayName("Happy Path")
        void crearUsuario_HappyPath_ReturnUsuarioRegistrado() {
            /* ARRANGE - prepara el escenario */
            when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(requestDto.password())).thenReturn("passwordEncriptadaBCrypt");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

            /* ACT - ejecutar la accion */
            ResponseUsuarioDto respuesta = UsuarioService.crearUsuario(requestDto);

            /* ASSERT - verificar resultados */
            assertNotNull(respuesta);
            assertEquals(1L, respuesta.id());
            assertEquals("nachus", respuesta.username());
            assertEquals(Role.USER, respuesta.role());

            /* verificar que el repo se uso una vez */
            verify(usuarioRepository, times(1)).existsByEmail(anyString());
            verify(passwordEncoder, times(1)).encode(anyString());
            verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Lanza excepcion si el email ya existe")
        void crearUsuario_EmailYaExiste_LanzaException() {
            /* ARRANGE */
            when(usuarioRepository.existsByEmail(requestDto.email())).thenReturn(true);

            /* ACT & ASSERT */
            assertThrows(EmailAlreadyExistsException.class, () -> {
                UsuarioService.crearUsuario(requestDto);
            });

            /* VERIFY */
            verify(usuarioRepository, times(1)).existsByEmail(anyString());
            verify(passwordEncoder, never()).encode(anyString());
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }

    /*
     * @Nested
     * class ObtenerUsuarioTests {
     * 
     * @Test
     * 
     * @DisplayName("Happy Path")
     * void obtenerPorId_IdExistente_ReturnUsuario() {
     * }
     * 
     * @Test
     * 
     * @DisplayName("ResourceNotFoundException si el ID no existe")
     * void obtenerPorId_IdNoExiste_LanzaNotFound() {
     * }
     * }
     */
}
