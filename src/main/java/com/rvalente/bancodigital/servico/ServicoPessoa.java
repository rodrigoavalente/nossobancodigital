package com.rvalente.bancodigital.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rvalente.bancodigital.dominio.Pessoa;
import com.rvalente.bancodigital.dominio.Proposta;
import com.rvalente.bancodigital.dto.PropostaDTO;
import com.rvalente.bancodigital.interfaces.Validavel;
import com.rvalente.bancodigital.repositorio.RepositorioPessoa;

@Service
public class ServicoPessoa extends ServicoBase<Pessoa, Validavel> {
	@Autowired
	private RepositorioPessoa repositorio;
	
	public Pessoa buildDeProposta(Proposta proposta) {
		return Pessoa.builder().nome(proposta.getNome()).sobrenome(proposta.getSobrenome()).email(proposta.getEmail())
				.dataNascimento(proposta.getDataNascimento()).cpf(proposta.getCpf()).cep(proposta.getCep())
				.rua(proposta.getRua()).bairro(proposta.getBairro()).complemento(proposta.getComplemento())
				.cidade(proposta.getCidade()).estado(proposta.getEstado()).build();
	}
	
	public Pessoa buildDeProposta(PropostaDTO proposta) {
		return Pessoa.builder().nome(proposta.getNome()).sobrenome(proposta.getSobrenome()).email(proposta.getEmail())
				.dataNascimento(proposta.getDataNascimento()).cpf(proposta.getCpf()).cep(proposta.getCep())
				.rua(proposta.getRua()).bairro(proposta.getBairro()).complemento(proposta.getComplemento())
				.cidade(proposta.getCidade()).estado(proposta.getEstado()).build();
	}
}
