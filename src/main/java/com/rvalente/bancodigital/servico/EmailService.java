package com.rvalente.bancodigital.servico;

import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine emailTemplateEngine;
	
	@Value("${com.rvalente.bancodigital.mail.remetente}")
	private String remetente;
	
	public void enviarEmail(String destinatario, String assunto, String template, Map<String, Object> parametros) throws MessagingException {
		final Context ctx = new Context(new Locale("pt_br"));
		ctx.setVariables(parametros);
		
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		
		message.setSubject(assunto);
		message.setFrom(remetente);
		message.setTo(destinatario);
		
		String conteudo = emailTemplateEngine.process(template, ctx);
		message.setText(conteudo, true);
		
		mailSender.send(mimeMessage);
	}
}
