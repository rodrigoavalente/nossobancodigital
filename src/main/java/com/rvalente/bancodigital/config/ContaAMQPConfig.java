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
public class ContaAMQPConfig {
	@Value("${com.rvalente.bancodigital.amqp.conta.queue}")
	private String QUEUE_NAME;
	
	@Value("${com.rvalente.bancodigital.amqp.conta.exchange-name}")
	private String EXCHANGE_NAME;
	
	@Bean("ContaExchange")
	public Exchange declareContaExchange() {
		return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
	}
	
	@Bean("ContaQueue")
	public Queue declareContaQueue() {
		return QueueBuilder.durable(QUEUE_NAME).build();
	}
	
	@Bean
	public Binding declareContaBinding(@Qualifier("ContaExchange") Exchange exchange, @Qualifier("ContaQueue") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with("").noargs();
	}
}
