package com.rvalente.bancodigital.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvalente.bancodigital.dto.PropostaDTO;
import com.rvalente.bancodigital.servico.ServicoConta;

@Component
public class ContaAMQPConsumer {	
	@Autowired
	private ServicoConta servicoConta;
	
	@RabbitListener(queues = "criar-contas")
	public void consumer(Message message) {
		try {
			PropostaDTO proposta = new ObjectMapper().readValue(message.getBody(), PropostaDTO.class);
			servicoConta.gerarNovaConta(proposta);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
