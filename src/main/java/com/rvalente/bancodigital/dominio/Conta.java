package com.rvalente.bancodigital.dominio;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Conta extends EntidadeBase {
	private int codigoBanco;
	
	private int agencia;
	
	private int conta;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_proposta_origem")
	private Proposta propostaOrigem;
	
	private BigDecimal saldo;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_pessoa")
	private Pessoa correntista;
}
