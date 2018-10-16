/**
 * 
 */
package br.com.ufpi.systematicmap.learn.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ufpi.systematicmap.dao.Dao;
import br.com.ufpi.systematicmap.learn.model.LearningAlgorithms;
import br.com.ufpi.systematicmap.learn.model.LearningStats;
import br.com.ufpi.systematicmap.model.MapStudy;

/**
 * @author Gleison Andrade
 *
 */
public class LearningStatsDao extends Dao<LearningStats>{
	/**
	 * @deprecated CDI eyes only
	 */
	protected LearningStatsDao() {
		this(null);
	}

	@Inject
	public LearningStatsDao(EntityManager entityManager) {
		super(entityManager);
	}

	public List<LearningStats> find(MapStudy mapStudy, LearningAlgorithms algorithm) {
		List<LearningStats> listStats = new ArrayList<>();
		
		try {
			listStats = entityManager.createQuery("select ls from LearningStats ls where ls.mapStudy.id =:mapid and ls.algorithm = :algorithm", LearningStats.class).setParameter("mapid", mapStudy.getId()).setParameter("algorithm", algorithm).getResultList();
		} catch (Exception e) {
			e.getMessage();
		}
		
		return listStats;
	}
	
	public LearningStats findLearningStats(Long id){
		LearningStats learningStats = null;
		try {
			learningStats = entityManager.createQuery("select ls from LearningStats ls where ls.mapStudy.id =:id", LearningStats.class).setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return learningStats;
	}

}