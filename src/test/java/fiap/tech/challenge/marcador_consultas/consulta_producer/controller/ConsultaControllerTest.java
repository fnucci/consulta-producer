package fiap.tech.challenge.marcador_consultas.consulta_producer.controller;

import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import fiap.tech.challenge.marcador_consultas.consulta_producer.service.ConsultaPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConsultaControllerTest {

    @Mock
    private ConsultaPublisherService consultaPublisherService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ConsultaController consultaController = new ConsultaController(consultaPublisherService);
        mockMvc = MockMvcBuilders.standaloneSetup(consultaController).build();
    }

    @Test
    void deveAgendarConsulta_QuandoPOSTChamado() throws Exception {
        doNothing().when(consultaPublisherService).sendNewConsulta(any(ConsultaIn.class));

        String jsonRequest = """
            {
                "pacienteId": 1,
                "dataConsulta": "2025-10-07T10:00:00"
            }
            """;

        mockMvc.perform(post("/agendarConsulta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(content().string("Solicitacao de consulta enviada para marcação"));

        verify(consultaPublisherService).sendNewConsulta(any(ConsultaIn.class));
    }
}