package br.com.ufpi.systematicmap.controller;

import static br.com.caelum.vraptor.view.Results.json;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.FileDownload;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.com.ufpi.systematicmap.components.FilterArticles;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.dao.EvaluationDao;
import br.com.ufpi.systematicmap.dao.ExclusionCriteriaDao;
import br.com.ufpi.systematicmap.dao.InclusionCriteriaDao;
import br.com.ufpi.systematicmap.dao.MapStudyDao;
import br.com.ufpi.systematicmap.dao.ResearchQuestionDao;
import br.com.ufpi.systematicmap.dao.SearchStringDao;
import br.com.ufpi.systematicmap.dao.UserDao;
import br.com.ufpi.systematicmap.files.FilesUtils;
import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.Evaluation;
import br.com.ufpi.systematicmap.model.ExclusionCriteria;
import br.com.ufpi.systematicmap.model.InclusionCriteria;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Question;
import br.com.ufpi.systematicmap.model.ResearchQuestion;
import br.com.ufpi.systematicmap.model.SearchString;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.ArticleSourceEnum;
import br.com.ufpi.systematicmap.model.enums.ClassificationEnum;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;
import br.com.ufpi.systematicmap.model.enums.QuestionType;
import br.com.ufpi.systematicmap.model.vo.ArticleCompareVO;
import br.com.ufpi.systematicmap.model.vo.Percent;
import br.com.ufpi.systematicmap.utils.BibtexToArticleUtils;
import br.com.ufpi.systematicmap.utils.BibtexUtils;
import br.com.ufpi.systematicmap.utils.FleissKappa;
import br.com.ufpi.systematicmap.utils.Linker;
import br.com.ufpi.systematicmap.utils.MailUtils;

@Controller
public class MapStudyController {

	private Result result;
	private Validator validator;
	private UserInfo userInfo;
	private FilesUtils files;
	private Linker linker;
	
	private MapStudyDao mapStudyDao;
	private UserDao userDao;
	private ArticleDao articleDao;
	private InclusionCriteriaDao inclusionDao;
	private ExclusionCriteriaDao exclusionDao;
	private EvaluationDao evaluationDao;
	private ResearchQuestionDao questionDao;
	private SearchStringDao stringDao;
	
	private MailUtils mailUtils;
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected MapStudyController() {
		this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}


	@Inject
	public MapStudyController(MapStudyDao musicDao, UserInfo userInfo, 
				Result result, Validator validator, FilesUtils files, 
				UserDao userDao, ArticleDao articleDao, 
				InclusionCriteriaDao inclusionDao,
				ExclusionCriteriaDao exclusionDao,
				EvaluationDao evaluationDao, MailUtils mailUtils, Linker linker, ResearchQuestionDao questionDao, SearchStringDao stringDao) {
		this.mapStudyDao = musicDao;
		this.result = result;
        this.validator = validator;
        this.userInfo = userInfo;
		this.files = files;
		this.userDao = userDao;
		this.articleDao = articleDao;
		this.inclusionDao = inclusionDao;
		this.exclusionDao = exclusionDao;
		this.evaluationDao = evaluationDao;
		this.mailUtils = mailUtils;
		this.linker = linker;
		this.questionDao = questionDao;
		this.stringDao = stringDao;
	}
	
	
	@Get("/maps")
	public void list() {
		result.include("mapStudys", mapStudyDao.mapStudys(userInfo.getUser()));
	}

	@Post("/maps")
	public void add(final @NotNull @Valid MapStudy mapstudy) {
		validator.onErrorForwardTo(this).create();
		
		User user = userInfo.getUser();
		userDao.refresh(user);
		
		//set member creator map 
		mapstudy.addCreator(user);
		mapStudyDao.add(mapstudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "mapstudy.add.sucess"));
		result.redirectTo(this).list();
	}
	
	@Get("/maps/{mapId}/remove")
	public void remove(Long mapId) {
		validator.onErrorForwardTo(this).list();
		MapStudy mapStudy = mapStudyDao.find(mapId);
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).list();
		
		//TODO seta todos removed ou somente o mapeamento ?
		mapStudy.setRemoved(true);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "mapstudy.remove.sucess"));
		result.redirectTo(this).list();
	}

	
	@Get("/maps/{id}")
	public void show(Long id) {
		MapStudy mapStudy = mapStudyDao.find(id);
		User user =  userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		if (!(mapStudy != null)) {
			validator.onErrorRedirectTo(this).list();
		}

		validator.check(mapStudy.members().contains(user), new SimpleMessage("user", "user.is.not.mapstudy"));
		if (!(mapStudy.members().contains(user))) {
			validator.onErrorRedirectTo(this).list();
		}
		
		List<User> mapStudyUsersList = userDao.mapStudyUsers(mapStudy);
		List<User> mapStudyArentUsers = userDao.mapStudyArentUsers(mapStudy);
		
		Double totalPercentEvaluated = 0.0, totalPercentExtracted = 0.0;
		
		HashMap<User, Percent> mapStudyUsers = new HashMap<User, Percent>();
//		HashMap<User, String> mapStudyUsersExtraction = new HashMap<User, String>();
		for(User u : mapStudyUsersList){
			Double percentEvaluatedDouble = mapStudy.percentEvaluatedDouble(articleDao, u);
			totalPercentEvaluated += percentEvaluatedDouble; 
//			mapStudyUsers.put(u, mapStudy.percentEvaluated(percentEvaluatedDouble));
			
			Double percentExtractedDouble = mapStudy.percentExtractedDouble(articleDao, u);
			totalPercentExtracted += percentExtractedDouble; 
//			mapStudyUsersExtraction.put(u, mapStudy.percentEvaluated(percentExtractedDouble));
			
			Percent p = new Percent();
			p.setSelection(mapStudy.percentEvaluated(percentEvaluatedDouble));
			p.setExtraction(mapStudy.percentEvaluated(percentExtractedDouble));
			mapStudyUsers.put(u, p);
		}
		
		totalPercentEvaluated = totalPercentEvaluated / (double) mapStudyUsersList.size();
		totalPercentExtracted = totalPercentExtracted / (double) mapStudyUsersList.size();
		
	    result.include("map", mapStudy);
	    result.include("sources", ArticleSourceEnum.values());
	    result.include("percentEvaluated", String.format("%.2f", totalPercentEvaluated));
	    result.include("percentExtracted", String.format("%.2f", totalPercentExtracted));	
	    result.include("mapStudyUsers", mapStudyUsers);
	    result.include("mapStudyArentUsers", mapStudyArentUsers);
	}

	//TODO Verificar que ação deve ser tomada ao remover um membro
	@Get("/maps/{id}/removemember/{userId}")
	public void removemember(Long id, Long userId){
		validator.onErrorForwardTo(this).show(id);

		MapStudy mapStudy = mapStudyDao.find(id);
		User user = userDao.find(userId);
		User currentUser = userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.isCreator(currentUser), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).show(id);
		
		validator.check(!currentUser.equals(user), new SimpleMessage("user", "user.is.not.remove.creator"));
		validator.onErrorRedirectTo(this).show(id);	
		
		// Um usuario esta associado a avaliações e outras coisas caso o mesmo seja removido apos o inicio dos trabalhos devemos ocultar suas avaliações mas não removelas
		
		mapStudy.removeParticipant(user);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "member.remove.sucess"));
		result.redirectTo(this).show(id);
	}
	
	@Post("/maps/addmember")
	public void addmember(Long id, Long userId, boolean notify){
		validator.onErrorForwardTo(this).show(id);
		
		MapStudy mapStudy = mapStudyDao.find(id);
		User user = null;
		
		if (userId != null){
			user = userDao.find(userId);
		}
		
		validator.check(user != null, new SimpleMessage("user", "user.is.not.select"));
		validator.onErrorRedirectTo(this).show(id);
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).show(id);
		
		mapStudy.addParticipant(user);
		
		mapStudyDao.update(mapStudy);
		userDao.update(user);
		
		if (notify){
			linker.buildLinkTo(HomeController.class).home();
			String url = "<a href=\""+linker.getURL()+"\" target=\"_blank\">Clique aqui</a> para acessar o site.";		
			
			String message = "<p>Ol&aacute; " + user.getName()+ ",</p>"
					+ "<p>Voc&ecirc; foi adicionado a um mapeamento sistemático.</p>"
					+ "<p>T&iacute;tulo: "+mapStudy.getTitle()+"</p>"
					+ "<p>Descri&ccedil;&atilde;o: "+mapStudy.getDescription()+"</p>"
					+ "<p>Adicionado por: "+userInfo.getUser().getName()+"</p>"
					+ "<p>Contato: "+userInfo.getUser().getEmail()+"</p>"
					+ "<p>"+ url +"</p>";
			//Send mail
			try {
				mailUtils.send("[TheEND] - Convite de Participação", message, user.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		result.include("notice", new SimpleMessage("mapstudy", "member.add.sucess"));
		result.redirectTo(this).show(id);
	}
	
	@Post("/maps/addarticles")
	public void addarticles(Long id, UploadedFile upFile, ArticleSourceEnum source){
		validator.onErrorForwardTo(this).identification(id);
		
		MapStudy mapStudy = mapStudyDao.find(id);
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).identification(id);
		
		BibTeXDatabase database = null;
		
		if (upFile != null){
			files.save(upFile, mapStudy);			
			try {
				database = BibtexUtils.parseBibTeX(files.getFile(mapStudy));
			} catch (IOException | ParseException e) {
			}
		}
		
		validator.check(database!=null, new SimpleMessage("path", "no.select.path"));
		validator.onErrorRedirectTo(this).identification(id);
		
	    Map<Key, BibTeXEntry> entryMap = database.getEntries();
	    Collection<BibTeXEntry> entries = entryMap.values();
	    
		for(BibTeXEntry entry : entries){
			Article a = BibtexToArticleUtils.bibtexToArticle(entry, source);
			mapStudy.addArticle(a);
			articleDao.insert(a);
		}
		
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy.articles", "articles.add.sucess"));
	    result.redirectTo(this).identification(id);
	}
	
	//TODO teste de inserir trabalhos manualmente @Get e @Post
	@Get("/maps/{mapId}/addmanuallyarticles")
	public void addmanuallyarticles(Long mapId){
		validator.onErrorForwardTo(this).identification(mapId);
		
		MapStudy mapStudy = mapStudyDao.find(mapId);
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).identification(mapId);
		
		result.include("map", mapStudy);
	}
	
	@Post("/maps/addmanuallyarticles")
	public void addmanuallyarticlesform(Long mapId, final @NotNull @Valid Article article){
		validator.onErrorForwardTo(this).addmanuallyarticles(mapId);
		
		MapStudy mapStudy = mapStudyDao.find(mapId);
		
		article.setSource(ArticleSourceEnum.MANUALLY.toString());
		
		mapStudy.addArticle(article);
		article.setMapStudy(mapStudy);
		
		articleDao.insert(article);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy.articles", "article.add.sucess"));
	    result.redirectTo(this).identification(mapStudy.getId());
	}
	
	//TODO A parte de visão não está completa, adicionar um botão selecionar todos, e melhorar aparência
	@Get("/maps/{mapId}/removearticles")
	public void removearticles(Long mapId){
		validator.onErrorForwardTo(this).identification(mapId);
		
		MapStudy mapStudy = mapStudyDao.find(mapId);
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).identification(mapId);
		
		result.include("articles", articleDao.getArticles(mapStudy));
		result.include("map", mapStudy);
	}
	
	@Post("/maps/removearticles")
	public void removearticlesform(Long mapId, final List<Integer> articlesIds){
		validator.onErrorForwardTo(this).identification(mapId);
		
		validator.check(articlesIds != null, new SimpleMessage("mapstudy.articles", "article.is.not.select"));
		validator.onErrorRedirectTo(this).identification(mapId);

		for (Integer id : articlesIds) {
			articleDao.delete(id.longValue());
		}
		
		result.include("notice", new SimpleMessage("mapstudy.articles", "article.remove.sucess"));
		result.redirectTo(this).identification(mapId);		
	}
	
	@Post("/maps/addinclusion")
	public void addinclusion(Long id, String description) {
		validator.onErrorRedirectTo(this).planning(id, "divcriterias");
		
		MapStudy mapStudy = mapStudyDao.find(id);
		
		InclusionCriteria inclusionCriteria = new InclusionCriteria();
		inclusionCriteria.setDescription(description);
		inclusionCriteria.setMapStudy(mapStudy);
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).planning(id, "divcriterias");	
		
		validator.check(inclusionCriteria.getDescription() != null, new SimpleMessage("mapstudy.inclusion.criteria", "error.not.null"));
		//TODO onErrorRedirectTo da problema porque ?
		validator.onErrorRedirectTo(this).planning(id, "divcriterias");
		
		mapStudy.addInclusionCriteria(inclusionCriteria);
		inclusionDao.insert(inclusionCriteria);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "inclusion.criteria.add.sucess"));
	    result.redirectTo(this).planning(id, "divcriterias");
		
//		result.use(Results.json()).indented().from(inclusionCriteria, "criteria").serialize();
	}
	
	@Post("/maps/addexclusion")
	public void addexclusion(Long id, String description) {
		//validator.onErrorForwardTo(this).planning(id);
		validator.onErrorRedirectTo(this).planning(id, "divcriterias");
		
		MapStudy mapStudy = mapStudyDao.find(id);
		
		ExclusionCriteria exclusionCriteria = new ExclusionCriteria();
		exclusionCriteria.setDescription(description);
		exclusionCriteria.setMapStudy(mapStudy);
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).planning(id, "divcriterias");		
		
		validator.check(exclusionCriteria.getDescription() != null, new SimpleMessage("mapstudy.exclusion.criteria", "error.not.null"));
		validator.onErrorRedirectTo(this).planning(id, "divcriterias");
		
		mapStudy.addExclusionCriteria(exclusionCriteria);
		exclusionDao.insert(exclusionCriteria);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "exclusion.criteria.add.sucess"));
	    result.redirectTo(this).planning(id, "divcriterias");
		
//		result.use(Results.json()).indented().from(exclusionCriteria, "criteria").serialize();
	}
	
	@Post("/maps/refinearticles")
	public void refinearticles(Long id, Integer levenshtein, String regex, Integer limiartitulo, Integer limiarabstract, Integer limiarkeywords, Integer limiartotal){
		MapStudy mapStudy = mapStudyDao.find(id);
		
		FilterArticles filter = new FilterArticles(mapStudy.getArticles(), levenshtein, regex.trim(), limiartitulo, limiarabstract, limiarkeywords, limiartotal);
		filter.filter();
		
		//TODO está faltando algo aqui ?
		validator.onErrorForwardTo(this).identification(id);
		
		result.include("notice", new SimpleMessage("mapstudy", "refine.articles.sucess"));
		result.redirectTo(this).identification(id);
	}
	
	@Get("/maps/{id}/unrefinearticles")
	public void unrefinearticles(Long id){
		MapStudy mapStudy = mapStudyDao.find(id);
		
		Set<Article> articles = mapStudy.getArticles();
		
		for (Article article : articles) {
			article.setMinLevenshteinDistance(0);
			article.setPaperMinLevenshteinDistance(null);
			article.setRegexAbs(0);
			article.setRegexKeys(0);
			article.setRegexTitle(0);
			article.setScore(0);
			article.setClassification(null);
			
			if (article.getComments() != null){
				List<ClassificationEnum> classifications = asList(ClassificationEnum.values());
				for (ClassificationEnum classificationEnum : classifications) {
					article.getComments().replace(classificationEnum.toString(), "");
				}
			}
		}
		
		//TODO está faltando algo aqui ?
		validator.onErrorForwardTo(this).identification(id);
		
		result.include("notice", new SimpleMessage("mapstudy", "unrefine.articles.sucess"));
		result.redirectTo(this).identification(id);
	}
	
	@Get("/maps/evaluate/{mapid}")
	public void evaluate(Long mapid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		User user = userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.members().contains(user), new SimpleMessage("user", "user.is.not.mapstudy"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check((mapStudy.getExclusionCriterias().size() > 0 && mapStudy.getInclusionCriterias().size() > 0), new SimpleMessage("mapstudy", "mapstudy.is.not.criterias"));
		validator.onErrorRedirectTo(this).show(mapid);
		
		result.redirectTo(this).evaluateArticle(mapid, 0l);
	}
	
	@Get("/maps/evaluate/{mapid}/article/{articleid}")
	public void evaluateArticle(Long mapid, Long articleid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();

		List<Article> articlesToEvaluate = articleDao.getArticlesToEvaluate(userInfo.getUser(), mapStudy);
		List<Evaluation> evaluations = evaluationDao.getEvaluations(userInfo.getUser(), mapStudy);
		Article article = null, nextArticle = null;
		Long nextArticleId = null;
		if(articleid != 0){
			article = articleDao.find(articleid);
		}else{
			article = getNextToEvaluate(articlesToEvaluate, null);
			if(article != null){
				articleid = article.getId();
			}
		}
		
		if(article != null){
			nextArticle = getNextToEvaluate(articlesToEvaluate, articleid);
			if(nextArticle != null){
				nextArticleId = nextArticle.getId();
			}
		}
		
		if (article == null){
			result.include("warning", new SimpleMessage("mapstudy", "mapstudy.evaluate.articles.none"));
			result.redirectTo(this).show(mapid);
		}
		
//		validator.check((article != null), new SimpleMessage("mapstudy", "mapstudy.evaluate.articles.none"));
//		validator.onErrorRedirectTo(this).show(mapid);
		
		Evaluation evaluationDone = evaluationDao.getEvaluation(userInfo.getUser(), mapStudy, article);
		
		//sort criterias
		TreeSet<InclusionCriteria> inclusionOrdered = new TreeSet<InclusionCriteria>(new Comparator<InclusionCriteria>(){
		    public int compare(InclusionCriteria a, InclusionCriteria b){
		        return a.getDescription().compareTo(b.getDescription());
		    }
		});
		inclusionOrdered.addAll(mapStudy.getInclusionCriterias());
		//
		TreeSet<ExclusionCriteria> exclusionOrdered = new TreeSet<ExclusionCriteria>(new Comparator<ExclusionCriteria>(){
		    public int compare(ExclusionCriteria a, ExclusionCriteria b){
		        return a.getDescription().compareTo(b.getDescription());
		    }
		});
		exclusionOrdered.addAll(mapStudy.getExclusionCriterias());
		
		Double percentEvaluatedDouble = mapStudy.percentEvaluatedDouble(articleDao, userInfo.getUser());
		
		result.include("map", mapStudy);
		result.include("exclusionOrdered", exclusionOrdered);
		result.include("inclusionOrdered", inclusionOrdered);
		
		result.include("article", article);
		result.include("nextArticleId", nextArticleId);
		result.include("articlesToEvaluate", articlesToEvaluate); // artigos a serem avaliados
		result.include("percentEvaluated", mapStudy.percentEvaluated(percentEvaluatedDouble));
		result.include("evaluations", evaluations); // artigos já avaliados
		result.include("evaluationDone", evaluationDone);
	}
	
	private static Article getNextToEvaluate(List<Article> articlesToEvaluate, Long actual){
		if(actual == null){
			return articlesToEvaluate.size() > 0 ? articlesToEvaluate.get(0) : null;
		}else{
			Article next = null;
			for(Article a : articlesToEvaluate){
				if(!a.getId().equals(actual)){
					next = a;
					break;
				}
			}
			return next;
		}
	}
	
	@Deprecated
	@Post("/maps/includearticle")
	public void includearticle(Long mapid, Long articleid, Long nextArticleId, List<Long> inclusions, String comment){
		doEvaluate(mapid, articleid, inclusions, comment, true);
		nextArticleId = nextArticleId != null ? nextArticleId : 0l;
		result.redirectTo(this).evaluateArticle(mapid, nextArticleId);
	}
	
	@Deprecated
	@Post("/maps/excludearticle")
	public void excludearticle(Long mapid, Long articleid, Long nextArticleId, List<Long> exclusions, String comment){
		doEvaluate(mapid, articleid, exclusions, comment, false);
		nextArticleId = nextArticleId != null ? nextArticleId : 0l;
		result.redirectTo(this).evaluateArticle(mapid, nextArticleId);
	}
	
	@Post("/maps/evaluate")
	public void evaluateAjax(Long mapid, Long articleid, List<Long> criterias, String comment, boolean isInclusion, Long nextArticleId){
		doEvaluate(mapid, articleid, criterias, comment, isInclusion);

		MapStudy mapStudy = mapStudyDao.find(mapid);
		Double percentEvaluatedDouble = mapStudy.percentEvaluatedDouble(articleDao, userInfo.getUser());
		
		HashMap<String, Object> returns = new HashMap<>();
		Article article = null;
		Evaluation evaluation = null;

		if (nextArticleId != null){
			article = articleDao.find(nextArticleId);
			evaluation = article.getEvaluation(userInfo.getUser());
		}else{
			article = articleDao.find(articleid);
			evaluation = article.getEvaluation(userInfo.getUser());
		}		
		
		returns.put("evaluation", evaluation);
		returns.put("article", article);
		returns.put("percent", mapStudy.percentEvaluated(percentEvaluatedDouble));
		
		result.use(Results.json()).indented().withoutRoot().from(returns).recursive().serialize();
	}
	
	@Get
	@Path("/maps/article/{articleid}/load")
	public void loadArticle(Long mapid, Long articleid){
		Article article = articleDao.find(articleid);
		
		// se o artigo não existir
		validator.check(article != null, new SimpleMessage("mapstudy", "mapstudy.evaluate.articles.none"));
		validator.onErrorRedirectTo(this).show(mapid);	
		
		HashMap<String, Object> returns = new HashMap<>();
		Evaluation evaluation = null;
		
		if (article != null){
			evaluation = article.getEvaluation(userInfo.getUser());
		}		
		
		returns.put("evaluation", evaluation);
		returns.put("article", article);
		
		result.use(Results.json()).indented().withoutRoot().from(returns).recursive().serialize();		
//		result.use(Results.json()).indented().from(article).recursive().serialize();
	}
	
	private void doEvaluate(Long mapid, Long articleid, List<Long> ids, String comment, boolean include){
		validator.check((ids != null), new SimpleMessage("mapstudy", "mapstudy.evaluate.criterias.none"));
		validator.onErrorRedirectTo(this).evaluateArticle(mapid, articleid);
		
		MapStudy mapStudy = mapStudyDao.find(mapid);
		Article article = articleDao.find(articleid);
		
		//remove last evaluation
		Evaluation existingEvaluation = evaluationDao.getEvaluation(userInfo.getUser(), mapStudy, article);
		if(existingEvaluation != null){
			evaluationDao.delete(existingEvaluation.getId());
		}
		
		Evaluation e = new Evaluation();
		e.setArticle(article);
		e.setMapStudy(mapStudy);
		e.setComment(comment);
		e.setUser(userInfo.getUser());
		
		for(Long id : ids){
			if(include){
				InclusionCriteria criteria = inclusionDao.find(id);
				e.addInclusion(criteria);
				inclusionDao.update(criteria);
			}else{
				ExclusionCriteria criteria = exclusionDao.find(id);
				e.addExclusion(criteria);
				exclusionDao.update(criteria);
			}
		}
		
		evaluationDao.insert(e);
	}
	
	/* Remove Criteria */
	@Get("/maps/{studyMapId}/removeexclusioncriteria/{criteriaId}")
	public void removeexclusioncriteriapage(Long studyMapId, Long criteriaId) {
		validator.onErrorForwardTo(this).planning(studyMapId, "divcriterias");
		
		MapStudy mapStudy = mapStudyDao.find(studyMapId);
		User user = userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.isCreator(user), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).planning(studyMapId, "divcriterias");
				
		ExclusionCriteria criteria = exclusionDao.find(criteriaId);
		Set<Evaluation> evaluations = criteria.getEvaluations();
		
		List<Evaluation> evaluationsImpacted = new ArrayList<Evaluation>();
		List<Evaluation> evaluationsRemoved = new ArrayList<Evaluation>();
		for(Evaluation e : evaluations){
			if(e.getExclusionCriterias().size() == 1 &&
				e.getExclusionCriterias().contains(criteria)){
				evaluationsRemoved.add(e);
			}
			evaluationsImpacted.add(e);
		}
		
		result.include("criteria", criteria);
		result.include("evaluationsImpacted", evaluationsImpacted);
		result.include("evaluationsRemoved", evaluationsRemoved);
	}
	
	@Post("/maps/removeexclusioncriteria/")
	public void removeexclusioncriteria(Long studyMapId, Long criteriaId) {
		validator.onErrorForwardTo(this).planning(studyMapId, "divcriterias");
		
		ExclusionCriteria criteria = exclusionDao.find(criteriaId);
		Set<Evaluation> evaluations = criteria.getEvaluations();
		
		for(Evaluation e : evaluations){
			if(e.getExclusionCriterias().size() == 1 &&
				e.getExclusionCriterias().contains(criteria)){
				evaluationDao.delete(e.getId());
			}else{
				e.getExclusionCriterias().remove(criteria);
				evaluationDao.update(e);
			}
		}
		
		exclusionDao.delete(criteriaId);
		
		result.include("notice", new SimpleMessage("mapstudy", "exclusion.criteria.remove.sucess"));		
		result.redirectTo(this).planning(studyMapId, "divcriterias");
	}
	
	/* Remove Criteria Inclusion*/
	//TODO Matheus Revisar isso
	@Get("/maps/{studyMapId}/removeinclusioncriteria/{criteriaId}")
	public void removeinclusioncriteriapage(Long studyMapId, Long criteriaId) {
		validator.onErrorForwardTo(this).planning(studyMapId,"divcriterias");
		
		MapStudy mapStudy = mapStudyDao.find(studyMapId);
		User user = userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.isCreator(user), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).planning(studyMapId, "divcriterias");
				
		InclusionCriteria criteria = inclusionDao.find(criteriaId);
		Set<Evaluation> evaluations = criteria.getEvaluations();
		
		List<Evaluation> evaluationsImpacted = new ArrayList<Evaluation>();
		List<Evaluation> evaluationsRemoved = new ArrayList<Evaluation>();
		
		for(Evaluation e : evaluations){
			if(e.getInclusionCriterias().size() == 1 &&
				e.getInclusionCriterias().contains(criteria)){
				evaluationsRemoved.add(e);
			}
			evaluationsImpacted.add(e);
		}
		
		result.include("criteria", criteria);
		result.include("evaluationsImpacted", evaluationsImpacted);
		result.include("evaluationsRemoved", evaluationsRemoved);
	}
	
	@Post("/maps/removeinclusioncriteria/")
	public void removeinclusioncriteria(Long studyMapId, Long criteriaId) {
		validator.onErrorForwardTo(this).planning(studyMapId, "divcriterias");
		
		InclusionCriteria criteria = inclusionDao.find(criteriaId);
		Set<Evaluation> evaluations = criteria.getEvaluations();
		
		for(Evaluation e : evaluations){
			if(e.getInclusionCriterias().size() == 1 &&
				e.getInclusionCriterias().contains(criteria)){
				evaluationDao.delete(e.getId());
			}else{
				e.getInclusionCriterias().remove(criteria);
				evaluationDao.update(e);
			}
		}
		
		inclusionDao.delete(criteriaId);
		
		result.include("notice", new SimpleMessage("mapstudy", "inclusion.criteria.remove.sucess"));		
		result.redirectTo(this).planning(studyMapId, "divcriterias");
	}
	
	@Get("/maps/{studyMapId}/evaluates/")
	public void showEvaluates(Long studyMapId) {
		MapStudy mapStudy = mapStudyDao.find(studyMapId);
		User user = userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.members().contains(user), new SimpleMessage("user", "user.is.not.mapstudy"));
		validator.onErrorRedirectTo(this).list();		
		
		List<Evaluation> evaluations = evaluationDao.getEvaluations(user, mapStudy);
		List<Article> articles = articleDao.getArticles(mapStudy);
		
		validator.check(articles.size() > 0, new SimpleMessage("mapstudy.articles", "mapstudy.articles.none"));
		validator.onErrorRedirectTo(this).show(studyMapId);
		
		validator.check(evaluations.size() > 0, new SimpleMessage("mapstudy.evaluations", "mapstudy.articles.not.evaluations"));
		validator.onErrorRedirectTo(this).show(studyMapId);
		
		HashMap<InclusionCriteria, Integer> inclusionCriterias = new HashMap<InclusionCriteria, Integer>();
		HashMap<ExclusionCriteria, Integer> exclusionCriterias = new HashMap<ExclusionCriteria, Integer>();
		
		int countRejected = 0, countAccepted = 0, countToDo = 0;
		
		for(Evaluation e : evaluations){
			if(e.getEvaluationStatus().equals(EvaluationStatusEnum.ACCEPTED)){
				countAccepted++;
			}else if(e.getEvaluationStatus().equals(EvaluationStatusEnum.REJECTED)){
				countRejected++;
			}
			
			if(e.getEvaluationStatus().equals(EvaluationStatusEnum.ACCEPTED)){
				for(InclusionCriteria i : e.getInclusionCriterias()){
					if(inclusionCriterias.containsKey(i)){
						inclusionCriterias.put(i, inclusionCriterias.get(i)+1);
					}else{
						inclusionCriterias.put(i, 1);
					}
				}
			}else if(e.getEvaluationStatus().equals(EvaluationStatusEnum.REJECTED)){
				for(ExclusionCriteria ec : e.getExclusionCriterias()){
					if(exclusionCriterias.containsKey(ec)){
						exclusionCriterias.put(ec, exclusionCriterias.get(ec)+1);
					}else{
						exclusionCriterias.put(ec, 1);
					}
				}
			}
		}
		
		int countRepeated = 0, countDontMatch = 0, countWithoutAuthors = 0, countWithoutAbstracts = 0, countWithoutClassification = 0, countClassified= 0;
		
		for(Article a : articles){
			if(a.getClassification() != null){
				if(a.getClassification().equals(ClassificationEnum.REPEAT)){
					countRepeated++;
				}else if(a.getClassification().equals(ClassificationEnum.WORDS_DONT_MATCH)){
					countDontMatch++;
				}else if(a.getClassification().equals(ClassificationEnum.WITHOUT_AUTHORS)){
					countWithoutAuthors++;
				}else if(a.getClassification().equals(ClassificationEnum.WITHOUT_ABSTRACT)){
					countWithoutAbstracts++;
				}
				countClassified++;
			}else{
				countWithoutClassification++;
			}
		}
		
		Double percentEvaluatedDouble = mapStudy.percentEvaluatedDouble(articleDao, user);
		
		result.include("user", user);
		result.include("mapStudy", mapStudy);
		result.include("articles", articles);
		result.include("inclusionCriteriasMap", inclusionCriterias);
		result.include("exclusionCriteriasMap", exclusionCriterias);
		
		result.include("percentEvaluated", mapStudy.percentEvaluated(percentEvaluatedDouble));
		result.include("percentEvaluatedDouble", percentEvaluatedDouble);
				
		result.include("countAccepted", countAccepted);
		result.include("countRejected", countRejected);
		result.include("countToDo", countToDo);
		
		result.include("countRepeated", countRepeated);
		result.include("countDontMatch", countDontMatch);
		result.include("countWithoutAuthors", countWithoutAuthors);
		result.include("countWithoutAbstracts", countWithoutAbstracts);
		result.include("countWithoutClassification", countWithoutClassification);
		result.include("countClassified", countClassified);
	}
	
	@Path("/maps/{mapStudyId}/accepted/mine")
	@Get
	public Download downloadMine(Long mapStudyId) throws IOException {
		MapStudy mapStudy = mapStudyDao.find(mapStudyId);
		List<Evaluation> evaluations = evaluationDao.getEvaluations(userInfo.getUser(), mapStudy);
		List<Article> articles = new ArrayList<Article>();
		
		validator.check(evaluations.size() > 0, new SimpleMessage("mapstudy.evaluations", "mapstudy.articles.not.evaluations"));
		validator.onErrorRedirectTo(this).showEvaluates(mapStudyId);
		
		for(Evaluation e : evaluations){
			if(e.getEvaluationStatus().equals(EvaluationStatusEnum.ACCEPTED)){
				articles.add(e.getArticle());
			}
		}
		
		validator.check(articles.size() > 0, new SimpleMessage("mapstudy.articles", "mapstudy.articles.none"));
		validator.onErrorRedirectTo(this).show(mapStudyId);
		
		return generateFile(mapStudy, articles);
	}
	
	@Path("/maps/{mapStudyId}/accepted/all")
	@Get
	public Download downloadAll(Long mapStudyId) throws IOException {
		MapStudy mapStudy = mapStudyDao.find(mapStudyId);
		List<Article> articles = articleDao.getArticlesFinalAccepted(mapStudy);
		
		validator.check(articles.size() > 0, new SimpleMessage("mapstudy.articles", "mapstudy.articles.accepted.all.none"));
		validator.onErrorRedirectTo(this).showEvaluates(mapStudyId);
		
		return generateFile(mapStudy, articles);
	}
	
	//TODO Depois adicionar a visão uma lista dos possiveis campos para o arquivo
	private Download generateFile(MapStudy mapStudy, List<Article> articles) throws IOException {
		
		//create file
//		String filename = mapStudy.getTitle().replaceAll(" ", "_")+ ".csv";
		Long time = System.currentTimeMillis();
		String filename =  "Evaluations_" + mapStudy.getId() +"_"+time+ ".csv";
		
		String temp = System.getProperty("java.io.tmpdir");
//		System.out.println("FILE TEMP: " + temp);
		
		File file = new File(temp + filename);
		String encoding = "ISO-8859-1";
		FileWriterWithEncoding writer = new FileWriterWithEncoding(file, encoding, false);
		
		Collections.sort(articles, new Comparator<Article>() {
			@Override
			public int compare(Article a1, Article a2){
				return a1.getId().compareTo(a2.getId());
			}
		});
		
		String delimiter = ";";
		
		//create header
		writer.append("Id"+delimiter);
		writer.append("Author"+delimiter);
		writer.append("Title"+delimiter);
	    writer.append("Journal"+delimiter);
//	    writer.append("Year"+delimiter);
//	    writer.append("Pages"+delimiter);
	    writer.append("Doi"+delimiter);
//	    writer.append("URL"+delimiter);
	    writer.append("DocType"+delimiter);
	    writer.append("Source"+delimiter);
//	    writer.append("Language"+delimiter);
//	    writer.append("Abstract"+delimiter);
//	    writer.append("Keywords");
	    writer.append('\n');
		
		for(Article a : articles){
			writer.append(a.getId()+delimiter);
			writer.append(a.getAuthor()+delimiter);
			writer.append(a.getTitle()+delimiter);
			writer.append((a.getJournal() != null ? a.getJournal() : "" ) +delimiter);
//		    writer.append(a.getJournal()+delimiter);
//			writer.append(a.getYear()+delimiter);
//			writer.append(a.getPages()+delimiter);
//		    writer.append(a.getDoi()+delimiter);
		    writer.append((a.getDoi() != null ? a.getDoi() : "" ) +delimiter);
//			writer.append(a.getUrl()+delimiter);
//		    writer.append(a.getDocType()+delimiter);
		    writer.append((a.getDocType() != null ? a.getDocType() : "" ) +delimiter);
		    writer.append(a.sourceView(a.getSource())+delimiter);
//			writer.append(a.getLanguage()+delimiter);
//			writer.append(a.getAbstrct()+delimiter);
//			writer.append(a.getKeywords());
		    writer.append('\n');
		}
		writer.flush();
	    writer.close();
		
		String contentType = "text/csv";
		return new FileDownload(file, contentType, filename);
	}
	
	//comparar as avaliações dos usuários
	@Path("/maps/{mapStudyId}/compare")
	@Get
	public void compareEvaluations(Long mapStudyId){
		MapStudy mapStudy = mapStudyDao.find(mapStudyId);
		User user = userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.members().contains(user), new SimpleMessage("user", "user.is.not.mapstudy"));
		validator.onErrorRedirectTo(this).list();
		
		Double percentEvaluatedDouble = mapStudy.percentEvaluatedDouble(articleDao, user);
		
		validator.check((percentEvaluatedDouble >= 100), new SimpleMessage("mapstudy", "mapstudy.evaluations.compare.undone"));
		validator.onErrorRedirectTo(this).list();
		
		List<Article> articles = articleDao.getArticlesToEvaluate(mapStudy);

		List<User> members = userDao.mapStudyUsers(mapStudy);
		Collections.sort(members, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2){
				return u1.getLogin().compareTo(u2.getLogin());
			}
		});
		
		//TODO mudar para name acima
		
		List<ArticleCompareVO> articlesCompare = new ArrayList<ArticleCompareVO>();
		List<ArticleCompareVO> articlesAcceptedCompare = new ArrayList<ArticleCompareVO>();
		
		for(Article a : articles){
			HashMap<User, Evaluation> evaluations = new HashMap<User, Evaluation>();
			boolean hasAccepted = false, all = true;
			for(User u : members){
				Evaluation evaluation = a.getEvaluation(u);
				evaluations.put(u, evaluation);
				if(evaluation != null) {
					if (evaluation.getEvaluationStatus().equals(EvaluationStatusEnum.ACCEPTED)){
						hasAccepted = true;
					}else{
						all = false;
					}
				}
			}
//TODO Seta se ceito ou recusado se todas as avaliações forem iguais			
			if (!hasAccepted){
				a.setFinalEvaluation(EvaluationStatusEnum.REJECTED);
			}else if (all){
				a.setFinalEvaluation(EvaluationStatusEnum.ACCEPTED);
			}	
			
			ArticleCompareVO acvo = new ArticleCompareVO(a, members, evaluations);
			articlesCompare.add(acvo);
			
			if(hasAccepted){
				articlesAcceptedCompare.add(acvo);
			}
		}
		
		result.include("mapStudy", mapStudy);
		result.include("members", members);
		result.include("articles", articlesCompare);
		result.include("articlesAccepted", articlesAcceptedCompare);
		result.include("evaluationStatus", EvaluationStatusEnum.values());
		
		if (members.size() > 1){
			result.include("kappa", FleissKappa.combineKappas(articlesCompare, members));
		}else{
			result.include("kappa", 100.0f);
		}		
	}
	
	@Path("/maps/finalEvaluate")
	@Post
	public void finalEvaluate(Long mapStudyId, Long articleId, EvaluationStatusEnum evaluation){
		Article article = articleDao.find(articleId);
		article.setFinalEvaluation(evaluation);
		articleDao.update(article);
		result.redirectTo(this).compareEvaluations(mapStudyId);
	}
	
	@Path("/maps/kappa")
	@Post
	public void calcKappa(Long mapStudyId, String usersIds){
		MapStudy mapStudy = mapStudyDao.find(mapStudyId);
		List<Article> articles = articleDao.getArticlesToEvaluate(mapStudy);
		List<User> users = new ArrayList<User>();
		HashMap<String, Object> retorno = new HashMap<String, Object>();
		
		if (usersIds != null){
			for(String s : usersIds.split(";")){
				users.add(userDao.find(Long.parseLong(s)));
			}			
		}		
		
		List<ArticleCompareVO> articlesCompare = new ArrayList<ArticleCompareVO>();
		
		for(Article a : articles){
			HashMap<User, Evaluation> evaluations = new HashMap<User, Evaluation>();
			
			for(User u : users){
				Evaluation evaluation = a.getEvaluation(u);
				evaluations.put(u, evaluation);
			}
			
			ArticleCompareVO acvo = new ArticleCompareVO(a, users, evaluations);
			articlesCompare.add(acvo);
		}
		
		retorno = FleissKappa.combineKappasMap(articlesCompare, users);
		
		result.use(Results.json()).from(retorno).serialize();
	}
	
	@Path("/maps/article/{articleId}/details")
	@Get
	public void articleDetail(Long articleId){
		Article article = articleDao.find(articleId);
		
		validator.check(article != null, new SimpleMessage("articule", "article.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		HashMap<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("id", article.getId());
		retorno.put("title", article.getTitle());
		retorno.put("abstract", article.getAbstrct());
		
		HashMap<String, Object> evaluations = new HashMap<String, Object>();
		for(Evaluation e : article.getEvaluations()){
			HashMap<String, Object> evaluation = new HashMap<String, Object>();
			String user = e.getUser().getName();
			String comment = e.getComment();
			
			List<String> criterias = new ArrayList<String>();
			if(e.getEvaluationStatus().equals(EvaluationStatusEnum.ACCEPTED)){
				for(InclusionCriteria c : e.getInclusionCriterias()){
					criterias.add(c.getDescription());
				}
			}else if(e.getEvaluationStatus().equals(EvaluationStatusEnum.REJECTED)){
				for(ExclusionCriteria c : e.getExclusionCriterias()){
					criterias.add(c.getDescription());
				}
			}
			
			evaluation.put("criterias", criterias);
			evaluation.put("comment", comment);
			
			evaluations.put(user, evaluation);
		}
		
		retorno.put("evaluations", evaluations);
		
		result.use(Results.json()).from(retorno).serialize();
	}
	
	@Get
	@Path("/maps/{id}/articles.json")
	public void articlesJson(Long id){
		MapStudy mapStudy = mapStudyDao.find(id);
		List<Article> articles = articleDao.getArticles(mapStudy);
		result.use(json()).indented().from(articles, "articles").serialize();
	}
	//TODO adicionar questoes de pesquisa etc.. para ser carregado ao iniciar metodo
	@Get
	@Path("/maps/{id}/planning")
	public void planning(Long id, String mydiv){
		MapStudy mapStudy = mapStudyDao.find(id);
		User user = userInfo.getUser();
		
		try {
			validator.check(mapStudy != null, new SimpleMessage("mapstudy", "mapstudy.is.not.exist"));
			
			if (!(mapStudy != null)) {
				validator.onErrorRedirectTo(this).list();
			}
			
			validator.check(mapStudy.members().contains(user), new SimpleMessage("user", "user.is.not.mapstudy"));
			if (!(mapStudy.members().contains(user))){
				validator.onErrorRedirectTo(this).list();
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		List<ArticleSourceEnum> sources = asList(ArticleSourceEnum.values());
		sources.get(0).getDescription();
//		sources.remove(ArticleSourceEnum.MANUALLY);
		
		if (mydiv == null){
			mydiv = "divgoals";
		}
		
		Set<Question> questions = new HashSet<>();
		
		if (mapStudy.getForm() != null){
			questions = mapStudy.getForm().getQuestions();
		}
		
		result.include("questionTypes", QuestionType.values());
		result.include("questions", questions);
		result.include("mydiv", mydiv);
		result.include("map", mapStudy);
		result.include("sources", sources);
	}
	
	@Get
	@Path("/maps/{id}/identification")
	public void identification(Long id){
		MapStudy mapStudy = mapStudyDao.find(id);
		User user = userInfo.getUser();

		validator.check(mapStudy != null, new SimpleMessage("mapstudy",	"mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();

		validator.check(mapStudy.members().contains(user), new SimpleMessage("user", "user.is.not.mapstudy"));
		validator.onErrorRedirectTo(this).list();
		
		List<ArticleSourceEnum> sources = asList(ArticleSourceEnum.values());
//		sources.remove(ArticleSourceEnum.MANUALLY);
				
		result.include("map", mapStudy);
		result.include("sources", sources);
	}
	
	@Post
	@Path("/maps/goals")
	public void addgoals(Long id, String goals){
		validator.onErrorForwardTo(this).planning(id, "divgoals");
		
		MapStudy mapStudy = mapStudyDao.find(id);
		User user = userInfo.getUser();
		
		validator.check(mapStudy != null, new SimpleMessage("mapstudy",	"mapstudy.is.not.exist"));
		validator.onErrorRedirectTo(this).list();
		
		validator.check(mapStudy.members().contains(user), new SimpleMessage("user", "user.is.not.mapstudy"));
		validator.onErrorRedirectTo(this).list();
		
		mapStudy.setGoals(goals);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "mapstudy.goals.add.success"));		
		result.redirectTo(this).planning(id, "divgoals"); 
		
//		result.use(Results.json()).indented().from(goals, "goals").serialize();
	}
	
	@Post("/maps/addquestion")
	public void addquestion(Long id, String description) {
		validator.onErrorRedirectTo(this).planning(id, "divquestion");
		
//		System.out.println("id: " + id + " desc: " + description);
			
		MapStudy mapStudy = mapStudyDao.find(id);
		
		ResearchQuestion researchQuestion = new ResearchQuestion();
		researchQuestion.setDescription(description);
		researchQuestion.setMapStudy(mapStudy);
		
		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
		validator.onErrorRedirectTo(this).planning(id, "divquestion");	
		
		validator.check(researchQuestion.getDescription() != null, new SimpleMessage("mapstudy.research.question.no.description", "error.not.null"));
		validator.onErrorRedirectTo(this).planning(id, "divquestion");
		
		questionDao.insert(researchQuestion);
		
		mapStudy.getResearchQuestions().add(researchQuestion);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "mapstudy.research.question.add.success"));
	    result.redirectTo(this).planning(id, "divquestion");
		
//		result.use(Results.json()).indented().from(researchQuestion, "question").serialize();
	}
	
	@Post("/maps/addstring")
	public void addstring(Long id, String string, ArticleSourceEnum source){
		MapStudy mapStudy = mapStudyDao.find(id);
		
		SearchString searchString = new SearchString();
		searchString.setDescription(string);
		searchString.setSource(source);
		searchString.setMapStudy(mapStudy);
		
//		validator.check(mapStudy.isCreator(userInfo.getUser()), new SimpleMessage("user", "user.is.not.creator"));
//		validator.onErrorRedirectTo(this).planning(id, "divstring");	
		
		validator.check(searchString.getDescription() != null, new SimpleMessage("mapstudy.search.string.no.description", "error.not.null"));
		validator.onErrorRedirectTo(this).planning(id, "divstring");
		
		stringDao.insert(searchString);
		
		mapStudy.getSearchString().add(searchString);
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "mapstudy.search.string.add.success"));
	    result.redirectTo(this).planning(id, "divstring");
		
//		result.use(Results.json()).indented().from(searchString, "string").serialize();
		
	}
	
	@Get("/maps/editstring")
	public void editstring(Long id, String string, ArticleSourceEnum source){
	}
	
	@Get("/maps/removestring")
	public void removestring(Long id, String string){
	}
	
	@Get("/maps/{mapid}/removequestion/{questid}")
	public void removequestion(Long mapid, Long questid){
		System.out.println(mapid + " - " + questid);
		MapStudy mapStudy = mapStudyDao.find(mapid);
		
		mapStudy.removeResearchQuestion(questid);
		
		mapStudyDao.update(mapStudy);
		
		result.include("notice", new SimpleMessage("mapstudy", "mapstudy.research.question.remove.success"));
	    result.redirectTo(this).planning(mapid, "divquestion");
	}
	
	@Get("/maps/editquestion/{questid}")
	public void editquestion(Long mapid, Long questid){
		
	}
	
	@Get("/maps/editinclusioncriteria/{criteriaid}")
	public void editinclusioncriteria(Long mapid, Long criteriaid){
		
	}
	
	@Get("/maps/editexclusioncriteria/{criteriaid}")
	public void editexclusioncriteria(Long mapid, Long criteriaid){
		
	}
	
	@Get("/maps/{mapid}/report")
	public void report(Long mapid){
		MapStudy map = mapStudyDao.find(mapid);
		Set<Question> questions = new HashSet<Question>();
		if (map.getForm() != null){
			questions = map.getForm().getQuestions();
		}
		String eixos[] = new String[2];
		
		eixos[0] = "Eixo X";
		eixos[1] = "Eixo Y";
		
		result.include("questions", questions);
		result.include("eixos", eixos);
		result.include("map", map);
	}
	
	@Get("/map")
	public void create(){
		
	}
	
	@Get("/home")
	public void home(){
		
	}
		
}
