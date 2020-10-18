package com.rvalente.bancodigital.dominio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public class EntidadeBase {
	@Id
	@GeneratedValue
	private int id;
	
	@Column(columnDefinition = "boolean default true")
	private boolean ativo = true;
	
	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtualizacao;
}
