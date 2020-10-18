package com.rvalente.bancodigital.propriedades;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "document")
@Getter @Setter
public class DocumentStorageProperty {
	private String uploadDir;	
}
