package com.rvalente.bancodigital.servico;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.rvalente.bancodigital.dominio.EntidadeBase;
import com.rvalente.bancodigital.excecoes.BadRequestException;
import com.rvalente.bancodigital.excecoes.NotFoundException;
import com.rvalente.bancodigital.interfaces.Validavel;
import com.rvalente.bancodigital.repositorio.RepositorioBase;

@Service
public class ServicoBase<E extends EntidadeBase, D extends Validavel> {
	@Autowired
	protected RepositorioBase<E> repositorio;
	
	@Autowired
	protected ModelMapper mapper;
	
	public D findById(int id) throws NotFoundException {
		E objeto = repositorio.findById(id)
				.orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum dado com as informações solicitadas."));
		return mapear(objeto);
	}
	
	public E salvar(E objeto) {
		return repositorio.save(objeto);
	}
	
	public E salvar(D dto) throws BadRequestException {
		List<String> erros = dto.validar();
		
		if (!erros.isEmpty())
			throw new BadRequestException(erros);			
		return repositorio.save(mapear(dto));
	}
	
	protected E mapear(D dto) {		
		TypeToken<E> tipoeEntidade = new TypeToken<E>(getClass()) {};
		return mapper.map(dto, 	tipoeEntidade.getType());
	}
	
	protected D mapear(E object) {
		TypeToken<D> tipoDTO = new TypeToken<D>(getClass()) {};
		return mapper.map(object, tipoDTO.getType());
	}
}
