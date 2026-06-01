package com.example.evento.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_INSCRICAO = "email.inscricao.queue";

    @Bean
    public Queue filaInscricao() {
        return new Queue(FILA_INSCRICAO, true);
    }
}