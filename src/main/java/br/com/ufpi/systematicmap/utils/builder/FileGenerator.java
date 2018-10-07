package br.com.ufpi.systematicmap.utils.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;

import br.com.ufpi.systematicmap.controller.MessagesController;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.DownloadArticleType;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;
import br.com.ufpi.systematicmap.model.enums.TypeMessage;
import br.com.ufpi.systematicmap.model.enums.TypeOfFile;

public class FileGenerator {
	private String fileName;
	private DownloadArticleType typeOfDown;
	private TypeOfFile typeOfFile;
	private MapStudy mapStudy;
	private ArticleDao articleDao;
	private User user;
	private Logger logger;

	public FileGenerator(String fileName, DownloadArticleType typeOfDown, TypeOfFile typeOfFile, MapStudy mapStudy,
			ArticleDao articleDao2, User user, Logger logger) {
		this.user = user;
		this.fileName = fileName;
		this.typeOfDown = typeOfDown;
		this.typeOfFile = typeOfFile;
		this.mapStudy = mapStudy;
		this.articleDao = articleDao2;
		this.logger = logger;
	}

	public File getFinalFile() {
		List<Article> articles = articlesList();
				
		if (articles.isEmpty()) {
			MessagesController.addMessage(new Mensagem("mapstudy.articles", "mapstudy.articles.list.none", TypeMessage.ERROR));
			return null;
		} else {
			try {
				return finalGenerator(articles);
			} catch (IOException ioException) {
				logger.error(ioException.getMessage());
			}
		}
		return null;
	}

	private List<Article> articlesList() {
		List<Article> articles = null;

		if (typeOfDown.equals(DownloadArticleType.MY_ACCEPTACES)) {
			articles = articleDao.getArticlesEvaluated(user, mapStudy, EvaluationStatusEnum.ACCEPTED);
		} else if (typeOfDown.equals(DownloadArticleType.MY_REJECTED)) {
			articles = articleDao.getArticlesEvaluated(user, mapStudy, EvaluationStatusEnum.REJECTED);
		} else if (typeOfDown.equals(DownloadArticleType.MY_ALL)) {
			articles = articleDao.getArticlesEvaluated(user, mapStudy);
		} else if (typeOfDown.equals(DownloadArticleType.ALL_ACCEPTANCES)) {
			articles = articleDao.getArticlesFinalEvaluate(mapStudy, EvaluationStatusEnum.ACCEPTED);
		} else if (typeOfDown.equals(DownloadArticleType.ALL_REJECTED)) {
			articles = articleDao.getArticlesFinalEvaluate(mapStudy, EvaluationStatusEnum.REJECTED);
		} else if (typeOfDown.equals(DownloadArticleType.ALL_EVALUATED)) {
			articles = articleDao.getArticlesFinalEvaluate(mapStudy);
		} else if (typeOfDown.equals(DownloadArticleType.ALL)) {
			articles = articleDao.getArticles(mapStudy);
		}

		return articles;
	}

	public File finalGenerator(List<Article> articles) throws IOException {
		if (typeOfFile.equals(TypeOfFile.XLS))
			return XLSBuilder.generateFile(articles, fileName, logger, isDownloadMy());
		else if (typeOfFile.equals(TypeOfFile.CSV))
			return CSVBuilder.generateFile(articles, fileName, isDownloadMy());
		return null;
	}
	
	public User isDownloadMy(){
		if(typeOfDown.equals(DownloadArticleType.MY_ACCEPTACES) || typeOfDown.equals(DownloadArticleType.MY_REJECTED) || typeOfDown.equals(DownloadArticleType.MY_ALL)){
			return user;
		}
		
		return null;
	}
}
