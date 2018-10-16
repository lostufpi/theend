/**
 * 
 */
package br.com.ufpi.systematicmap.learn.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ufpi.systematicmap.dao.Dao;
import br.com.ufpi.systematicmap.learn.model.LearningConfiguration;
import br.com.ufpi.systematicmap.learn.model.LearningStats;

/**
 * @author Gleison Andrade
 *
 */
public class LearningConfigurationDao extends Dao<LearningConfiguration>{
	/**
	 * @deprecated CDI eyes only
	 */
	protected LearningConfigurationDao() {
		this(null);
	}

	@Inject
	public LearningConfigurationDao(EntityManager entityManager) {
		super(entityManager);
	}
	
	public LearningConfiguration findLearningConfiguration(Long id){
		LearningConfiguration learningConfiguration = null;
		
		try {
			learningConfiguration = entityManager.createQuery("select lc from LearningConfiguration lc where lc.mapStudy.id =:id", LearningConfiguration.class).setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return learningConfiguration;
	}
	
	

}