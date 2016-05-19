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

import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.EvaluationExtractionFinal;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.Question;

public class EvaluationExtractionFinalDao extends Dao<EvaluationExtractionFinal> {
		
    protected EvaluationExtractionFinalDao() {
        this(null);
    }

    @Inject
    public EvaluationExtractionFinalDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Article> getArticlesToExtractionFinal(MapStudy mapStudy) {
        List<Article> articles = this.entityManager.createQuery("select a from Article a where a.classification = null and a.mapStudy = :mapStudy order by a.title asc", Article.class)
        		.setParameter("mapStudy", mapStudy).getResultList();
        return articles;
    }
    
    public List<EvaluationExtractionFinal> getExtractionsFinal(Long q1, Long q2){
    	List<EvaluationExtractionFinal> extractions = this.entityManager.createQuery("select e from EvaluationExtractionFinal e where e.question.id :q1 or e.question.id :q2", EvaluationExtractionFinal.class)
        		.setParameter("q1", q1).setParameter("q2", q2).getResultList();
        return extractions;
    }
}

