package br.com.ufpi.systematicmap.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class GenerateHashPasswordUtil {
	public String generateHash(String password) {
//		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		String encodePassword = passwordEncoder().encode(password);
		return encodePassword;
	}
//
//	private MessageDigestPasswordEncoder getInstanceMessageDisterPassword() {
//		// informo tipo de enconding que desejo
//		MessageDigestPasswordEncoder digestPasswordEncoder = new MessageDigestPasswordEncoder("MD5");
//		return digestPasswordEncoder;
//	}
//
//	// método que faz a validação  como não usamos salt deixei em null
	public boolean isPasswordValid(String password, String hashPassword) {
//		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		return passwordEncoder().matches(password, hashPassword);
	}
	
	public boolean isDefaultEncoder(String password){
		return password.contains(DefaultPasswordEncoderFactories.encoderDefault);
	}
//	
//	//
	public String generateCodeRecovery(String code){
		return generateHash(code);
	}
	
	public PasswordEncoder passwordEncoder() {
	    return DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
}