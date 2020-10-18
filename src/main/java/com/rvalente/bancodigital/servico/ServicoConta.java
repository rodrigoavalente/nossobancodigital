package com.rvalente.bancodigital.servico;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rvalente.bancodigital.dominio.ConstantesEmail;
import com.rvalente.bancodigital.dominio.Conta;
import com.rvalente.bancodigital.dominio.Pessoa;
import com.rvalente.bancodigital.dto.PropostaDTO;
import com.rvalente.bancodigital.interfaces.Validavel;
import com.rvalente.bancodigital.repositorio.RepositorioConta;

@Service
public class ServicoConta extends ServicoBase<Conta, Validavel> {
	@Autowired
	private RepositorioConta repositorio;
	
	@Autowired
	private ServicoPessoa servicoPessoa;
	
	@Autowired
	private  ServicoProposta servicoProposta;
	
	@Autowired
	private EmailService emailService;
	
	private final static int CODIGO_BANCO = 555;
	
	public Conta gerarNovaConta(PropostaDTO proposta) {
		Pessoa pessoa = servicoPessoa.buildDeProposta(proposta);
		
		servicoPessoa.salvar(pessoa);	
		Conta conta = Conta.builder().agencia(gerarCodigoAgencia()).codigoBanco(CODIGO_BANCO).conta(gerarNumeroConta())
				.correntista(pessoa).propostaOrigem(servicoProposta.findByCpf(pessoa.getCpf())).saldo(BigDecimal.ZERO)
				.build();			
		repositorio.save(conta);
		enviarEmailNovaConta(conta);		
		return conta;
	}
	
	public Conta findByAgenciaAndConta(int agencia, int conta) {
		return repositorio.findByAgenciaAndConta(agencia, conta);
	}
	
	private int gerarCodigoAgencia() {
		return ThreadLocalRandom.current().nextInt(1000, 10000);
	}
	
	private int gerarNumeroConta() {
		return ThreadLocalRandom.current().nextInt(10000000, 100000000);
	}
	
	private void enviarEmailNovaConta(Conta conta) {
		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("nome", conta.getCorrentista().getNome());
		parametros.put("banco", conta.getCodigoBanco());
		parametros.put("agencia", conta.getAgencia());
		parametros.put("conta", conta.getConta());
		
		try {
			emailService.enviarEmail(conta.getCorrentista().getEmail(), "As Informações da sua Nova Conta", ConstantesEmail.CONTA_CADASTRADA, parametros);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
