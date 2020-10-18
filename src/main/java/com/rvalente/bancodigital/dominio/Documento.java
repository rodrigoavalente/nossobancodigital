package com.rvalente.bancodigital.dominio;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Documento extends EntidadeBase {
	@Enumerated(EnumType.STRING)
	private TipoDocumentoEnum tipoDocumento;
	
	private String caminhoDocumento;
}
