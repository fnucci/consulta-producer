package fiap.tech.challenge.marcador_consultas.consulta_producer.service;

import fiap.tech.challenge.marcador_consultas.consulta_producer.config.RabbitConfig;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsultaPublisherService {

    private RabbitTemplate rabbitTemplate;
    private LoginService loginService;

    public ConsultaPublisherService(RabbitTemplate rabbitTemplate, LoginService loginService){
        this.rabbitTemplate = rabbitTemplate;
        this.loginService = loginService;
    }

    public void sendNewConsulta(ConsultaIn consulta){

        String jwtToken = loginService.autenticar();

        MessagePostProcessor processor = message -> {
            MessageProperties props = message.getMessageProperties();
            props.setHeader("Authorization", "Bearer " + jwtToken);
            return message;
        };

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY_CONSULTA,
                consulta,
                processor
        );

    }
}
