package br.com.ufpi.systematicmap.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;

/**
 * @author Gleison Andrade
 *
 */
@RequestScoped
public class ArticleDao extends Dao<Article> {
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected ArticleDao() {
		this(null);
	}

	@Inject
	public ArticleDao(EntityManager entityManager) {
		super(entityManager);
	}

	public List<Article> getArticles(MapStudy mapStudy){
		List<Article> articles = entityManager
			.createQuery("select a from Article a where a.mapStudy = :mapStudy AND a.removed = false order by a.title asc", Article.class)
				.setParameter("mapStudy", mapStudy)
				.getResultList();
		return articles;
	}
	
	public List<Article> getArticlesToEvaluate(MapStudy mapStudy){
		List<Article> articles = entityManager
			.createQuery("select a from Article a where a.classification = null and a.mapStudy = :mapStudy AND a.removed = false order by a.title asc", Article.class)
				.setParameter("mapStudy", mapStudy)
				.getResultList();
		return articles;
	}
	
	public List<Article> getArticlesToEvaluate(User user, MapStudy mapStudy){
		List<Article> articles = entityManager
			.createQuery("select a from Article a where a.classification = null and a.mapStudy = :mapStudy AND a.removed = false and a.id not in (select e.article.id from Evaluation e where e.user = :user and e.mapStudy = :mapStudy) order by a.title asc", Article.class)
				.setParameter("user", user)
				.setParameter("mapStudy", mapStudy)
				.getResultList();
		return articles;
	}
	
//	public Article getArticlesToEvaluate(User user, Long articleid){
//		Article article = entityManager
//			.createQuery("select a from Article a where a.classification = null  and a.id = :articleid and a.id not in (select e.article.id from Evaluation e where e.user = :user) order by a.title asc", Article.class)
//				.setParameter("user", user)
//				.setParameter("articleid", articleid)
//				.getSingleResult();
//		return article;
//	}
	
	public List<Article> getArticlesEvaluated(User user, MapStudy mapStudy){
		List<Article> articles = entityManager
			.createQuery("select a from Article a where a.classification = null and a.mapStudy = :mapStudy AND a.removed = false and a.id in (select e.article.id from Evaluation e where e.user = :user and e.mapStudy = :mapStudy) order by a.title asc", Article.class)
				.setParameter("user", user)
				.setParameter("mapStudy", mapStudy)
				.getResultList();
		return articles;
	}
	
	public List<Article> getArticlesFinalAccepted(MapStudy mapStudy){
		List<Article> articles = entityManager
			.createQuery("select a from Article a where a.classification = null and a.finalEvaluation = :finalEvaluation and a.mapStudy = :mapStudy AND a.removed = false order by a.title asc", Article.class)
				.setParameter("mapStudy", mapStudy)
				.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)
				.getResultList();
		return articles;
	}
	
	public List<Article> getArticlesFinalEvaluate(MapStudy mapStudy){
		List<Article> articles = entityManager
			.createQuery("select a from Article a where a.classification = null and a.finalEvaluation is not null and a.finalEvaluation <> :finalEvaluation and a.mapStudy = :mapStudy AND a.removed = false order by a.title asc", Article.class)
				.setParameter("mapStudy", mapStudy).setParameter("finalEvaluation", EvaluationStatusEnum.NOT_EVALUATED)
				.getResultList();
		return articles;
	}
	
	public Long countArticlesFinalAccepted(MapStudy mapStudy){
		Long count = -1l;
		
		try {
			count = entityManager
				.createQuery("select count(1) from Article a where a.classification = null and a.finalEvaluation = :finalEvaluation and a.mapStudy = :mapStudy AND a.removed = false", Long.class)
				.setParameter("mapStudy", mapStudy)
				.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)
				.getSingleResult();
		}catch(Exception e){
		}
		
		return count;
	}
	
	
	/**
	 * Conta quantos artigos não possuem avaliação final
	 * @param mapStudy
	 * @return
	 */
	public Long countArticlesFinalEvaluation(MapStudy mapStudy){
		Long count = -1l;
		
		try{
			count = entityManager.createQuery("select count(1) from Article a where a.classification = null and a.finalEvaluation = null and a.mapStudy = :mapStudy AND a.removed = false", Long.class)
						 .setParameter("mapStudy", mapStudy)
						 .getSingleResult();
		}catch(Exception e){
		}		
		
		return count;
	}
	
	public Long countArticleToEvaluate(User user, MapStudy mapStudy){
		Long count = -1l;
		
		try{
			count = entityManager
					.createQuery("select count(1) from Article a where a.classification = null and a.mapStudy = :mapStudy AND a.removed = false and a.id not in (select e.article.id from Evaluation e where e.user = :user and e.mapStudy = :mapStudy)", Long.class)
					.setParameter("user", user)
					.setParameter("mapStudy", mapStudy)
					.getSingleResult();			
		}catch(Exception e){
			
		}
		
		return count;
	}
	
//	public Long countArticleToEvaluateExtraction(User user, MapStudy mapStudy) {
//		Long count = entityManager
//				.createQuery("select count(1) from Article a where a.classification = null and a.mapStudy = :mapStudy and a.id not in (select e.article.id from Evaluation e where e.user = :user and e.mapStudy = :mapStudy)", Long.class)
//					.setParameter("user", user)
//					.setParameter("mapStudy", mapStudy)
//					.getSingleResult();
//				return count;
//	}	
	
	public Long countArticleToEvaluateExtraction(User user, MapStudy mapStudy){
		Long count = -1l;
		
		try{
			count = entityManager
					.createQuery("select count(1) from Article a where a.classification = null and a.finalEvaluation = :finalEvaluation and a.mapStudy = :mapStudy AND a.removed = false and a.id not in (select e.article.id from EvaluationExtraction e where e.user = :user)", Long.class)
					.setParameter("user", user)
					.setParameter("mapStudy", mapStudy)
					.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)
					.getSingleResult();			
		}catch(Exception e){
			
		}
		
		return count;
	}
	
	
	public Long countArticleNotRefined(MapStudy mapStudy){
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);

		Root<Article> root = cq.from(Article.class);
		cq.select(qb.count(root));
		
		Predicate p = qb.conjunction();
        p = qb.and(qb.equal(root.get("mapStudy"), mapStudy), qb.isNull(root.get("classification")));
        
        Predicate p2 = qb.conjunction();
        p2 = qb.and(qb.equal(root.get("removed"), false), p);
        
		cq.where(p2);
		
		Long count = -1l;
		
		try{
			count = entityManager.createQuery(cq).getSingleResult();
		}catch(Exception e){
			
		}
		
		return count;
	}

	public List<Article> getArticlesToExtraction(User user, MapStudy mapStudy) {
		List<Article> articles = entityManager
				.createQuery("select a from Article a where a.classification = null and a.finalEvaluation = :finalEvaluation and a.mapStudy = :mapStudy AND a.removed = false and a.id not in (select e.article.id from EvaluationExtraction e where e.user = :user) order by a.title asc", Article.class)
					.setParameter("user", user)
					.setParameter("mapStudy", mapStudy)
					.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)
					.getResultList();
			return articles;
	}

//	public List<EvaluationExtraction> getExtractions(User user, MapStudy mapStudy) {
//		List<EvaluationExtraction> extractions = entityManager
//				.createQuery("select e from EvaluationExtraction e where e.user = :user and e.article.mapStudy = :mapStudy order by e.article.id asc", EvaluationExtraction.class)
//					.setParameter("user", user)
//					.setParameter("mapStudy", mapStudy)
//					.getResultList();
//			return extractions;
//	}
	
	public List<Article> getExtractions(User user, MapStudy mapStudy) {
		List<Article> extractions = entityManager
				.createQuery("select distinct(e.article) from EvaluationExtraction e where e.article.classification = null and e.article.finalEvaluation = :finalEvaluation and e.user = :user and e.article.mapStudy = :mapStudy AND e.article.removed = false order by e.article.id asc", Article.class)
				.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)	
				.setParameter("user", user)
					.setParameter("mapStudy", mapStudy)
					.getResultList();
			return extractions;
	}
	
	public List<Article> getExtractions(MapStudy mapStudy) {
		List<Article> extractions = entityManager
				.createQuery("select distinct(e.article) from EvaluationExtraction e where e.article.classification = null and e.article.mapStudy = :mapStudy and e.article.finalEvaluation = :finalEvaluation AND e.article.removed = false order by e.article.id asc", Article.class)
					.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)
					.setParameter("mapStudy", mapStudy)
					.getResultList();
			return extractions;
	}
	
	public List<Article> getArticlesToFinalExtraction(MapStudy mapStudy) {
		List<Article> finalToExtractions = entityManager
				.createQuery("select a from Article a where a.classification = null and a.finalEvaluation = :finalEvaluation and a.mapStudy = :mapStudy AND a.removed = false and a not in (select e.article from EvaluationExtractionFinal e where e.mapStudy = :mapStudy) order by a.id asc", Article.class)
					.setParameter("mapStudy", mapStudy)
					.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)
					.getResultList();
			return finalToExtractions;
	}
	
	public List<Article> getArticlesFinalExtraction(MapStudy mapStudy) {
		List<Article> finalExtractions = entityManager
				.createQuery("select distinct(e.article) from EvaluationExtractionFinal e where e.article.classification = null and e.article.finalEvaluation = :finalEvaluation and e.mapStudy = :mapStudy AND e.article.removed = false order by e.article.id asc", Article.class)
					.setParameter("finalEvaluation", EvaluationStatusEnum.ACCEPTED)
					.setParameter("mapStudy", mapStudy)
					.getResultList();
			return finalExtractions;
	}

	public void removeAllArticlesMap(Long mapId) {
		entityManager.createQuery("UPDATE Article a SET a.removed = true WHERE a.mapStudy.id = :mapId").setParameter("mapId", mapId).executeUpdate();
	}
	
	public void remove(Long articleId) {
		entityManager.createQuery("UPDATE Article a SET a.removed = true  WHERE a.id :articleId").setParameter("articleId", articleId).executeUpdate();
	}
}