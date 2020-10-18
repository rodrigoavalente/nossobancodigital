package com.rvalente.bancodigital.dominio;

import lombok.Getter;

@Getter
public enum TipoDocumentoEnum {
	COPIA_CPF("Cópia do CPF");
	
	private final String descricao;
	
	TipoDocumentoEnum(final String descricao) {
		this.descricao = descricao;
	}
	
	public String toString() {
		return descricao;
	}
}
