package com.rvalente.bancodigital.excecoes;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnprocessableEntityException extends Exception {	
	private static final long serialVersionUID = 1L;

	public UnprocessableEntityException(String mensagem) {
		super(mensagem);
	}
}
