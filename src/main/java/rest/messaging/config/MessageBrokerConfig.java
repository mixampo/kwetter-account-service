package rest.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MessageBrokerConfig {
    private Map<String, Object> arguments = new HashMap<String, Object>();
    int ttl = 3000;

    @Value("${kwetter.rabbitmq.queue}")
    private String queueName;
    @Value("${kwetter.rabbitmq.exchange}")
    private String exchange;
    @Value("${kwetter.rabbitmq.routingkey}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    public TopicExchange exchange() {
        arguments.put("x-message-ttl", ttl);
        return new TopicExchange(exchange, false, false, arguments);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
