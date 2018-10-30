/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  br.com.ufpi.systematicmap.dao.Dao
 *  br.com.ufpi.systematicmap.dao.EvaluationExtractionFinalDao
 *  br.com.ufpi.systematicmap.model.Article
 *  br.com.ufpi.systematicmap.model.EvaluationExtractionFinal
 *  br.com.ufpi.systematicmap.model.MapStudy
 *  javax.inject.Inject
 *  javax.persistence.EntityManager
 *  javax.persistence.TypedQuery
 */
package br.com.ufpi.systematicmap.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.EvaluationExtractionFinal;
import br.com.ufpi.systematicmap.model.MapStudy;

public class EvaluationExtractionFinalDao extends Dao<EvaluationExtractionFinal> {
		
    protected EvaluationExtractionFinalDao() {
        this(null);
    }

    @Inject
    public EvaluationExtractionFinalDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Article> getArticlesToExtractionFinal(MapStudy mapStudy) {
        List<Article> articles = this.entityManager.createQuery("select a from Article a where a.classification = null and a.mapStudy = :mapStudy AND a.removed = false order by a.title asc", Article.class)
        		.setParameter("mapStudy", mapStudy).getResultList();
        return articles;
    }
    
    public List<EvaluationExtractionFinal> getExtractionsFinal(Long q1, Long q2){
    	List<EvaluationExtractionFinal> extractions = this.entityManager.createQuery("select e from EvaluationExtractionFinal e where e.question.id :q1 or e.question.id :q2", EvaluationExtractionFinal.class)
        		.setParameter("q1", q1).setParameter("q2", q2).getResultList();
        return extractions;
    }

	public void removeAllExtractionsFinal(MapStudy mapStudy) {
		try {
			this.entityManager.getTransaction().begin();
			Query query = this.entityManager.createQuery("DELETE FROM EvaluationExtractionFinal eaf where eaf.mapStudy = :mapStudy");
			query.setParameter("mapStudy", mapStudy);
			int result = query.executeUpdate();
			this.entityManager.getTransaction().commit();
			
			System.out.println("MapStudy_id = " + mapStudy.getId() + ", Total = " + result);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}

