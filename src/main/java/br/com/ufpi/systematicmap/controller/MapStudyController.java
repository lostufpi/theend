package br.com.ufpi.systematicmap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.ufpi.systematicmap.components.FilterArticles;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.dao.EvaluationDao;
import br.com.ufpi.systematicmap.dao.ExclusionCriteriaDao;
import br.com.ufpi.systematicmap.dao.InclusionCriteriaDao;
import br.com.ufpi.systematicmap.dao.MapStudyDao;
import br.com.ufpi.systematicmap.dao.UserDao;
import br.com.ufpi.systematicmap.files.FilesUtils;
import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.Evaluation;
import br.com.ufpi.systematicmap.model.ExclusionCriteria;
import br.com.ufpi.systematicmap.model.InclusionCriteria;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.ArticleSourceEnum;
import br.com.ufpi.systematicmap.utils.BibtexToArticleUtils;
import br.com.ufpi.systematicmap.utils.BibtexUtils;

@Controller
public class MapStudyController {

	private final Result result;
	private final Validator validator;
	private final UserInfo userInfo;
	private final FilesUtils files;
	
	private final MapStudyDao mapStudyDao;
	private final UserDao userDao;
	private final ArticleDao articleDao;
	private final InclusionCriteriaDao inclusionDao;
	private final ExclusionCriteriaDao exclusionDao;
	private final EvaluationDao evaluationDao;
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected MapStudyController() {
		this(null, null, null, null, null, null, null, null, null, null);
	}


	@Inject
	public MapStudyController(MapStudyDao musicDao, UserInfo userInfo, 
				Result result, Validator validator, FilesUtils files, 
				UserDao userDao, ArticleDao articleDao, 
				InclusionCriteriaDao inclusionDao,
				ExclusionCriteriaDao exclusionDao,
				EvaluationDao evaluationDao) {
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
	}
	
	
	@Get("/maps")
	public void list() {
		result.include("users", this.userDao.findAll());
	}


	@Post("/maps")
	public void add(final @NotNull @Valid MapStudy mapstudy, List<Integer> members) {
		validator.onErrorForwardTo(this).list();
		
		
		
		User user = userInfo.getUser();
		userDao.refresh(user);
		user.add(mapstudy);
		mapstudy.addUser(user);
		
		if(members != null){
			for(Integer id : members){
				if(id.longValue() != user.getId()){
					User member = userDao.find(id.longValue());
					member.add(mapstudy);
					mapstudy.addUser(member);
				}
			}
		}
		
		mapStudyDao.add(mapstudy);
		
		result.redirectTo(this).list();
	}

	
	@Get("/maps/{id}")
	public void show(Long id) {
		validator.onErrorForwardTo(this).list();
		
		MapStudy mapStudy = mapStudyDao.find(id);
		List<User> mapStudyUsers = userDao.mapStudyUsers(mapStudy);
		List<User> mapStudyArentUsers = userDao.mapStudyArentUsers(mapStudy);
		
	    result.include("map", mapStudy);
	    result.include("sources", ArticleSourceEnum.values());
	    result.include("percentEvaluated", mapStudy.percentEvaluated(
	    		articleDao.countArticleNotRefined(mapStudy).intValue(),
	    		articleDao.countArticleToEvaluate(userInfo.getUser(), mapStudy).intValue())
	    );
	    result.include("mapStudyUsers", mapStudyUsers);
	    result.include("mapStudyArentUsers", mapStudyArentUsers);
	}
	
	@Post("/maps/addmember")
	public void addmember(Long id, Long userId){
		validator.onErrorForwardTo(this).show(id);
		
		MapStudy mapStudy = mapStudyDao.find(id);
		User user = userDao.find(userId);
		mapStudy.addUser(user);
		
		mapStudyDao.update(mapStudy);
		userDao.update(user);
		
		result.redirectTo(this).show(id);
	}
	
	@Post("/maps/addarticles")
	public void addarticles(Long id, UploadedFile upFile, ArticleSourceEnum source) throws IOException, ParseException {
		validator.onErrorForwardTo(this).show(id);
		
		MapStudy mapStudy = mapStudyDao.find(id);
		
		files.save(upFile, mapStudy);
		
		BibTeXDatabase database = BibtexUtils.parseBibTeX(files.getFile(mapStudy));
	    Map<Key, BibTeXEntry> entryMap = database.getEntries();
	    Collection<BibTeXEntry> entries = entryMap.values();
	    
		for(BibTeXEntry entry : entries){
			Article a = BibtexToArticleUtils.bibtexToArticle(entry, source);
			mapStudy.addArticle(a);
			articleDao.insert(a);
		}
		
		mapStudyDao.update(mapStudy);
		
	    result.redirectTo(this).show(id);
	}
	
	@Post("/maps/addinclusion")
	public void addinclusion(Long id, InclusionCriteria criteria) {
		validator.onErrorForwardTo(this).show(id);
		
		MapStudy mapStudy = mapStudyDao.find(id);
		mapStudy.addInclusionCriteria(criteria);
		inclusionDao.insert(criteria);
		mapStudyDao.update(mapStudy);
		
	    result.redirectTo(this).show(id);
	}
	
	@Post("/maps/addexclusion")
	public void addexclusion(Long id, ExclusionCriteria criteria) {
		validator.onErrorForwardTo(this).show(id);
		
		MapStudy mapStudy = mapStudyDao.find(id);
		mapStudy.addExclusionCriteria(criteria);
		exclusionDao.insert(criteria);
		mapStudyDao.update(mapStudy);
		
	    result.redirectTo(this).show(id);
	}
	
	@Post("/maps/refinearticles")
	public void refinearticles(Long id, Integer levenshtein, String regex, 
			Integer limiartitulo, Integer limiarabstract, Integer limiarkeywords, Integer limiartotal){
		
		MapStudy mapStudy = mapStudyDao.find(id);
		FilterArticles filter = new FilterArticles(mapStudy.getArticles(), levenshtein, regex.trim(), limiartitulo, limiarabstract, limiarkeywords, limiartotal);
		filter.filter();
		
		validator.onErrorForwardTo(this).show(id);
		result.redirectTo(this).show(id);
	}
	
	@Get("/maps/evaluate/{mapid}")
	public void evaluate(Long mapid) {
		result.redirectTo(this).evaluateArticle(mapid, 0l);
	}
	
	@Get("/maps/evaluate/{mapid}/article/{articleid}")
	public void evaluateArticle(Long mapid, Long articleid) {
		MapStudy mapStudy = mapStudyDao.find(mapid);
		
		validator.check((mapStudy.getArticles().size() > 0), 
				new SimpleMessage("mapstudy", "mapstudy.evaluate.articles.none"));
		
		validator.onErrorForwardTo(this).show(mapid);
		
		List<Article> articlesToEvaluate = articleDao.getArticlesToEvaluate(userInfo.getUser(), mapStudy);
		List<Evaluation> evaluations = evaluationDao.getEvaluations(userInfo.getUser(), mapStudy);
		
		validator.check((articlesToEvaluate.size() > 0), 
				new SimpleMessage("mapstudy", "mapstudy.evaluate.articles.complete"));
		
		Article article = null, nextArticle = null;
		Long nextArticleId = null;
		if(articleid != 0){
			article = articleDao.find(articleid);
		}else{
			article = getNextToEvaluate(articlesToEvaluate, null);
			articleid = article.getId();
		}
		
		if(article != null){
			nextArticle = getNextToEvaluate(articlesToEvaluate, articleid);
			if(nextArticle != null){
				nextArticleId = nextArticle.getId();
			}
		}
		
		validator.check((article != null), 
				new SimpleMessage("mapstudy", "mapstudy.evaluate.articles.none"));
		
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
		
		
		
		result.include("map", mapStudy);
		result.include("exclusionOrdered", exclusionOrdered);
		result.include("inclusionOrdered", inclusionOrdered);
		
		result.include("article", article);
		result.include("nextArticleId", nextArticleId);
		result.include("articlesToEvaluate", articlesToEvaluate);
		result.include("percentEvaluated", mapStudy.percentEvaluated(
				articleDao.countArticleNotRefined(mapStudy).intValue(), 
				articlesToEvaluate.size())
		);
		result.include("evaluations", evaluations);
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
	
	@Post("/maps/includearticle")
	public void includearticle(Long mapid, Long articleid, Long evaluationid, Long nextArticleId, List<Long> inclusions, String comment){
		doEvaluate(mapid, articleid, evaluationid, inclusions, comment, true);
		nextArticleId = nextArticleId != null ? nextArticleId : 0l;
		result.redirectTo(this).evaluateArticle(mapid, nextArticleId);
	}
	
	@Post("/maps/excludearticle")
	public void excludearticle(Long mapid, Long articleid, Long evaluationid, Long nextArticleId, List<Long> exclusions, String comment){
		doEvaluate(mapid, articleid, evaluationid, exclusions, comment, false);
		nextArticleId = nextArticleId != null ? nextArticleId : 0l;
		result.redirectTo(this).evaluateArticle(mapid, nextArticleId);
	}
	
	private void doEvaluate(Long mapid, Long articleid, Long evaluationid, List<Long> ids, String comment, boolean include){
		validator.check((ids != null), 
				new SimpleMessage("mapstudy", "mapstudy.evaluate.criterias.none"));
		validator.onErrorRedirectTo(this).evaluateArticle(mapid, articleid);
		
		MapStudy mapStudy = mapStudyDao.find(mapid);
		Article article = articleDao.find(articleid);
		
		if(evaluationid != null){
			evaluationDao.delete(evaluationid);
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
		validator.onErrorForwardTo(this).show(studyMapId);
		
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
		validator.onErrorForwardTo(this).show(studyMapId);
		

//com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`systematicmap`.`evaluations_exclusions`, CONSTRAINT `FK_5rm03fhb8j4nuj61k7cn9dcoq` FOREIGN KEY (`exclusion_id`) REFERENCES `ExclusionCriteria` (`id`))
		
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
		
		result.redirectTo(this).show(studyMapId);
	}
	
	
	/*
	 * , UploadedFile file
	validator.onErrorForwardTo(UsersController.class).home();

	mapStudyDao.add(map);
	
	User currentUser = userInfo.getUser();
	userDao.refresh(currentUser);
	
	currentUser.add(map);
	
	// is there a file?
	if (file != null) {
	    // Let's save the file
		musics.save(file, map);
		logger.info("Uploaded file: {}", file.getFileName());
	}

	// you can add objects to result even in redirects. Added objects will
	// survive one more request when redirecting.
	result.include("notice", map.getTitle() + " music added");

	result.redirectTo(UsersController.class).home();
	*/
	
	
	/*
	 
	@Path("/maps/download/{id}")
	@Get
	public Download download(Long id) throws FileNotFoundException {
		MapStudy music = mapStudyDao.find(id);
		File file = musics.getFile(music);
		String contentType = "audio/mpeg";
		String filename = music.getTitle() + ".mp3";

		return new FileDownload(file, contentType, filename);
	}
	
	 * Show all list of registered musics in json format
	@Public @Path("/maps/list/json")
	public void showAllMusicsAsJSON() {
		result.use(json()).from(mapStudyDao.findAll()).serialize();
	}
	@Public @Path("/maps/list/xml")
	public void showAllMusicsAsXML() {
		result.use(xml()).from(mapStudyDao.findAll()).serialize();
	}
	@Public @Path("/maps/list/http")
	public void showAllMusicsAsHTTP() {
		result.use(http()).body("<p class=\"content\">"+
			mapStudyDao.findAll().toString()+"</p>");
	}

	@Public @Path("/maps/list/form")
	public void listForm() {}
	
	@Public @Path("maps/listAs")
	public void listAs() {
		result.use(representation())
			.from(mapStudyDao.findAll()).serialize();
	}
	*/
}
