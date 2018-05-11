package br.com.ufpi.systematicmap.utils;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.ufpi.systematicmap.components.FilterArticles;
import br.com.ufpi.systematicmap.controller.Messages;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.enums.TipoMensagem;

@Dependent
public class MapStudyFilterArticleThread extends Thread implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FilterArticles filter;

	@Override
	public void run() {
		filter.filter();
		System.out.println("Thread Concluida!!!!");
		Messages.addMessage(new Mensagem("filter.end.tittle","filter.end.message", TipoMensagem.SUCESSO));
	}
	public FilterArticles getFilter() {
		return filter;
	}

	public void setFilter(FilterArticles filter) {
		this.filter = filter;
	}
	
}
