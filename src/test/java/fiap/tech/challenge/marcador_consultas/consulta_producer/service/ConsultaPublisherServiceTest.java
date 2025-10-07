package fiap.tech.challenge.marcador_consultas.consulta_producer.service;

import fiap.tech.challenge.marcador_consultas.consulta_producer.config.RabbitConfig;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConsultaPublisherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private ConsultaPublisherService service;

    @Captor
    private ArgumentCaptor<MessagePostProcessor> messagePostProcessorCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveEnviarConsultaComAutenticacaoNoRabbit() {
        var consulta = new ConsultaIn(
                LocalDateTime.now(),
                1L,
                2L,
                "CARDIOLOGIA");

        when(loginService.autenticar()).thenReturn("fake-jwt-token");

        service.sendNewConsulta(consulta);

        verify(loginService, times(1)).autenticar();
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitConfig.EXCHANGE_NAME),
                eq(RabbitConfig.ROUTING_KEY_CONSULTA),
                eq(consulta),
                messagePostProcessorCaptor.capture());

        MessagePostProcessor processor = messagePostProcessorCaptor.getValue();
        MessageProperties props = new MessageProperties();
        Message message = new Message("{}".getBytes(), props);

        Message processed = processor.postProcessMessage(message);

        String authHeader = processed.getMessageProperties().getHeader("Authorization");
        assertEquals("Bearer fake-jwt-token", authHeader);
    }
}
