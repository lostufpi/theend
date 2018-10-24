/**
 * 
 */
package br.com.ufpi.systematicmap.learn.util;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufpi.systematicmap.learn.dao.EvaluationArticleAlgorithmDao;
import br.com.ufpi.systematicmap.learn.model.EvaluationArticleAlgorithm;
import br.com.ufpi.systematicmap.learn.model.LearningAlgorithms;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.Evaluation;
import br.com.ufpi.systematicmap.model.ExclusionCriteria;
import br.com.ufpi.systematicmap.model.InclusionCriteria;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;

/**
 * @author Gleison Andrade
 *
 */
@Named
@ApplicationScoped
public class EvaluationArticleAlgorithmUtil {
	@Inject EvaluationArticleAlgorithmDao evaluationArticleAlgorithmDao;
	
	public List<User> algorithmToMembers(MapStudy mapStudy){
		List<User> members = new ArrayList<>();
		
		for(LearningAlgorithms algorithm : evaluationArticleAlgorithmDao.getAlgoritms(mapStudy)){
			members.add(new User(algorithm.getName(), algorithm.toString()));
		}
		
		return members;
	}

	public Evaluation putLearningEvaluation(Article a, User u) {
		EvaluationArticleAlgorithm eaa = a.getEvaluationsAlgorithms(LearningAlgorithms.valueOf(u.getLogin()));
		
		Evaluation evaluation = new Evaluation("", u, a.getMapStudy(), a);		
		
		if(EvaluationStatusEnum.ACCEPTED.equals(eaa.getEvaluation())){
			evaluation.addInclusion(new InclusionCriteria("Aceito automáticamente"));
		}else if(EvaluationStatusEnum.REJECTED.equals(eaa.getEvaluation())){
			evaluation.addExclusion(new ExclusionCriteria("Rejeitado automáticamente"));
		}
		
		return evaluation;
	}

}
