package fiap.tech.challenge.marcador_consultas.consulta_producer.service;

import fiap.tech.challenge.marcador_consultas.consulta_producer.config.RabbitConfig;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsultaPublisherService {

    private RabbitTemplate rabbitTemplate;

    public ConsultaPublisherService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNewConsulta(ConsultaIn consulta){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY_CONSULTA, consulta);
    }
}
