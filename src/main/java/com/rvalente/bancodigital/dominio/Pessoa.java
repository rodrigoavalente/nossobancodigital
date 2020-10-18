package com.rvalente.bancodigital.dominio;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.rvalente.bancodigital.interfaces.Validavel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Pessoa extends EntidadeBase {
	private String nome;
	
	private String sobrenome;
	
	private String email;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	private String cpf;
	
	private String cep;
	
	private String rua;
	
	private String bairro;
	
	private String complemento;
	
	private String cidade;
	
	private String estado;
}
