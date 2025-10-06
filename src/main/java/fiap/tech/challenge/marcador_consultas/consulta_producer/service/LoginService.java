package fiap.tech.challenge.marcador_consultas.consulta_producer.service;

import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.LoginRequest;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.out.LoginResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService {

    private RestTemplate restTemplate;

    public LoginService (RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String autenticar() {
        String url = "http://localhost:8080/api/auth/login";

        LoginRequest request = new LoginRequest("enfermeiro", "guest");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<LoginResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, LoginResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().token();
        } else {
            throw new RuntimeException("Falha ao obter token: " + response.getStatusCode());
        }
    }
}
