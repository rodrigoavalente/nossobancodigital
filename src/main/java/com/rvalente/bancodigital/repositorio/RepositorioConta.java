package com.rvalente.bancodigital.repositorio;

import com.rvalente.bancodigital.dominio.Conta;

public interface RepositorioConta extends RepositorioBase<Conta> {
	Conta findByAgenciaAndConta(int agencia, int conta);
}
