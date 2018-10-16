/**
 * 
 */
package br.com.ufpi.systematicmap.learn.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.gson.WithoutRoot;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.com.ufpi.systematicmap.controller.MapStudyController;
import br.com.ufpi.systematicmap.controller.MessagesController;
import br.com.ufpi.systematicmap.dao.MapStudyDao;
import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.learn.dao.LearningConfigurationDao;
import br.com.ufpi.systematicmap.learn.dao.LearningStatsDao;
import br.com.ufpi.systematicmap.learn.model.LearningAlgorithms;
import br.com.ufpi.systematicmap.learn.model.LearningConfiguration;
import br.com.ufpi.systematicmap.learn.model.LearningStats;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.enums.TypeMessage;

/**
 * @author Gleison Andrade
 *
 */
@Controller
public class AutomaticSelectionController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Result result;
	private Validator validator;
	private UserInfo userInfo;
	private Logger logger;
	
	private MapStudyDao mapStudyDao;
	private LearningConfigurationDao configurationDao;
	private LearningStatsDao statsDao;

	@Deprecated
	protected AutomaticSelectionController() {
		this(null, null, null, null, null, null, null);
	}

	@Inject
	public AutomaticSelectionController(Result result, Validator validator, UserInfo userInfo, Logger logger, MapStudyDao mapStudyDao, LearningConfigurationDao configurationDao, LearningStatsDao statsDao) {
		this.result = result;
		this.validator = validator;
		this.userInfo = userInfo;
		this.logger = logger;
		this.mapStudyDao = mapStudyDao;
		this.configurationDao = configurationDao;
		this.statsDao = statsDao;
	}
	
	@Post
	@Path("/maps/learningconfiguration")
	@Consumes(value = "application/json")//, options = WithoutRoot.class)
	public void addconfiguration(Long mapid, String algorithm, LearningConfiguration learningConfiguration) {
		System.out.println(mapid + " " + algorithm + " " + learningConfiguration);
		MapStudy mapStudy = mapStudyDao.find(mapid);
		LearningConfiguration lcMap = configurationDao.findLearningConfiguration(mapid);
		
		if(lcMap == null){
			lcMap = new LearningConfiguration();
		}

		lcMap.setAlgorithm(LearningAlgorithms.valueOf(algorithm));
		lcMap.setMapStudy(mapStudy);
		lcMap.setNumberArtclesValidation(learningConfiguration.getNumberArtclesValidation());
		lcMap.setShowSelection(learningConfiguration.getShowSelection());
		lcMap.setUseResearcher(learningConfiguration.getUseResearcher());
		
		configurationDao.update(lcMap);
		
		LearningStats learningStats = statsDao.findLearningStats(mapid);
		
		if(learningStats == null){
			learningStats = new LearningStats(mapStudy, LearningAlgorithms.valueOf(algorithm));
		}else{
			learningStats.resetInfos(LearningAlgorithms.valueOf(algorithm));
		}
		
		statsDao.update(learningStats);

//		MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.learningConfiguration.add.success", TypeMessage.SUCCESS));
		result.use(Results.json()).from(lcMap).serialize();
//		result.redirectTo(MapStudyController.class).planning(mapid, "divautomaticselection");
	}
	
	@Post
	@Path("/maps/calculateStats")
	@Consumes("application/json")
	public void calculateStats(Long mapid, LearningAlgorithms algorithm) {
		System.out.println(mapid + " " + algorithm);
		MapStudy mapStudy = mapStudyDao.find(mapid);
		LearningStats learningStats = calculateMediam(mapStudy, algorithm);
		learningStats.setAlgorithm(algorithm);
		learningStats.setMapStudy(mapStudy);

//		MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.learningStats.load.success", TypeMessage.SUCCESS));
		result.use(Results.json()).from(learningStats).serialize();
	}

	private LearningStats calculateMediam(MapStudy mapStudy, LearningAlgorithms algorithm) {
		List<LearningStats> listStats = statsDao.find(mapStudy, algorithm);
		LearningStats learningStats = new LearningStats();
		
		Integer count = new Integer(listStats.size());
		Integer sumNumberarticles = new Integer(0);
		Integer sumNumberArticlesAccepted = new Integer(0);
		Integer sumNumberArticlesRejected = new Integer(0);
		Double sumWss = new Double(0);
		Double sumRecal = new Double(0);
		Double sumAccuracy = new Double(0);
		Double sumError = new Double(0);
		Double sumRocArea = new Double(0);
		Double sumfMeasure = new Double(0);
		Integer sumNumberArticlesTraining = new Integer(0);
		
		for (LearningStats stats : listStats) {
			sumNumberarticles += stats.getNumberArticles();
			sumNumberArticlesAccepted += stats.getNumberArticlesAccepted();
			sumNumberArticlesRejected += stats.getNumberArticlesRejected();
			sumWss += stats.getWss();
			sumRecal += stats.getRecall();
			sumAccuracy += stats.getAccuracy();
			sumError += stats.getError();
			sumRocArea += stats.getRocArea();
			sumfMeasure += stats.getfMeasure();
			sumNumberArticlesTraining += stats.getNumberArticlesTraining();
		}
		
		learningStats.setNumberArticles(sumNumberarticles != 0 ?  sumNumberarticles / count : 0);
		learningStats.setNumberArticlesAccepted(sumNumberArticlesAccepted != 0 ?  sumNumberArticlesAccepted / count : 0);
		learningStats.setNumberArticlesRejected(sumNumberArticlesRejected != 0 ? sumNumberArticlesRejected / count : 0);
		learningStats.setWss(sumWss != 0 ? sumWss / count : 0.0);
		learningStats.setRecall(sumRecal != 0 ? sumRecal / count : 0.0);
		learningStats.setAccuracy(sumAccuracy != 0 ? sumAccuracy / count : 0.0);
		learningStats.setError(sumError != 0 ? sumError / count : 0.0);
		learningStats.setRocArea(sumRocArea != 0 ? sumRocArea / count : 0.0);
		learningStats.setfMeasure(sumfMeasure != 0 ? sumfMeasure / count : 0.0);
		learningStats.setNumberArticlesTraining(sumNumberArticlesTraining != 0 ? sumNumberArticlesTraining / count : 0); 
		
		
		return learningStats;
	}
	
//	@Post
//	@Path("/maps/goals")
//	public void addstats(Long id, LearningAlgorithms algorithm, LearningStats learningStats) {
//
//		MapStudy mapStudy = mapStudyDao.find(id);
//
//		learningStats.setAlgorithm(algorithm);
//		learningStats.setMapStudy(mapStudy);
//		
//		statsDao.insert(learningStats);
//
//		MessagesController.addMessage(new Mensagem("mapstudy", "mapstudy.learningStats.add.success", TypeMessage.SUCCESS));
//		result.redirectTo(MapStudyController.class).planning(id, "divautomaticselection");
//
//	}
	

}
