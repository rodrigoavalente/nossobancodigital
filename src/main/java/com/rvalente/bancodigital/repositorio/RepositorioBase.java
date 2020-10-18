package com.rvalente.bancodigital.repositorio;

import org.springframework.data.repository.CrudRepository;

import com.rvalente.bancodigital.dominio.EntidadeBase;

public interface RepositorioBase<E extends EntidadeBase> extends CrudRepository<E, Integer> {

}
