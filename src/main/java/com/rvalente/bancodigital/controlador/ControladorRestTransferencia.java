package com.rvalente.bancodigital.controlador;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rvalente.bancodigital.dominio.Transferencia;
import com.rvalente.bancodigital.dto.TransferenciaDTO;
import com.rvalente.bancodigital.excecoes.BadRequestException;
import com.rvalente.bancodigital.servico.ServicoTransferencia;

@RestController
@RequestMapping("/api/transferencia")
public class ControladorRestTransferencia extends ControladorRestBase<Transferencia, TransferenciaDTO> {
	@Autowired
	private ServicoTransferencia servico;
	
	@ResponseBody
	@PutMapping(value = "/nova-transferencia")
	public ResponseEntity<Object> registrarNovaTransferencia(@RequestBody TransferenciaDTO transferencia) throws BadRequestException, AmqpException, JsonProcessingException {
		servico.registrarNovaTransferencia(transferencia);
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", "A sua transferência foi registrada com sucesso.");				
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

}
