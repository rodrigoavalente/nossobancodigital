package com.rvalente.bancodigital.excecoes;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class BadRequestException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private @Getter @Setter List<String> erros;
	
	public BadRequestException(List<String> erros) {
		super("Um ou mais erros foram encontrados ao processar a sua requisição, verifique as informações prestadas e tente novamente.");
		setErros(erros);
	}
}
