package com.rvalente.bancodigital.servico;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rvalente.bancodigital.propriedades.DocumentStorageProperty;

@Service
public class StorageService {
	@Autowired
	private DocumentStorageProperty documentStorageProperty;
	
	private Path localizacaoDocumentos;
	
	@PostConstruct
	private void init() {
		this.localizacaoDocumentos = Paths.get(documentStorageProperty.getUploadDir()).toAbsolutePath().normalize();
	}
	
	public String salvarArquivo(MultipartFile arquivo, String caminho) throws IOException {
		Path localizacao = localizacaoDocumentos.resolve(Paths.get(caminho, arquivo.getOriginalFilename()));
		Files.createDirectories(localizacao.getParent());		
		Files.copy(arquivo.getInputStream(), localizacao, StandardCopyOption.REPLACE_EXISTING);
		
		return localizacao.toString();
	}
}
