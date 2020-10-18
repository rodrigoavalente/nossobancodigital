package com.rvalente.bancodigital.dominio;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Transferencia extends EntidadeBase {
	private BigDecimal valorTransferencia;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRealizacao;
	
	private String documentoIdentificadorOrigem;
	
	private int bancoOrigem;
	
	private int contaOrigem;
	
	private int agenciaOrigem;
	
	private String codigoUnico;
	
	private int contaDestino;
	
	private int agenciaDestino;
	
	private boolean processada;
}
