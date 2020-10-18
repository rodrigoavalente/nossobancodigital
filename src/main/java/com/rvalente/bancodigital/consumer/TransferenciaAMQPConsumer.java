package com.rvalente.bancodigital.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvalente.bancodigital.dto.TransferenciaDTO;
import com.rvalente.bancodigital.servico.ServicoTransferencia;

@Component
public class TransferenciaAMQPConsumer {
	@Autowired
	private ServicoTransferencia servicoTransferencia;
	
	@RabbitListener(queues = "processar-transferencias")
	public void consumer(Message message) {
		try {
			TransferenciaDTO transferencia = new ObjectMapper().readValue(message.getBody(), TransferenciaDTO.class);
			servicoTransferencia.processarTransferencia(transferencia);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
