package fiap.tech.challenge.marcador_consultas.consulta_producer.service;

import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.out.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        try {
            var field = LoginService.class.getDeclaredField("authUrl");
            field.setAccessible(true);
            field.set(loginService, "http://localhost:8080/api/auth/login");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deveAutenticarERetornarTokenQuandoSucesso() {
        var loginResponse = new LoginResponse("token123");
        ResponseEntity<LoginResponse> responseEntity = ResponseEntity.ok(loginResponse);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(LoginResponse.class)
        )).thenReturn(responseEntity);

        String token = loginService.autenticar();

        assertEquals("token123", token);
        verify(restTemplate).exchange(
                eq("http://localhost:8080/api/auth/login"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(LoginResponse.class)
        );
    }

    @Test
    void deveLancarExcecaoQuandoStatusNaoFor2xx() {
        ResponseEntity<LoginResponse> responseEntity =
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(LoginResponse.class)
        )).thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> loginService.autenticar());
        assertTrue(exception.getMessage().contains("Falha ao obter token"));
    }

    @Test
    void deveLancarExcecaoQuandoBodyForNuloMesmoComStatus2xx() {
        ResponseEntity<LoginResponse> responseEntity = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(LoginResponse.class)
        )).thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> loginService.autenticar());
        assertTrue(exception.getMessage().contains("Falha ao obter token"));
    }
}
