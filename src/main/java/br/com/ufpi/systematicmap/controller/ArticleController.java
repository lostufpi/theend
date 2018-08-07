/**
 * 
 */
package br.com.ufpi.systematicmap.controller;

import java.io.Serializable;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.dao.MapStudyDao;
import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.TypeMessage;

/**
 * @author Gleison Andrade
 *
 */
@Controller
public class ArticleController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserInfo userInfo;
	private MapStudyDao mapStudyDao;
	private ArticleDao articleDao;
	private Result result;
	private Validator validator;
	private final Logger logger;

	/**
	 * @deprecated CDI eyes only
	 */
	protected ArticleController() {
		this(null, null, null, null, null, null);
	}

	@Inject
	public ArticleController(MapStudyDao musicDao, UserInfo userInfo, Result result, Validator validator, ArticleDao articleDao, Logger logger) {
		this.mapStudyDao = musicDao;
		this.result = result;
		this.validator = validator;
		this.userInfo = userInfo;
		this.articleDao = articleDao;
		this.logger = logger;
	}
	
	@Get("/map/{mapid}/article/{id}")
	public void show(Long mapid, Long id) {
		validator.onErrorForwardTo(MapStudyController.class).identification(mapid);
		
		MapStudy mapStudy = mapStudyDao.find(mapid);
		User user = userInfo.getUser();

		if (mapStudy == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.exist", TypeMessage.SUCCESS));
			result.redirectTo(MapStudyController.class).list();
			return;
		}

		if (!mapStudy.members().contains(user)) {
			MessagesController.addMessage(new Mensagem("user", "user.does.not.have.access", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}
		
		Article article = articleDao.find(id);
		
		if(article == null) {
			MessagesController.addMessage(new Mensagem("article", "article.is.not.exist", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).identification(mapid);
			return;
		}
		
		if(!article.getMapStudy().getId().equals(mapStudy.getId())) {
			MessagesController
			.addMessage(new Mensagem("article", "article.is.not.part.mapping", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).identification(mapid);
		}
		
		MessagesController.addMessage(new Mensagem("mapstudy", "unrefine.articles.sucess", TypeMessage.SUCCESS));
		result.include("article", article);
		result.include("map", mapStudy);
	}

}
