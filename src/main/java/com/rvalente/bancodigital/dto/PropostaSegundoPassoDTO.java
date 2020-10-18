package com.rvalente.bancodigital.dto;

import java.util.ArrayList;
import java.util.List;

import com.rvalente.bancodigital.interfaces.Validavel;
import com.rvalente.bancodigital.utilidades.UtilitarioValidacao;

import lombok.Data;

@Data
public class PropostaSegundoPassoDTO implements Validavel {
	
	private String cep;
	
	private String rua;
	
	private String bairro;
	
	private String complemento;
	
	private String cidade;
	
	private String estado;
	
	@Override
	public List<String> validar() {
		List<String> erros = new ArrayList<>();
		
		UtilitarioValidacao.validarRequerido(cep, "CEP", erros);
		UtilitarioValidacao.validarCEP(cep, erros);
		UtilitarioValidacao.validarRequerido(rua, "Rua", erros);
		UtilitarioValidacao.validarRequerido(bairro, "Bairro", erros);
		UtilitarioValidacao.validarRequerido(complemento, "Complemento", erros);
		UtilitarioValidacao.validarRequerido(cidade, "Cidade", erros);
		UtilitarioValidacao.validarRequerido(estado, "Estado", erros);
		
		return erros;
	}
}
