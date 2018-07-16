/**
 * 
 */
package br.com.ufpi.systematicmap.controller;

import static br.com.caelum.vraptor.view.Results.json;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.dao.EvaluationDao;
import br.com.ufpi.systematicmap.dao.EvaluationExtractionFinalDao;
import br.com.ufpi.systematicmap.dao.MapStudyDao;
import br.com.ufpi.systematicmap.dao.QuestionDao;
import br.com.ufpi.systematicmap.dao.UserDao;
import br.com.ufpi.systematicmap.interceptor.Public;
import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.model.Alternative;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.Data;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Pie;
import br.com.ufpi.systematicmap.model.Question;
import br.com.ufpi.systematicmap.model.enums.ArticleSourceEnum;
import br.com.ufpi.systematicmap.model.enums.ClassificationEnum;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;
import br.com.ufpi.systematicmap.model.vo.Column;

/**
 * @author Gleison
 *
 */
@Controller
public class GraphicsController {
	private final UserInfo userInfo;
	private MapStudyDao mapStudyDao;
	private ArticleDao articleDao;
	private UserDao userDao;
	private EvaluationDao evaluationDao;
	private Result result;
	private EvaluationExtractionFinalDao extractionFinalDao;
	private QuestionDao questionDao;
	
	protected GraphicsController(){
		this(null, null, null, null, null, null, null, null);
	}

	@Inject
	public GraphicsController(UserInfo userInfo, MapStudyDao mapStudyDao,
			UserDao userDao, Result result, EvaluationDao evaluationDao, ArticleDao articleDao, EvaluationExtractionFinalDao extractionFinalDao, QuestionDao questionDao) {
		super();
		this.userInfo = userInfo;
		this.mapStudyDao = mapStudyDao;
		this.userDao = userDao;
		this.result = result;
		this.evaluationDao = evaluationDao;
		this.articleDao = articleDao;
		this.extractionFinalDao = extractionFinalDao;
		this.questionDao = questionDao;
	}


	@Get("/test")
	@Public
	public void test(){
		
	}

	@Get("/graphics/pie/sources")
	public void articlesSources(Long mapid){
//		System.out.println(mapid);
		MapStudy mapStudy = mapStudyDao.find(mapid);
		mapStudyDao.refresh(mapStudy);
		
		List<Article> articles = articleDao.getArticles(mapStudy);
		
		HashMap<String, Double> sources = new HashMap<>();
		List<ArticleSourceEnum> listSources = asList(ArticleSourceEnum.values());
		
		for (ArticleSourceEnum articleSourceEnum : listSources) {
			sources.put(articleSourceEnum.toString(), 0d);
		}
		
		for (Article article : articles) {
			Double value = sources.get(article.getSource().toString());
			++value;
			sources.put(article.getSource().toString(), value);
		}	
		
		int total = articles.size();
		
		Pie pie = new Pie();
		List<Data> data = new ArrayList<>();
		
		for (ArticleSourceEnum articleSourceEnum : listSources) {
			Data d = new Data();
			d.setName(articleSourceEnum.getDescription());
			d.setY(sources.get(articleSourceEnum.toString()));
			Double percent = (d.getY() / total) * 100;
			d.setPercent(percent);
			data.add(d);
		}		
		
		pie.setTitle("Artigos obtidos por base de busca");
		pie.setColorByPoint(true);
		pie.setName("Artigos");
		pie.setData(data);		
		
//		System.out.println("total: " + total + " pie: " + pie);
		
		result.use(json()).indented().withoutRoot().from(pie).recursive().serialize();
	}

//	@Get("/graphics/pie/evaluates")
//	public void articlesEvaluates(Long mapid){
//		MapStudy mapStudy = mapStudyDao.find(mapid);
//		mapStudyDao.refresh(mapStudy);
//		User user = userInfo.getUser();
//		
////		Set<Article> articles = mapStudy.getArticles();
//		List<Evaluation> evaluations = evaluationDao.getEvaluations(user, mapStudy);
//		
//		HashMap<String, Double> sources = new HashMap<>();
//		List<EvaluationStatusEnum> listEvaluate = asList(EvaluationStatusEnum.values());
//		
//		for (EvaluationStatusEnum evaluationStatusEnum : listEvaluate) {
//			sources.put(evaluationStatusEnum.toString(), 0d);
//		}
//		
//		HashMap<String, Double> acceptedMap = new HashMap<String, Double>();
//		HashMap<String, Double> rejectedMap = new HashMap<String, Double>();
//		HashMap<String, Double> noEvaluate = new HashMap<String, Double>();
//		
//		for(Evaluation e : evaluations){
//			String criteriaType = e.getEvaluationStatus().toString();
//			Double value = sources.get(criteriaType);
//			++value;
//			sources.put(criteriaType, value);
//			
//			if ("ACCEPTED".equals(criteriaType)){
//				for(InclusionCriteria i : e.getInclusionCriterias()){
//					if(acceptedMap.containsKey(i.getId().toString())){
//						Double cont = acceptedMap.get(i.getId().toString());
//						++cont;
//						acceptedMap.put(i.getId().toString(), cont);
//					}else{
//						acceptedMap.put(i.getId().toString(), (double) 1);
//					}					
//				}
//				
//			}else if ("REJECTED".equals(criteriaType)){
//				for(ExclusionCriteria i : e.getExclusionCriterias()){
//					if(rejectedMap.containsKey(i.getId().toString())){
//						Double cont = rejectedMap.get(i.getId().toString());
//						++cont;
//						rejectedMap.put(i.getId().toString(), cont);
//					}else{
//						rejectedMap.put(i.getId().toString(), (double) 1);
//					}					
//				}
//			}			
//		}
//		
//		Double count = sources.get("ACCEPTED") + sources.get("REJECTED");
//		sources.put("NOT_EVALUATED", count);
//		
//		int total = articles.size();
//		
//		System.out.println("total: " + total);
//		
//		Pie pie = new Pie();
//		List<Data> data = new ArrayList<>();
//		List<Series> series = new ArrayList<Series>();
//		
//		for (EvaluationStatusEnum evaluationStatusEnum : listEvaluate) {
//			Data d = new Data();
//			d.setName(evaluationStatusEnum.getDescription());
//			
//			if (d.getName().equals("ACCEPTED")){
//				Series serie = new Series();
//				serie.setData(acceptedMap);
//				serie.setName(d.getName());
//				serie.setId(d.getName());				
//				d.setDrilldown(serie.getId());
//			}else if(d.getName().equals("REJECTED")){
//				Series serie = new Series();
//				serie.setData(rejectedMap);
//				serie.setName(d.getName());
//				serie.setId(d.getName());				
//				d.setDrilldown(serie.getId());
//			}else{
//				Series serie = new Series();
//				serie.setData(noEvaluate);
//				serie.setName(d.getName());
//				serie.setId(d.getName());				
//				d.setDrilldown(serie.getId());
//			}
//			
//			d.setY(sources.get(evaluationStatusEnum.toString()));
//			Double percent = (d.getY() / total) * 100;
//			d.setPercent(percent);
//			data.add(d);
//			System.out.println();
//			System.out.println(d);
//		}	
//		
//		pie.setTitle("Avaliação dos artigos");
//		pie.setColorByPoint(true);
//		pie.setName("Artigos");
//		pie.setData(data);		
//		
//		System.out.println("total: " + total + " pie: " + pie);
//		
//		result.use(json()).indented().withoutRoot().from(pie).recursive().serialize();
//	}
	
	@Get("/graphics/pie/evaluates")
	public void articlesEvaluates(Long mapid){
		MapStudy mapStudy = mapStudyDao.find(mapid);
		mapStudyDao.refresh(mapStudy);
//		User user = userInfo.getUser();
		
		List<Article> articles = articleDao.getArticlesToEvaluate(mapStudy);
		
		HashMap<String, Double> sources = new HashMap<>();
		List<EvaluationStatusEnum> listEvaluate = asList(EvaluationStatusEnum.values());
		
		for (EvaluationStatusEnum evaluationStatusEnum : listEvaluate) {
			sources.put(evaluationStatusEnum.toString(), 0d);
		}
		
		for (Article article : articles) {
			String evaluation = article.getFinalEvaluation() != null ? article.getFinalEvaluation().toString() : EvaluationStatusEnum.NOT_EVALUATED.toString(); //TODO: revisar 
			Double value = sources.get(evaluation);
			++value;
			sources.put(evaluation, value);
		}	
		
		int total = articles.size();
		
		Pie pie = new Pie();
		List<Data> data = new ArrayList<>();
		
		for (EvaluationStatusEnum eval : listEvaluate) {
			Data d = new Data();
			d.setName(eval.getDescription());
			d.setY(sources.get(eval.toString()));
			Double percent = (d.getY() / total) * 100;
			d.setPercent(percent);
			data.add(d);
		}		
		
		pie.setTitle("Avaliação final dos artigos");
		pie.setColorByPoint(true);
		pie.setName("Artigos");
		pie.setData(data);		
		
		result.use(json()).indented().withoutRoot().from(pie).recursive().serialize();
	}
	
	@Get("/graphics/pie/refine")
	public void articlesRefine(Long mapid){
//		System.out.println(mapid);
		MapStudy mapStudy = mapStudyDao.find(mapid);
		mapStudyDao.refresh(mapStudy);
		
		List<Article> articles = articleDao.getArticles(mapStudy);
		
		HashMap<String, Double> sources = new HashMap<>();
		List<ClassificationEnum> listRefines = asList(ClassificationEnum.values());
		
		for (ClassificationEnum classificationEnum : listRefines) {
			sources.put(classificationEnum.toString(), 0d);
		}
		
		sources.put("NO_CLASSIFICATION", (double) 0);
		
		for (Article article : articles) {
			if (article.getClassification() != null){
				Double value = sources.get(article.getClassification().toString());
				++value;
				sources.put(article.getClassification().toString(), value);
			}else{
				Double value = sources.get("NO_CLASSIFICATION");
				++value;
				sources.put("NO_CLASSIFICATION", value);
			}
		}	
		int total = articles.size();
		
		Pie pie = new Pie();
		List<Data> data = new ArrayList<>();
		
		for (ClassificationEnum articleSourceEnum : listRefines) {
			Data d = new Data();
			d.setName(articleSourceEnum.getDescription());
			d.setY(sources.get(articleSourceEnum.toString()));
			Double percent = (d.getY() / total) * 100;
			d.setPercent(percent);
			data.add(d);
		}		
		
		Data d = new Data();
		d.setName("Não Refinados");
		d.setY(sources.get("NO_CLASSIFICATION"));
		Double percent = (d.getY() / total) * 100;
		d.setPercent(percent);
		data.add(d);
		
		pie.setTitle("Artigos Refinados");
		pie.setColorByPoint(true);
		pie.setName("Artigos");
		pie.setData(data);		
		
//		System.out.println("total: " + total + " pie: " + pie);
		
		result.use(json()).indented().withoutRoot().from(pie).recursive().serialize();
	}
	
	/**
	 * @param mapStudy eixo x
	 * @param question1 eixo y
	 * @param question2
	 * @return
	 */
	private HashMap<String, HashMap<String, Long>> alternativesHash(MapStudy mapStudy, Question question1, Question question2){
//		MapStudy mapStudy = mapStudyDao.find(question2);
//		List<EvaluationExtractionFinal> extractions = extractionFinalDao.getExtractionsFinal(q1, q2);
		List<Article> articles = articleDao.getArticlesFinalExtraction(mapStudy);
		
//		System.out.println("hash : " + articles.size());
		
		HashMap<String, HashMap<String, Long>> map = new HashMap<String, HashMap<String, Long>>();
		List<Alternative> alternativeX = questionDao.getAlternativesFinalExtraction(question1, mapStudy);
		List<Alternative> alternativeY = questionDao.getAlternativesFinalExtraction(question2, mapStudy);
		
		for (Alternative x : alternativeX) {
			HashMap<String, Long> v = new HashMap<String, Long>();
			map.put(x.getValue(), v);
			
			for (Alternative y : alternativeY) {
				Long count = 0l;
				for (Article a : articles){
					count += a.alternativesCount(x, y);
				}
				
//				System.out.println("count: " + count);
				
				map.get(x.getValue()).put(y.getValue(), count);
			}
		}
		
//		for (Article a : articles) {
//			for (EvaluationExtractionFinal eef: a.getEvaluationExtractionsFinal()){
//				HashMap<String, Long> v = map.get(eef.getAlternative().getValue());
//				if (v != null){
//					v.get(key)
//				}
//			}
//		}	
		
		return map;
	}
	
	@Get("/graphics/bubble")
	public void bubble(Long mapid, Long q1, Long q2) throws JSONException{
		List<JSONObject> data = new ArrayList<JSONObject>();
		
		MapStudy mapStudy = mapStudyDao.find(mapid);
		if (q1 != -1 && q2 != -1){
			
			Question question1 = questionDao.find(q1),  question2 = questionDao.find(q2);
			
			HashMap<String, HashMap<String, Long>> map = alternativesHash(mapStudy, question1, question2);
			
			for(Map.Entry<String, HashMap<String, Long>> m : map.entrySet()){
				for (Map.Entry<String, Long> v : m.getValue().entrySet()) {
					JSONObject my_obj = new JSONObject();
					
					my_obj.put("q1", question1.getName());
					my_obj.put("q2", question2.getName());
					
					my_obj.put(question1.getName(), m.getKey());
					my_obj.put(question2.getName(), v.getKey());
					
					my_obj.put("qnt", v.getValue());
					
					data.add(my_obj);
					
//					System.out.println(my_obj);
				}			
			}	
		}
		
		result.use(json()).withoutRoot().from(data.toString()).serialize();
	}
	
	@Get("/graphics/column/year")
	public void articlesYear(Long mapid){
		MapStudy mapStudy = mapStudyDao.find(mapid);
		mapStudyDao.refresh(mapStudy);
//		User user = userInfo.getUser();
		
		List<Article> articles = articleDao.getArticlesFinalExtraction(mapStudy);
		
		HashMap<Integer, Double> sources = new HashMap<>();		
//		Random rand = new Random();
		
		for (Article article : articles) {
//		for (int i = 0; i < 10; i++) {
			Integer year = article.getYear();
//			Integer year = rand.nextInt(1000);
			
			if (year == null){
				year = -1;
			}
			
			Double value = 0d;
			
			if (sources.containsKey(year)){
				value = sources.get(year);
			}
			
			++value;
			sources.put(year, value);
		}	
		
		Column column = new Column();
		column.setTitle("Publicações por ano");
		column.setSubTitle("Mostra uma visão da quantidade de publicações por ano de acordo com artigos que foram para extração final");
		column.setName("Artigos");
		column.setyAxis("Quantidade de Publicações");
		
		List<Entry<Integer, Double>> entries = new ArrayList<Entry<Integer, Double>>(sources.entrySet());
				Collections.sort(entries, new Comparator<Map.Entry<Integer, Double>>() {
				  public int compare(Map.Entry<Integer, Double> a, Map.Entry<Integer, Double> b){
				    return a.getKey().compareTo(b.getKey());
				  }
				});
				
				Set<Entry<Integer, Double>> sortedMap = new LinkedHashSet<Entry<Integer, Double>>(entries);

		
		for(Map.Entry<Integer, Double> m : sortedMap){
			column.getCategories().add(m.getKey().toString());
			column.getData().add(m.getValue());
		}
		
		result.use(json()).indented().withoutRoot().from(column).recursive().serialize();
	}
	
	@Get("/map/{mapid}/graphics/bubble/{q1}/{q2}")
	public void bubbleNew(Long mapid, Long q1, Long q2){
		result.include("mapid", mapid);
		result.include("q1", q1);
		result.include("q2", q2);
	}
	

}
