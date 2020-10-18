package com.rvalente.bancodigital.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.rvalente.bancodigital.interfaces.Validavel;
import com.rvalente.bancodigital.utilidades.UtilitarioValidacao;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferenciaDTO implements Validavel {
	private BigDecimal valorTransferencia;	
	
	private String documentoIdentificadorOrigem;
	
	private Integer bancoOrigem;
	
	private Integer contaOrigem;
	
	private Integer agenciaOrigem;
	
	private String codigoUnico;
	
	private Integer contaDestino;
	
	private Integer agenciaDestino;

	@Override
	public List<String> validar() {
		List<String> erros = new ArrayList<>();
		
		UtilitarioValidacao.validarRequerido(valorTransferencia, "Valor da Transferência", erros);
		UtilitarioValidacao.validarRequerido(documentoIdentificadorOrigem, "Documento de Origem", erros);
		UtilitarioValidacao.validarRequerido(bancoOrigem, "Banco de Origem", erros);
		UtilitarioValidacao.validarRequerido(contaOrigem, "Conta de Origem", erros);
		UtilitarioValidacao.validarRequerido(codigoUnico, "Código Único", erros);
		UtilitarioValidacao.validarRequerido(contaDestino, "Conta de Destino", erros);
		UtilitarioValidacao.validarRequerido(agenciaDestino, "Agência de Destino", erros);	
		
		return erros;
	}
}
