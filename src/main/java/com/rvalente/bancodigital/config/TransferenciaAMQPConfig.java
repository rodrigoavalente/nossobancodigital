package com.rvalente.bancodigital.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferenciaAMQPConfig {
	@Value("${com.rvalente.bancodigital.amqp.transferencia.queue}")
	private String QUEUE_NAME;
	
	@Value("${com.rvalente.bancodigital.amqp.transferencia.exchange-name}")
	private String EXCHANGE_NAME;
	
	@Bean("TransferenciaExchange")
	public Exchange declareTransferenciaExchange() {
		return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
	}
	
	@Bean("TransferenciaQueue")
	public Queue declareTransferenciaQueue() {
		return QueueBuilder.durable(QUEUE_NAME).build();
	}
	
	@Bean
	public Binding declareTransferenciaBinding(@Qualifier("TransferenciaExchange") Exchange exchange, @Qualifier("TransferenciaQueue") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with("").noargs();
	}
}
