package br.com.ufpi.systematicmap.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.com.ufpi.systematicmap.model.Mensagem;

@Named("msg") @SessionScoped
public class Messages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ArrayList<Mensagem> message = new ArrayList<Mensagem>();
	

	public static void addMessage(Mensagem msg) {
		message.add(msg);
	}

	public static ArrayList<Mensagem> getMessage() {
		return message;
	}
	
	public static void clean() {
		message.clear();
	}

}
