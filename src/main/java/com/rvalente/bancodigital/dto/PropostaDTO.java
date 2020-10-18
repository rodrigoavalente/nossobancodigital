package com.rvalente.bancodigital.dto;

import java.util.Date;
import java.util.List;

import com.rvalente.bancodigital.interfaces.Validavel;

import lombok.Data;

@Data
public class PropostaDTO implements Validavel {
	private String nome;
	
	private String sobrenome;
	
	private String email;
	
	private Date dataNascimento;
	
	private String cpf;
	
	private String cep;
	
	private String rua;
	
	private String bairro;
	
	private String complemento;
	
	private String cidade;
	
	private String estado;
	

	@Override
	public List<String> validar() {
		// TODO Auto-generated method stub
		return null;
	}

}
