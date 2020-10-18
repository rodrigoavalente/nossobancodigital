package com.rvalente.bancodigital.servico;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvalente.bancodigital.dominio.Conta;
import com.rvalente.bancodigital.dominio.Transferencia;
import com.rvalente.bancodigital.dto.TransferenciaDTO;
import com.rvalente.bancodigital.excecoes.BadRequestException;
import com.rvalente.bancodigital.repositorio.RepositorioTransferencia;


@Service
public class ServicoTransferencia extends ServicoBase<Transferencia, TransferenciaDTO> {
	@Autowired	
	private RepositorioTransferencia repositorio;
	
	@Autowired
	private ServicoConta servicoConta;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${com.rvalente.bancodigital.amqp.transferencia.exchange-name}")
	private String EXCHANGE_NAME;
	
	private Logger logger = LoggerFactory.getLogger(ServicoTransferencia.class);

	public void registrarNovaTransferencia(TransferenciaDTO dto) throws BadRequestException, AmqpException, JsonProcessingException {
		if (repositorio.existsByCodigoUnico(dto.getCodigoUnico()))
			logger.info("Um transferência com esse código já foi processada.");
		else {
			List<String> erros = dto.validar();
			
			if (!erros.isEmpty())
				throw new BadRequestException(erros);			
			Transferencia transferencia = mapear(dto);
			repositorio.save(transferencia);
			
			rabbitTemplate.convertAndSend(EXCHANGE_NAME, "",
					new ObjectMapper().writeValueAsString(mapear(transferencia)));
		}			
	}
	
	public void processarTransferencia(TransferenciaDTO dto) {
		Conta conta = servicoConta.findByAgenciaAndConta(dto.getAgenciaDestino(), dto.getContaDestino());
		
		if (Objects.isNull(conta))
			logger.info("Não existe conta com as informações prestadas cadastrada no sistema.");
		else {
			BigDecimal saldoAnterior = conta.getSaldo();
			BigDecimal saldoAtual = saldoAnterior.add(dto.getValorTransferencia());
			
			conta.setSaldo(saldoAtual);
			
			servicoConta.salvar(conta);
		}
	}
}
