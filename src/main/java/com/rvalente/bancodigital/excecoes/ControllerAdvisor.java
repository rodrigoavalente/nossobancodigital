package com.rvalente.bancodigital.excecoes;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> tratarExcecaoRequisicaoMalFormada(BadRequestException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		
		body.put("mensagem", ex.getMessage());
		body.put("erros", ex.getErros());		
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> tratarExcecaoRecursoNaoEncontrado(NotFoundException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		
		body.put("mensagem", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UnprocessableEntityException.class)
	public ResponseEntity<Object> tratarExcecaoEntidadeInprocessavel(UnprocessableEntityException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		
		body.put("mensagem", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(value = {IOException.class, MessagingException.class, AmqpException.class, JsonProcessingException.class})
	public ResponseEntity<Object> tratarIOException(IOException ex, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		
		body.put("mensagem", "Ocorreu um erro ao processar a sua requisição, tente novamente em instantes.");
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
