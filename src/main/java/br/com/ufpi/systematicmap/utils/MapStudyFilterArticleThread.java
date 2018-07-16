package br.com.ufpi.systematicmap.utils;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import br.com.ufpi.systematicmap.components.FilterArticles;
import br.com.ufpi.systematicmap.controller.MessagesController;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.enums.TipoMensagem;

@Dependent
public class MapStudyFilterArticleThread extends Thread implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SessionScoped private ArticleDao articleDao;

	private MapStudy mapStudy;

	
	/**
     * @deprecated CDI eyes only
     */
    protected MapStudyFilterArticleThread() {
        this(null);
    }

    @Inject
    public MapStudyFilterArticleThread(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }
	
	@Override
	public void run() {
//		MessagesController.changeRunner(true);
//		(new FilterArticles()).filter(mapStudy, articleDao);
//		MessagesController.addMessage(
//				new Mensagem("mapstudy.filter.end.tittle", "mapstudy.filter.end.message", TipoMensagem.SUCESSO));
//		MessagesController.changeRunner(false);
	}

	public void filterParams(MapStudy mapStudy) {
		this.mapStudy = mapStudy;		
	}

	/**
	 * @return the mapStudy
	 */
	public MapStudy getMapStudy() {
		return mapStudy;
	}

	/**
	 * @param mapStudy the mapStudy to set
	 */
	public void setMapStudy(MapStudy mapStudy) {
		this.mapStudy = mapStudy;
	}
}