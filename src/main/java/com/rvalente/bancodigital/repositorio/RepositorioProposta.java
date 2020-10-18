package com.rvalente.bancodigital.repositorio;

import com.rvalente.bancodigital.dominio.Proposta;

public interface RepositorioProposta extends RepositorioBase<Proposta> {
	boolean existsByCpf(String cpf);
	
	boolean existsByEmail(String email);	
	
	Proposta findByCpf(String cpf);
}
