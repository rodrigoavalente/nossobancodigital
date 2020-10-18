package com.rvalente.bancodigital.controlador;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;

import com.rvalente.bancodigital.dominio.EntidadeBase;
import com.rvalente.bancodigital.interfaces.Validavel;
import com.rvalente.bancodigital.servico.ServicoBase;

@MappedSuperclass
public class ControladorRestBase<E extends EntidadeBase, D extends Validavel> {
	@Autowired
	protected ServicoBase<E,D> servico;
		
}
