/**
 * 
 */
package br.com.ufpi.systematicmap.learn.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ufpi.systematicmap.dao.Dao;
import br.com.ufpi.systematicmap.learn.model.EvaluationArticleAlgorithm;
import br.com.ufpi.systematicmap.learn.model.LearningAlgorithms;
import br.com.ufpi.systematicmap.model.MapStudy;

/**
 * @author Gleison Andrade
 *
 */
public class EvaluationArticleAlgorithmDao extends Dao<EvaluationArticleAlgorithm>{
	/**
	 * @deprecated CDI eyes only
	 */
	protected EvaluationArticleAlgorithmDao() {
		this(null);
	}

	@Inject
	public EvaluationArticleAlgorithmDao(EntityManager entityManager) {
		super(entityManager);
	}

	/**
	 * 
	 * SELECT DISTINCT() FROM 
	 * @param mapStudy
	 * @return
	 */
	public List<LearningAlgorithms> getAlgoritms(MapStudy mapStudy) {
		List<LearningAlgorithms> algoritms = new ArrayList<>();
		
		return algoritms;
	}
	
}