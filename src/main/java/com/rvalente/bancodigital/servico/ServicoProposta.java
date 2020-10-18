package com.rvalente.bancodigital.servico;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvalente.bancodigital.dominio.ConstantesEmail;
import com.rvalente.bancodigital.dominio.Documento;
import com.rvalente.bancodigital.dominio.Proposta;
import com.rvalente.bancodigital.dominio.TipoDocumentoEnum;
import com.rvalente.bancodigital.dto.PropostaDTO;
import com.rvalente.bancodigital.dto.PropostaPrimeiroPassoDTO;
import com.rvalente.bancodigital.dto.PropostaSegundoPassoDTO;
import com.rvalente.bancodigital.excecoes.BadRequestException;
import com.rvalente.bancodigital.excecoes.NotFoundException;
import com.rvalente.bancodigital.excecoes.UnprocessableEntityException;
import com.rvalente.bancodigital.repositorio.RepositorioDocumento;
import com.rvalente.bancodigital.repositorio.RepositorioProposta;

@Service
public class ServicoProposta extends ServicoBase<Proposta, PropostaDTO> {
	@Autowired
	private RepositorioProposta repositorio;
	
	@Autowired
	private RepositorioDocumento repositorioDocumento;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${com.rvalente.bancodigital.amqp.conta.exchange-name}")
	private String EXCHANGE_NAME;
	
	public String registrarNovaProposta(PropostaPrimeiroPassoDTO dto) throws BadRequestException {		
		List<String> erros = dto.validar();
		
		dto.removerCaracteresEspeciaisCPF();
		if (repositorio.existsByCpf(dto.getCpf()))
			erros.add("Já existe uma proposta cadastrada para o cpf informado.");
		if (repositorio.existsByEmail(dto.getEmail()))
			erros.add("Já existe uma proposta cadastrada para o e-mail informado.");
		
		if (!erros.isEmpty())
			throw new BadRequestException(erros);		
		Proposta proposta = mapearPrimeiroPasso(dto);
		proposta.setConcluiuPrimeiroPasso(true);
		repositorio.save(proposta);
		
		return String.format("/api/proposta/cadastrar-endereco/%s", proposta.getId());
	}
	
	public String registrarEnderecoNovaProposta(int idProposta, PropostaSegundoPassoDTO dto) throws NotFoundException, BadRequestException {
		Proposta proposta = repositorio.findById(idProposta)
				.orElseThrow(() -> new NotFoundException("Não existe proposta cadastrada com os dados informados."));
		
		PropostaPrimeiroPassoDTO primeiroPasso = mapearParaPrimeiroPasso(proposta);
		
		List<String> erros = primeiroPasso.validar();
		erros.addAll(dto.validar());
		
		if (!erros.isEmpty())
			throw new BadRequestException(erros);		
		proposta = mapearSegundoPasso(dto, proposta);
		proposta.setConcluiuSegundoPasso(true);
		repositorio.save(proposta);
		
		return String.format("/api/proposta/enviar-documento/%s", proposta.getId());
	}
	
	public String realizarUploadCopiaCPF(int idProposta, MultipartFile arquivo) throws NotFoundException, BadRequestException, UnprocessableEntityException, IOException {
		Proposta proposta = repositorio.findById(idProposta)
				.orElseThrow(() -> new NotFoundException("Não existe proposta cadastrada com os dados informados."));
		if (!proposta.concluiuDoisPrimeirosPassos())
			throw new UnprocessableEntityException("É preciso concluir os passos anteriores antes de prosseguir.");
		if (arquivo.isEmpty())
			throw new BadRequestException(Arrays.asList("A cópia do CPF precisa ser fornecida antes de continuar."));
		
		Documento documento = Documento.builder().tipoDocumento(TipoDocumentoEnum.COPIA_CPF)
				.caminhoDocumento(storageService.salvarArquivo(arquivo, proposta.getCpf())).build();
		repositorioDocumento.save(documento);
		
		proposta.setCopiaCPF(documento);
		proposta.setConcluiuTerceiroPasso(true);
		repositorio.save(proposta);		
		
		return String.format("/api/proposta/revisar/%s", proposta.getId());
	}
	
	public PropostaDTO revisarProposta(int idProposta) throws NotFoundException, UnprocessableEntityException {
		Proposta proposta = repositorio.findById(idProposta)
				.orElseThrow(() -> new NotFoundException("Não existe proposta cadastrada com os dados informados."));
		if (!proposta.concluiuTodosOsPassos())
			throw new UnprocessableEntityException("É preciso concluir os passos anteriores antes de prosseguir.");		
		return mapear(proposta); 
	}
	
	public String processarAceiteDeProposta(int idProposta, boolean aceite) throws NotFoundException, UnprocessableEntityException, MessagingException, AmqpException, JsonProcessingException {
		Proposta proposta = repositorio.findById(idProposta)
				.orElseThrow(() -> new NotFoundException("Não existe proposta cadastrada com os dados informados."));
		if (!proposta.concluiuTodosOsPassos())
			throw new UnprocessableEntityException("É preciso concluir os passos anteriores antes de prosseguir.");
		String mensagem = aceite
				? "A proposta foi aceita com sucesso."
				: "A proposta foi recusada.";		
		
		proposta.setAceita(aceite);
		repositorio.save(proposta);
		
		if (aceite)
			tratarAceiteDeProposta(proposta);
		else
			tratarRejeicaoDeProposta(proposta);		
		return mensagem;
	}
	
	public Proposta findByCpf(String cpf) {
		return repositorio.findByCpf(cpf);
	}
	
	private void tratarAceiteDeProposta(Proposta proposta) throws MessagingException, AmqpException, JsonProcessingException {
		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("nome", proposta.getNome());
		emailService.enviarEmail(proposta.getEmail(), "Proposta Aceita", ConstantesEmail.ACEITE_PROPOSTA, parametros);
		
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", new ObjectMapper().writeValueAsString(mapear(proposta)));
	}
	
	private void tratarRejeicaoDeProposta(Proposta proposta) throws MessagingException {
		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("nome", proposta.getNome());
		emailService.enviarEmail(proposta.getEmail(), "Proposta Recusada", ConstantesEmail.NAO_ACEITE_PROPOSTA, parametros);		
	}
	
	private Proposta mapearPrimeiroPasso(PropostaPrimeiroPassoDTO dto) {
		Proposta proposta = mapper.map(dto, Proposta.class);
		proposta.setConcluiuPrimeiroPasso(true);
		
		return proposta;
	}
	
	private PropostaPrimeiroPassoDTO mapearParaPrimeiroPasso(Proposta proposta) {
		return mapper.map(proposta, PropostaPrimeiroPassoDTO.class);
	}
	
	private Proposta mapearSegundoPasso(PropostaSegundoPassoDTO dto, Proposta proposta) {
		mapper.map(dto, proposta);		
		return proposta;
	}
}
