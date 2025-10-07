package fiap.tech.challenge.marcador_consultas.consulta_producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.junit.jupiter.api.Assertions.*;

class RabbitConfigTest {

    private final RabbitConfig config = new RabbitConfig();

    @Test
    void deveCriarExchange() {
        DirectExchange exchange = config.exchange();
        assertNotNull(exchange);
        assertEquals(RabbitConfig.EXCHANGE_NAME, exchange.getName());
    }

    @Test
    void deveCriarConsultaQueue() {
        Queue queue = config.consultaQueue();
        assertNotNull(queue);
        assertEquals(RabbitConfig.CONSULTA_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void deveCriarBinding() {
        Binding binding = config.binding();
        assertNotNull(binding);
        assertEquals(RabbitConfig.ROUTING_KEY_CONSULTA, binding.getRoutingKey());
    }

    @Test
    void deveCriarProducerMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonMessageConverter converter = config.producerMessageConverter(objectMapper);
        assertNotNull(converter);
    }

    @Test
    void deveCriarRabbitTemplate() {
        ConnectionFactory connectionFactory = new CachingConnectionFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonMessageConverter converter = config.producerMessageConverter(objectMapper);
        RabbitTemplate template = config.rabbitTemplate(connectionFactory, converter);
        assertNotNull(template);
        assertEquals(converter, template.getMessageConverter());
    }
}