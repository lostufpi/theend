package br.com.ufpi.systematicmap.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class DefaultPasswordEncoderFactories {
	public static final String encoderDefault = "bcrypt";

    @SuppressWarnings("deprecation")
    static PasswordEncoder createDelegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encoderDefault, new BCryptPasswordEncoder());
//        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
//        encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
//        encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
//        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
//        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
//        encoders.put("scrypt", new SCryptPasswordEncoder());
//        encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
//        encoders.put("SHA-256", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encoderDefault, encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
        return delegatingPasswordEncoder;
    }

}