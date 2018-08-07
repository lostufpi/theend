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
import br.com.ufpi.systematicmap.model.enums.AcceptanceType;
import br.com.ufpi.systematicmap.model.enums.TipoMensagem;
import br.com.ufpi.systematicmap.model.enums.TypeOfFile;

public class FileGenerator {

	private String fileName;
	private AcceptanceType acceptanceType;
	private TypeOfFile typeOfFile;
	private MapStudy mapStudy;
	private ArticleDao articleDao;
	private User user;
	private Logger logger;

	public FileGenerator(String fileName, AcceptanceType acceptanceType, TypeOfFile typeOfFile, MapStudy mapStudy,
			ArticleDao articleDao2, User user, Logger logger) {
		this.user = user;
		this.fileName = fileName;
		this.acceptanceType=acceptanceType;
		this.typeOfFile=typeOfFile;
		this.mapStudy = mapStudy;
		this.articleDao = articleDao2;
		this.logger=logger;
	}

	public File getFinalFile() {
 		if (acceptanceType.equals(AcceptanceType.MY_ACCEPTACES))
 			return myAcceptances(logger);
		else if (acceptanceType.equals(AcceptanceType.ALL_ACCEPTANCES))
			return allAcceptances(logger);
		else
			return null;
	}

	private File allAcceptances(Logger logguer) {
		List<Article> articles = articleDao.getArticlesFinalAccepted(mapStudy);
		if (articles.isEmpty()) {
			MessagesController.addMessage(
					new Mensagem("mapstudy.articles", "mapstudy.articles.accepted.all.none", TipoMensagem.ERRO));
			return null;
		} else {
			try {
				return finalGenerator(articles,logguer);
			} catch (IOException e) {
				logguer.error(e.getMessage());
			}
			return null;
		}

	}

	private File myAcceptances(Logger logguer) {
		List<Article> articles = articleDao.getArticlesEvaluated(user, mapStudy);
		if (articles.isEmpty()) {
			MessagesController.addMessage(
					new Mensagem("mapstudy.articles", "mapstudy.articles.evaluated.none", TipoMensagem.ERRO));
			return null;
		} else {
			try {
				return finalGenerator(articles, logguer);
			} catch (IOException ioException) {
				logguer.error(ioException.getMessage());
			}
		}
		return null;
	}

	public File finalGenerator(List<Article> articles, Logger logguer) throws IOException {
		if (typeOfFile.equals(TypeOfFile.XLS))
			return XLSBuilder.generateFile(articles, fileName, logguer);
		else if (typeOfFile.equals(TypeOfFile.CSV))
			return CSVBuilder.generateFile(articles, fileName);
		return null;
	}
}
