package com.rvalente.bancodigital.utilidades;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.validator.routines.EmailValidator;

public class UtilitarioValidacao {
	public static void validarRequerido(Object objeto, String campo, List<String> erros) {
		if (Objects.isNull(objeto))
			erros.add(String.format("Não foi informado um valor para o campo '%s'.", campo));
	}
	
	public static void validarRequerido(String objeto, String campo, List<String> erros) {
		if (Objects.isNull(objeto) || objeto.trim().length() == 0)
			erros.add(String.format("Não foi informado um valor para o campo '%s'.", campo));
	}
	
	public static void validarEmail(String email, List<String> erros) {
		EmailValidator validador = EmailValidator.getInstance();		
		if (!validador.isValid(email))
			erros.add("O email fornecido não é válido.");
	}
	
	public static void validarDataAnteriorDataAtual(Date data, String campo, List<String> erros) {		
		if (Objects.nonNull(data) && data.after((new Date())))
			erros.add(String.format("A '%s' deve ser anterior a data de hoje.", campo));
	}
	
	public static void validarMaioridade(Date data, List<String> erros) {
		if (Objects.nonNull(data)) {
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(data);
			
			Long anos = ChronoUnit.YEARS.between(LocalDate.of(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH + 1),
					calendario.get(Calendar.DAY_OF_MONTH)), LocalDate.now());			
			if (anos < 18)
				erros.add("É preciso ser maior de idade para prosseguir.");
		}
	}
	
	public static void validarCEP(String cep, List<String> erros) {
		if (Objects.nonNull(cep)) {
			String cepLimpo = cep.replaceAll("[\\D]+", "");
			
			try {
				if (cepLimpo.length() != 8)
					erros.add("O cep informado não é válido, a quantidade de caracteres é diferente do esperado.");
				
				Long numeros = Long.parseLong(cepLimpo);
				
				if (numeros == 0)
					erros.add("O cep informado não é válido.");
			} catch (NumberFormatException e) {
				erros.add("O cep deve conter apenas números.");
			}
		}
	}
	
	public static void validarCPF(String cpf, List<String> erros) {
		String cpfLimpo = cpf.replaceAll("[\\d]+", "");
		
		try {
			if (cpf.length() < 11)
				erros.add("O cpf informado não é válido, ele não contêm todos os números.");
			else if (!cpfContemFormatoValido(cpfLimpo))
				erros.add("O cpf informado não é válido, por favor verifique e tente novamente.");
		} catch (NumberFormatException e) {
			erros.add("O cpf deve conter apenas números.");
		}		
	}
	
	private static boolean cpfContemFormatoValido(String cpfLimpo) throws NumberFormatException {
		Long.parseLong(cpfLimpo);
		
		int soma = 0;
		
		for (int i = 0; i < 9; i++)
			soma += (10 - i) * (cpfLimpo.charAt(i) - '0');
		soma = 11 - (soma % 11);
		
		if (soma > 9)
			soma = 0;
		if (soma == (cpfLimpo.charAt(9) - '0')) {
			soma = 0;
			
			for (int i = 0; i < 10; i++)
				soma += (11 - 1) * (cpfLimpo.charAt(i) - '0');
			soma = 11 - (soma % 11);
			if (soma > 9)
				soma = 0;			
			if (soma == (cpfLimpo.charAt(10) - '0'))
				return true;
		}
		
		return false;
	}
}
