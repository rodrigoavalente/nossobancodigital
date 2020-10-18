package com.rvalente.bancodigital.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rvalente.bancodigital.interfaces.Validavel;
import com.rvalente.bancodigital.utilidades.UtilitarioValidacao;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PropostaPrimeiroPassoDTO implements Validavel {	
	private String nome;
	
	private String sobrenome;
	
	private String email;
	
	private Date dataNascimento;
	
	private String cpf;
	
	public List<String> validar() {
		List<String> erros = new ArrayList<>();
		
		UtilitarioValidacao.validarRequerido(nome, "Nome", erros);
		UtilitarioValidacao.validarRequerido(sobrenome, "Sobrenome", erros);
		UtilitarioValidacao.validarRequerido(email, "e-mail", erros);
		UtilitarioValidacao.validarEmail(email, erros);
		UtilitarioValidacao.validarRequerido(dataNascimento, "Data de Nascimento", erros);
		UtilitarioValidacao.validarDataAnteriorDataAtual(dataNascimento, "Data de Nascimento", erros);
		UtilitarioValidacao.validarMaioridade(dataNascimento, erros);
		UtilitarioValidacao.validarRequerido(cpf, "CPF", erros);		
		
		return erros;
	}
	
	public void removerCaracteresEspeciaisCPF() {
		cpf = cpf.replaceAll("[\\D]+", "");
	}
}
