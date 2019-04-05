/**
 * 
 */
package br.com.ufpi.systematicmap.controller;

import static br.com.caelum.vraptor.view.Results.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.io.output.FileWriterWithEncoding;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.FileDownload;
import br.com.caelum.vraptor.view.Results;
import br.com.ufpi.systematicmap.dao.AlternativeDao;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.dao.EvaluationExtractionFinalDao;
import br.com.ufpi.systematicmap.dao.EvaluationExtrationDao;
import br.com.ufpi.systematicmap.dao.FormDao;
import br.com.ufpi.systematicmap.dao.MapStudyDao;
import br.com.ufpi.systematicmap.dao.QuestionDao;
import br.com.ufpi.systematicmap.dao.UserDao;
import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.model.Alternative;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.EvaluationExtraction;
import br.com.ufpi.systematicmap.model.EvaluationExtractionFinal;
import br.com.ufpi.systematicmap.model.Form;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.Question;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;
import br.com.ufpi.systematicmap.model.enums.QuestionType;
import br.com.ufpi.systematicmap.model.enums.ReturnStatusEnum;
import br.com.ufpi.systematicmap.model.enums.Roles;
import br.com.ufpi.systematicmap.model.enums.TypeMessage;
import br.com.ufpi.systematicmap.model.vo.ExtractionCompareVO;
import br.com.ufpi.systematicmap.model.vo.ExtractionFinalVO;
import br.com.ufpi.systematicmap.model.vo.QuestionAndAlternativeCSV;
import br.com.ufpi.systematicmap.model.vo.QuestionVO;
import br.com.ufpi.systematicmap.model.vo.ReturnVO;
import br.com.ufpi.systematicmap.utils.UserUtil;

/**
 * @author Gleison Andrade
 *
 */
@Controller
public class ExtractionController {
	private UserInfo userInfo;
	private Result result;
	private MapStudyDao mapStudyDao;
	private ArticleDao articleDao;
	private AlternativeDao alternativeDao;
	private QuestionDao questionDao;
	private EvaluationExtrationDao evaluationExtrationDao;
	private EvaluationExtractionFinalDao evaluationExtractionFinalDao;
	private UserDao userDao;

	protected ExtractionController() {
		this(null, null, null, null, null, null, null, null, null, null);
	}

	@Inject
	public ExtractionController(UserInfo userInfo, Result result, MapStudyDao mapStudyDao, FormDao formDao,
			ArticleDao articleDao, AlternativeDao alternativeDao, QuestionDao questionDao,
			EvaluationExtrationDao evaluationExtrationDao, EvaluationExtractionFinalDao evaluationExtractionFinalDao,
			UserDao userDao) {
		this.userInfo = userInfo;
		this.result = result;
		this.mapStudyDao = mapStudyDao;
		this.articleDao = articleDao;
		this.alternativeDao = alternativeDao;
		this.questionDao = questionDao;
		this.evaluationExtrationDao = evaluationExtrationDao;
		this.evaluationExtractionFinalDao = evaluationExtractionFinalDao;
		this.userDao = userDao;
	}

	@Post
	@Path("/extraction/form")
	@Consumes("application/json")
	public void formAjax(QuestionVO questionVO) {
		Form form = new Form();
		MapStudy mapStudy = mapStudyDao.find(questionVO.getMapid());
		Set<Question> backQuestions = new HashSet<>();
		Set<Question> removeQuestions = new HashSet<>();

		// se o mapeamento possui um formulário
		if (mapStudy.getForm() != null) {
			backQuestions = mapStudy.getForm().getQuestions(); // backup das questões atuais do formulário
			removeQuestions = new HashSet<>(); // lista de questões que serão removidas

			// adicionando questões a lista das que serão removidas
			for (Question q : backQuestions) { // percorre a lista atual de questões
				if (!questionVO.getQuestions().contains(q)) { // aquelas que estiverem na lista mas não estão na lista
																// que veio da visão serão adicionadas a lista de
																// removidas
					removeQuestions.add(q); // adiciona a lista de questões removidas
				}
			}

			// mapStudy.getForm().getQuestions().removeAll(removeQuestions);
		}

		form.addQuestionVO(questionVO);
		mapStudy.addForm(form);

		for (Question q : removeQuestions) {
			for (Alternative a : q.getAlternatives()) {
				alternativeDao.delete(a); // remove as alternativas das questões que foram excluidas
				// evaluationExtrationDao.removeAlternative(a);
			}
			q.getAlternatives().clear(); // limpa a lista de alternativas da questão
			questionDao.delete(q); // deleta a questão
			evaluationExtrationDao.removeQuestion(q); // remove todas as extrações que tem a questão
			mapStudy.getForm().getQuestions().remove(q); // remove a questão do formulário
		}

		mapStudyDao.update(mapStudy); // atualiza o mapeamento

		result.use(json()).indented().withoutRoot().from(mapStudy).recursive().serialize();
	}

	@Post
	@Consumes("application/json")
	public void loadQuestions(Long mapid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		Set<Question> questions = new HashSet<>();

		if (mapStudy != null && mapStudy.getForm() != null) {
			questions = mapStudy.getForm().getQuestions();
		}

		result.use(json()).indented().withoutRoot().from(questions).recursive().serialize();
	}

	@Post
	@Consumes("application/json")
	public void removeQuestion(Long mapid, Long questionId) {

		MapStudy mapStudy = mapStudyDao.find(mapid);
		Question question = questionDao.find(questionId);

		boolean containsQuestion = false;
		for (Question q : mapStudy.getForm().getQuestions()) {
			if (question.getId().equals(q.getId())) {
				containsQuestion = true;
				break;
			}
		}

		ReturnVO retorno;
		if (containsQuestion) {
			for (Alternative a : question.getAlternatives()) {
				alternativeDao.delete(a);
			}
			question.getAlternatives().clear();
			evaluationExtrationDao.removeQuestion(question);
			mapStudy.getForm().getQuestions().remove(question);
			questionDao.delete(question);
			mapStudyDao.update(mapStudy);

			retorno = new ReturnVO(ReturnStatusEnum.SUCESSO, "");
		} else {
			retorno = new ReturnVO(ReturnStatusEnum.ERRO, "Ops, não foi possível concluir. Tente novamente.");
		}

		result.use(json()).indented().withoutRoot().from(retorno).recursive().serialize();
	}

	@Post
	@Consumes("application/json")
	public void addQuestion(Long mapid, Question question) {
		Form form = new Form();
		MapStudy mapStudy = mapStudyDao.find(mapid);
		Question questionEquals = null;

		// se já existir form no mapeamento
		if (mapStudy.getForm() != null && question.getId() != null) {
			Set<Question> questions = new HashSet<Question>(mapStudy.getForm().getQuestions());
			for (Question questionActual : questions) {
				// verifico se já existe algum com o id passado
				if (question.getId().equals(questionActual.getId())) {
					if (question.getType().equals(QuestionType.SIMPLE)
							&& !questionActual.getType().equals(QuestionType.SIMPLE)) {
						// remove todas as questões
						for (Alternative altActual : questionActual.getAlternatives()) {
							altActual.setQuestion(null);
						}
						questionActual.getAlternatives().clear();
					} else {
						for (Alternative altActual : questionActual.getAlternatives()) {
							boolean remove = true;
							// Objetivo aqui é remover as alternativas que não existem mais
							for (Alternative newAlt : question.getAlternatives()) {
								if (altActual.getValue().equals(newAlt.getValue())
										|| altActual.getId().equals(newAlt.getId())) {
									newAlt.setId(altActual.getId());
									// a2.setQuestion(a.getQuestion());
									remove = false;
									break;
								}
							}

							if (remove) {
								altActual.setQuestion(null);
								alternativeDao.delete(altActual);
							}
						}
					}

					questionEquals = questionActual;

				}
			}
		}

		if (questionEquals != null) {
			questionEquals.setName(question.getName());
			questionEquals.setType(question.getType());
			questionEquals.getAlternatives().clear();

			for (Alternative newAlt : question.getAlternatives()) {
				if (!newAlt.getValue().equals("")) {
					questionEquals.addAlternative(newAlt);
				} else {
					questionEquals.removeAlternative(newAlt);
				}
			}

			questionDao.update(questionEquals);
		} else {
			question.setId(null);

			for (Alternative a : question.getAlternatives()) {
				if (!a.getValue().equals("")) {
					a.setQuestion(question);
				} else {
					question.removeAlternative(a);
				}
			}

			form.addQuestion(question);
			mapStudy.addForm(form);
		}

		mapStudyDao.update(mapStudy);
		result.use(json()).indented().withoutRoot().from(mapStudy).recursive().serialize();
	}

	@Post
	@Consumes("application/json")
	public void getQuestion(Long mapid, Long questionId) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		Question question = questionDao.find(questionId);

		boolean containsQuestion = false;

		for (Question q : mapStudy.getForm().getQuestions()) {
			if (question.getId().equals(q.getId())) {
				containsQuestion = true;
				break;
			}
		}

		ReturnVO retorno;
		if (containsQuestion) {
			retorno = new ReturnVO(ReturnStatusEnum.SUCESSO, "");
			retorno.setData(question);
		} else {
			retorno = new ReturnVO(ReturnStatusEnum.ERRO, "Ops, não foi possível concluir. Tente novamente.");
		}

		result.use(json()).indented().withoutRoot().from(retorno).recursive().serialize();
	}

	@Get("/maps/extraction/{mapid}")
	public void extraction(Long mapid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		User user = userInfo.getUser();
		if (mapStudy == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.exist", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}
		if (!mapStudy.members().contains(user)) {
			MessagesController.addMessage(new Mensagem("user", "user.does.not.have.access", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}

		if (mapStudy.getForm() == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.form", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).show(mapid);
			return;
		}

		if (mapStudy.getForm().getQuestions() == null || mapStudy.getForm().getQuestions().size() <= 0) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.form", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).show(mapid);
			return;
		}

		Double percentExtractedDouble = mapStudy.percentExtractedDouble(articleDao, userInfo.getUser()); // TODO da um
																											// update
																											// nele
																											// depois,
																											// verificar
																											// se falta
																											// questão

		if (!percentExtractedDouble.equals((Double) 100.0)) {
			Long countArticlesToExtraction = articleDao.countArticleToEvaluateExtraction(user, mapStudy);
			// se existe artigos para realizar extração entra
			if (countArticlesToExtraction <= 0l) {
				MessagesController.addMessage(
						new Mensagem("mapstudy", "mapstudy.is.not.article.to.extraction", TypeMessage.ERROR));
				result.redirectTo(MapStudyController.class).show(mapid);
				return;
			}

		}
		
		result.redirectTo(this).evaluateExtraction(mapid, 0l);
	}

	@Get("/maps/extraction/{mapid}/article/{articleid}")
	public void evaluateExtraction(Long mapid, Long articleid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);

		if (mapStudy == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.exist", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}

		List<Article> articlesToExtraction = articleDao.getArticlesToExtraction(userInfo.getUser(), mapStudy);
		// FIXME
		List<Article> extractions = articleDao.getExtractions(userInfo.getUser(), mapStudy);

		Article article = null, nextArticle = null;
		Long nextArticleId = null;

		if (articleid != 0) {
			article = articleDao.find(articleid);
		} else {
			article = getNextToEvaluate(articlesToExtraction, null);
			if (article != null) {
				articleid = article.getId();
			}
		}

		if (article != null) {
			nextArticle = getNextToEvaluate(articlesToExtraction, articleid);
			if (nextArticle != null) {
				nextArticleId = nextArticle.getId();
			}
		}

		if (article == null) {
			article = extractions.get(0);
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.extraction.articles.none", TypeMessage.INFORMATION));
		}

		Double percentExtractedDouble = mapStudy.percentExtractedDouble(articleDao, userInfo.getUser()); 

		result.include("map", mapStudy);
		result.include("article", article);
		result.include("nextArticleId", nextArticleId);
		result.include("articlesToExtraction", articlesToExtraction); // artigos a serem avaliados
		result.include("percentExtracted", mapStudy.percentEvaluated(percentExtractedDouble));
		result.include("extractions", extractions); // artigos já avaliados
		result.include("form", mapStudy.getForm());

	}

	private static Article getNextToEvaluate(List<Article> articlesToExtraction, Long actual) {
		if (actual == null) {
			return articlesToExtraction.size() > 0 ? articlesToExtraction.get(0) : null;
		} else {
			Article next = null;
			for (Article a : articlesToExtraction) {
				if (!a.getId().equals(actual)) {
					next = a;
					break;
				}
			}
			return next;
		}
	}

	@Post("/maps/extraction")
	@Consumes("application/json")
	public void evaluateAjax(QuestionVO questionVO) {

		User user = userInfo.getUser();
		Article article = articleDao.find(questionVO.getArticleid());
		MapStudy mapStudy = mapStudyDao.find(questionVO.getMapid());

		if (article == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.extraction.articles.none", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).show(questionVO.getMapid());
			return;
		}

		int numberQuestions = questionVO.getQuestions().size();

//		article.addComments(userInfo.getUser(), questionVO.getComment());

		for (int i = 0; i < numberQuestions; i++) {
			Set<Alternative> auxList = questionVO.getQuestions().get(i).getAlternatives();

			for (Alternative alternative : auxList) {
				EvaluationExtraction evaluationExtraction = new EvaluationExtraction();

				if (alternative.getId() == null) {
					Alternative aux = alternativeDao.find(questionVO.getQuestions().get(i).getId(),
							alternative.getValue());
					if (aux == null) {
						alternativeDao.insert(alternative);
						Question question = questionMapStudy(mapStudy, questionVO.getQuestions().get(i).getId());
						question.addAlternative(alternative);
					} else {
						alternative = aux;
					}
				} else {
					if (alternative.getQuestion() == null) {
						Question question = questionMapStudy(mapStudy, questionVO.getQuestions().get(i).getId());
						alternative.setQuestion(question);
					}
				}

				evaluationExtraction.setAlternative(alternative);
				evaluationExtraction.setArticle(article);
				evaluationExtraction.setUser(user);
				evaluationExtraction.setQuestion(alternative.getQuestion());
				evaluationExtraction.setComment(questionVO.getComment());

				// TODO ao adicionar seria melhor verificar aqui ? se a alternativa alternativa
				// então deveria só atualizar
				article.AddEvaluationExtractions(evaluationExtraction);
			}
			// Alternative alternative = auxList.iterator().next(); //TODO pode ter mais de
			// uma

		}


		Double percentExtractedDouble = mapStudy.percentExtractedDouble(articleDao, userInfo.getUser());
		HashMap<String, Object> returns = new HashMap<>();
		Article nextArticleL = null;
		List<EvaluationExtraction> extraction = new ArrayList<>();

		if (questionVO.getNextArticle() != null) {
			nextArticleL = articleDao.find(questionVO.getNextArticle());
			extraction = nextArticleL.getEvaluationExtraction(userInfo.getUser());
		} else {
			nextArticleL = new Article();
			nextArticleL.setId(-1l);
		}

		articleDao.update(article);

		returns.put("extraction", extraction);
		returns.put("article", nextArticleL);
		returns.put("percent", mapStudy.percentEvaluated(percentExtractedDouble));

		result.use(Results.json()).indented().withoutRoot().from(returns).recursive().serialize();
	}

	@Get
	@Path("/extraction/article/{articleid}/load")
	public void loadArticleAjax(Long mapid, Long articleid) {
		Article article = articleDao.find(articleid);

		// se o artigo não existir
		if (article == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.evaluate.articles.none", TypeMessage.INFORMATION));
			result.redirectTo(MapStudyController.class).show(mapid);
			return;
		}

		HashMap<String, Object> returns = new HashMap<>();
		List<EvaluationExtraction> extraction = null;

		extraction = article.getEvaluationExtraction(userInfo.getUser());
		
//		List<EvaluationExtraction> extractionOrdered = new ArrayList<EvaluationExtraction>();
//		extractionOrdered.addAll(extraction);
		
		Collections.sort(extraction);
		
		returns.put("extraction", extraction);
		returns.put("article", article);
		returns.put("comment", article.getCommentsUser(userInfo.getUser().getId()));

		result.use(Results.json()).indented().withoutRoot().from(returns).recursive().serialize();
	}

	@Get("/extraction/show/{mapid}")
	public void showExtractionEvaluates(Long mapid) {
		MapStudy mapStudy = this.mapStudyDao.find(mapid);
		User user = this.userInfo.getUser();
		Double percentEvaluatedDouble = 0d;

		if (mapStudy == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.exist", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}
		if (!mapStudy.members().contains(user)) {
			MessagesController.addMessage(new Mensagem("user", "user.does.not.have.access", TypeMessage.INFORMATION));
			result.redirectTo(MapStudyController.class).list();
			return;
		}

		List<Article> extractions = new ArrayList<Article>();

		HashMap<String, HashMap<String, Long>> ext = new HashMap<String, HashMap<String, Long>>();

		if (!mapStudy.isSupervisor(user)) {
			extractions = this.articleDao.getExtractions(this.userInfo.getUser(), mapStudy);

			if (extractions.isEmpty()) {
				MessagesController.addMessage(new Mensagem("mapstudy.articles", "mapstudy.extraction.none", TypeMessage.INFORMATION));
				result.redirectTo(MapStudyController.class).show(mapid);
				return;
			}

			for (Article article : extractions) {
				for (EvaluationExtraction ee : article.getEvaluationExtraction(user)) {
					String questionName = "";
					if (ee.getQuestion() != null) {
						questionName = ee.getQuestion().getName();
					}

					HashMap<String, Long> aux = new HashMap<String, Long>();

					if (ext.containsKey(questionName)) {
						aux = ext.get(questionName);
					}

					String alternativeValue = ee.getAlternative().getValue();
					Long count = 1l;

					if (aux.containsKey(alternativeValue)) {
						count = aux.get(alternativeValue) + 1;
					}

					aux.put(alternativeValue, count);
					ext.put(questionName, aux);
				}
			}

			percentEvaluatedDouble = mapStudy.percentExtractedDouble(this.articleDao, user);
			this.result.include("article", extractions.get(0));
		} 
		else {
			percentEvaluatedDouble = 100d;
		}

		this.result.include("map", mapStudy);
		this.result.include("extractions", ext);
		this.result.include("form", mapStudy.getForm());
		this.result.include("percentEvaluatedDouble", percentEvaluatedDouble);
	}

	private Question questionMapStudy(MapStudy mapStudy, Long questid) {
		for (Question q : mapStudy.getForm().getQuestions()) {
			if (q.getId().equals(questid)) {
				return q;
			}
		}
		return null;
	}
	
	//TODO FASE TESTE
	@Post("/maps/extraction/concordance")
	public void concordance(Long mapid){
		MapStudy mapStudy = mapStudyDao.find(mapid);
		User user = userInfo.getUser();

		if (mapStudy == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.exist", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}
		if (!mapStudy.members().contains(user)) {
			MessagesController.addMessage(new Mensagem("user", "user.does.not.have.access", TypeMessage.INFORMATION));
			result.redirectTo(MapStudyController.class).list();
			return;
		}	
		
		evaluationExtractionFinalDao.removeAllExtractionsFinal(mapStudy);
		List<Article> articles = articleDao.getArticlesFinalEvaluate(mapStudy, EvaluationStatusEnum.ACCEPTED);

		List<User> members = UserUtil.removeUserRole(mapStudy, Roles.SUPERVISOR);

		Collections.sort(members, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u1.getName().compareTo(u2.getName());
			}
		});
		

		for (Article article : articles){
			/**
			 * Obter as extrações feitas por cada usuário
			 */
			for (User u : members) {
				List<EvaluationExtraction> extractionsList = article.getEvaluationExtraction(u); // Extrações feitas pelo usuário u
				// Para cada extração obter os dados dela
				if(!extractionsList.isEmpty()){
					for (EvaluationExtraction evaluationExtraction : extractionsList) {
						article.addExtractionFinal(evaluationExtraction);
					}
				}
			}
		}
		
		MessagesController.addMessage(new Mensagem("mapstudy.article", "mapstudy.article.concordance.success", TypeMessage.SUCCESS));
		result.redirectTo(this).compare(mapid);
	}
	

	@Get("/maps/{mapid}/compareExtractions")
	public void compare(Long mapid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		User user = userInfo.getUser();

		if (mapStudy == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.exist", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}
		if (!mapStudy.members().contains(user)) {
			MessagesController.addMessage(new Mensagem("user", "user.does.not.have.access", TypeMessage.INFORMATION));
			result.redirectTo(MapStudyController.class).list();
			return;
		}		
		
		result.redirectTo(this).finalExtractionLoad(mapid, 0l);
	}

	@Get("/maps/{mapid}/articleCompare/{articleid}")
	public void finalExtractionLoad(Long mapid, Long articleid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		User user = userInfo.getUser();
		
		if (mapStudy == null) {
			MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.is.not.exist", TypeMessage.ERROR));
			result.redirectTo(MapStudyController.class).list();
			return;
		}

		List<Article> articlesToCompare = articleDao.getArticlesToFinalExtraction(mapStudy);
		List<Article> articlesFinalExtracted = articleDao.getArticlesFinalExtraction(mapStudy);

		Article article = null, nextArticle = null;
		if (articleid != 0) {
			article = articleDao.find(articleid);
		} else {
			article = getNextToEvaluate(articlesToCompare, null);
			if (article != null) {
				articleid = article.getId();
			}
		}

		if (article != null) {
			nextArticle = getNextToEvaluate(articlesToCompare, articleid);
			if (nextArticle != null) {
			}
		}

		if (article == null) {
			article = articlesFinalExtracted.isEmpty() ? null : articlesFinalExtracted.get(0);
			MessagesController.addMessage(new Mensagem("mapstudy.article", "mapstudy.extraction.final.articles.none", TypeMessage.INFORMATION));
		}
		
		if(article == null){
			MessagesController.addMessage(new Mensagem("mapstudy.article", "mapstudy.extraction.articles.none", TypeMessage.ERROR));
			result.redirectTo(this).showExtractionEvaluates(mapid);
			return;
		}

//		List<User> members = userDao.mapStudyUsers(mapStudy);
		List<User> members = UserUtil.removeUserRole(mapStudy, Roles.SUPERVISOR);

//		if (mapStudy.isSupervisor(user)) {
//			members.remove(user);
//		}

		Collections.sort(members, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u1.getName().compareTo(u2.getName());
			}
		});

		ExtractionCompareVO extractionCompareVO = new ExtractionCompareVO(article); // Faz comparação de extrações

		/**
		 * Obter as extrações feitas por cada usuário
		 */
		for (User u : members) {
			List<EvaluationExtraction> extractionsList = article.getEvaluationExtraction(u); // Extrações feitas pelo
																								// usuário u

			// Para cada extração obter os dados dela
			for (EvaluationExtraction ee : extractionsList) {
				extractionCompareVO.addQueston(ee.getQuestion(), ee.getAlternative(), ee.getUser());
			}
		}

		result.include("mapStudy", mapStudy);
		result.include("members", members);
		result.include("articlesFinalExtracted", articlesFinalExtracted);
		result.include("articlesToCompare", articlesToCompare);
		result.include("extractionCompareVO", extractionCompareVO);
		MessagesController.addMessage(new Mensagem("mapstudy.article", "mapstudy.article.load.success", TypeMessage.SUCCESS));
	}

	@Post
	public void finalExtraction(Long mapid, Long articleid, List<ExtractionFinalVO> questions) {
		EvaluationExtractionFinal eef = null;

		MapStudy mapStudy = mapStudyDao.find(mapid);
		Article article = articleDao.find(articleid);

		if (questions != null) {
			int count = questions.size();

			for (int i = 0; i < count; i++) {
				Long questionId = questions.get(i).getQuestionId();
				if (questionId == null)
					continue;

				Question question = questionDao.find(questionId);
				boolean enter = true;
				List<Long> alternatives = questions.get(i).getAlternatives();

				for (Long alternative_id : alternatives) {
					if (!alternative_id.equals(0)) {
						if (question.getType().equals(QuestionType.MULT)) {
							if (enter) {
								article.removeEvaluationExtractionFinal(question, evaluationExtractionFinalDao);
								enter = false;
							}
						} else {
							article.removeEvaluationExtractionFinal(question, alternative_id,
									evaluationExtractionFinalDao);
						}

						eef = new EvaluationExtractionFinal();
						eef.setMapStudy(mapStudy);
						eef.setArticle(article);

						Alternative alternative = alternativeDao.find(alternative_id);

						eef.setQuestion(question);
						eef.setAlternative(alternative);

						article.getEvaluationExtractionsFinal().add(eef);
					}
				}
			}
		}

		articleDao.update(article);

		result.redirectTo(this).finalExtractionLoad(mapid, 0l);
	}

	@Path("/maps/{mapStudyId}/extractions/mine")
	@Get
	public Download downloadMine(Long mapStudyId) throws IOException {
		MapStudy mapStudy = mapStudyDao.find(mapStudyId);
		List<Article> articles = articleDao.getExtractions(userInfo.getUser(), mapStudy);

		if (articles.size() < 0) {
			MessagesController.addMessage(new Mensagem("mapstudy.articles", "mapstudy.articles.extraction.none", TypeMessage.INFORMATION));
			result.redirectTo(this).showExtractionEvaluates(mapStudyId);
			return null;
		}
		
		return generateFile(mapStudy, articles, false);
	}

	@Path("/maps/{mapStudyId}/extractions/all")
	@Get
	public Download downloadAll(Long mapStudyId) throws IOException {
		MapStudy mapStudy = mapStudyDao.find(mapStudyId);

		// Todos os artigos com a avaliação final aceita
		List<Article> articles = articleDao.getArticlesFinalExtraction(mapStudy);

		if (articles.size() < 0) {
			MessagesController.addMessage(new Mensagem("mapstudy.articles", "mapstudy.articles.extractionfinal.all.none", TypeMessage.INFORMATION));
			result.redirectTo(this).showExtractionEvaluates(mapStudyId);
			return null;
		}
		
		return generateFile(mapStudy, articles, true);
	}

	private Download generateFile(MapStudy mapStudy, List<Article> articles, boolean all) throws IOException {
		
		Long time = System.currentTimeMillis();
		String filename = "Extractions_" + mapStudy.getId() + "_" + time + ".csv";
		String temp = System.getProperty("java.io.tmpdir");

		File file = new File(temp + filename);

		String encoding = "ISO-8859-1";
		FileWriterWithEncoding writer = new FileWriterWithEncoding(file, encoding, false);

		Collections.sort(articles, new Comparator<Article>() {
			@Override
			public int compare(Article a1, Article a2) {
				return a1.getId().compareTo(a2.getId());
			}
		});

		String data = "";

		String delimiter = ";";

		

		data += "Id" + delimiter;
		data += "Title" + delimiter;
		data += "Author" + delimiter;
		data += "Journal" + delimiter;
		data += "Year" + delimiter;
		data += "DocType" + delimiter;
		data += "Source" + delimiter;

		QuestionAndAlternativeCSV[] head = questionsName(mapStudy);

		// create head name questions
		for (QuestionAndAlternativeCSV s : head) {
			// writer.append(s + delimiter);
			int count = s.getAlternatives().size();
			data += (s.getQuestionName() + newCol(count == 0 ? 1 : count));
		}

		data += "\n;;;;;;;";

		// tem que adicionar o nome das alternativas nessa linha saltando 7 colunas

		// create head name questions
		for (QuestionAndAlternativeCSV s : head) {
			boolean simple = true;

			for (String alt : s.getAlternatives()) {
				data += alt + ";";
				simple = false;
			}

			if (simple) {
				data += ";";
			}
		}

		data += "\n";

		HashMap<String, List<String>> questionsAndAlternative = new HashMap<String, List<String>>();

		for (Article a : articles) {

			data += (a.getId() + delimiter);
			String title = a.getTitle().replace('\n', ' ').replace(';', ' ');
			data += (title + delimiter);
			String author = a.getAuthor().replace('\n', ' ').replace(';', ' ');
			data += (author + delimiter);
			String journal = a.getJournal();
			journal = (journal != null ? journal.replace('\n', ' ').replace(';', ' ') : "Dado não extraído");
			data += (journal + delimiter);

			data += (a.getYear() != null ? a.getYear() : "Dado não extraído") + delimiter;
			data += (a.getDocType() != null && !a.getDocType().equals("") ? a.getDocType() : "Dado não extraído")
					+ delimiter;
			data += a.sourceView(a.getSource()) + delimiter;

			if (all) {
				questionsAndAlternative = a.getEvaluateFinalExtractionAlternatives();
			} else {
				questionsAndAlternative = a.getEvaluateFinalExtractionAlternatives(userInfo.getUser());
			}

			for (QuestionAndAlternativeCSV headRead : head) {
				// writer.append((questionsAndAlternative.get(s) != null ?
				// questionsAndAlternative.get(s) : "Dado não extraído") + delimiter);
				List<String> alternatives = questionsAndAlternative.get(headRead.getQuestionName());

				int count = headRead.getAlternatives().size();

				if (count == 0) {
					String alt = alternatives != null ? alternatives.get(0) : "Dado não extraído";
					data = data + (alt + delimiter);
				} else {
					for (int i = 0; i < count; i++) {
						// if (headRead.getAlternatives().get(i).equals(alternatives.get(0))){
						String alt = headRead.getAlternatives().get(i);
						if (alternatives == null) {
							data = data + "Dado não extraído" + delimiter;
							continue;
						}

						if (alternatives.contains(alt)) {
							data = data + "1;";
						} else {
							data = data + "0;";
						}
					}
				}

			}
			// writer.append('\n');
			data += "\n";
		}

		writer.append(data);

		writer.flush();
		writer.close();

		String contentType = "text/csv";
		return new FileDownload(file, contentType, filename);
	}

	private QuestionAndAlternativeCSV[] questionsName(MapStudy mapStudy) {// , String defaultExtraction[]) {
		int count = mapStudy.getForm().getQuestions().size();// + defaultExtraction.length;
		QuestionAndAlternativeCSV[] head = new QuestionAndAlternativeCSV[count];

		int i = 0;
		for (Question q : mapStudy.getForm().getQuestions()) {
			List<String> alternatives = new ArrayList<String>();

			if (!q.getType().equals(QuestionType.SIMPLE)) {
				for (Alternative a : q.getAlternatives()) {
					alternatives.add(a.getValue());
				}
			}

			head[i] = new QuestionAndAlternativeCSV(q.getName(), alternatives);
			++i;
		}

		return head;
	}

	public void alternatives(Long questionId, Long mapid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		Question question = questionDao.find(questionId);

		List<Alternative> alternatives = questionDao.getAlternativesFinalExtraction(question, mapStudy);

		result.use(Results.json()).indented().withoutRoot().from(alternatives).recursive().serialize();
	}

	private String newCol(int count) {
		String s = "";
		for (int i = 0; i < count; i++) {
			s = s + ";";
		}
		return s;
	}

}
