package com.rvalente.bancodigital.controlador;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rvalente.bancodigital.dominio.Proposta;
import com.rvalente.bancodigital.dto.PropostaDTO;
import com.rvalente.bancodigital.dto.PropostaPrimeiroPassoDTO;
import com.rvalente.bancodigital.dto.PropostaSegundoPassoDTO;
import com.rvalente.bancodigital.excecoes.BadRequestException;
import com.rvalente.bancodigital.excecoes.NotFoundException;
import com.rvalente.bancodigital.excecoes.UnprocessableEntityException;
import com.rvalente.bancodigital.servico.ServicoProposta;

@RestController
@RequestMapping("/api/proposta")
public class ControladorRestProposta extends ControladorRestBase<Proposta, PropostaDTO>{
	
	@Autowired
	private ServicoProposta servico;

	@ResponseBody
	@PutMapping(value = "/nova-proposta")
	public ResponseEntity<Object> registrarNovaProposta(HttpServletResponse response, @RequestBody PropostaPrimeiroPassoDTO primeiroPassoDTO) throws BadRequestException {
		String location = servico.registrarNovaProposta(primeiroPassoDTO);
	
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", "A sua proposta foi registrada com sucesso.");
		
		response.addHeader("Location", location);
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@PutMapping(value = "/cadastrar-endereco/{idProposta}")
	public ResponseEntity<Object> registrarEnderecoNovaProposta(HttpServletResponse response,
			@PathVariable("idProposta") int idProposta, @RequestBody PropostaSegundoPassoDTO segundoPassoDTO)
			throws NotFoundException, BadRequestException {
		
		String location = servico.registrarEnderecoNovaProposta(idProposta, segundoPassoDTO);
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", "O endereço foi salvo com sucesso.");
		
		response.addHeader("Location", location);
		return new ResponseEntity<>(body, HttpStatus.CREATED);		
	}
	
	@ResponseBody
	@PutMapping(value = "/enviar-documento/{idProposta}")
	public ResponseEntity<Object> realizarUploadCopiaCPF(HttpServletResponse response,
			@PathVariable("idProposta") int idProposta, @RequestParam("arquivo") MultipartFile arquivo)
			throws NotFoundException, BadRequestException, UnprocessableEntityException, IOException {
		
		String location = servico.realizarUploadCopiaCPF(idProposta, arquivo);
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", "A cópia do CPF foi salva com sucesso.");
		
		response.addHeader("Location", location);		
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@GetMapping(value = "/revisar/{idProposta}")
	public ResponseEntity<Object> recuperarDadosPropostaParaAceitacao(@PathVariable("idProposta") int idProposta)
		throws NotFoundException, UnprocessableEntityException {
		
		PropostaDTO proposta = servico.revisarProposta(idProposta);
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", "Deseja aceitar a seguinte proposta com os seguintes dados?");
		body.put("proposta", proposta);		
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	@ResponseBody
	@PutMapping(value = "/aceitar/{idProposta}")
	public ResponseEntity<Object> aceitarProposta(@PathVariable("idProposta") int idProposta) throws NotFoundException, UnprocessableEntityException, MessagingException, AmqpException, JsonProcessingException {
		String mensagem = servico.processarAceiteDeProposta(idProposta, true);
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", mensagem);				
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	@ResponseBody
	@PutMapping(value = "/rejeitar/{idProposta}")
	public ResponseEntity<Object> rejeitarProposta(@PathVariable("idProposta") int idProposta) throws NotFoundException, UnprocessableEntityException, MessagingException, AmqpException, JsonProcessingException {
		String mensagem = servico.processarAceiteDeProposta(idProposta, false);
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", mensagem);				
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}
