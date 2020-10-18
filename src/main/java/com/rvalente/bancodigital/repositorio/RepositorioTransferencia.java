package com.rvalente.bancodigital.repositorio;

import com.rvalente.bancodigital.dominio.Transferencia;

public interface RepositorioTransferencia extends RepositorioBase<Transferencia> {
	boolean existsByCodigoUnico(String codigoUnico);
}
