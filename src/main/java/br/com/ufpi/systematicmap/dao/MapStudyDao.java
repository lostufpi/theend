package br.com.ufpi.systematicmap.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ufpi.systematicmap.learn.model.LearningConfiguration;
import br.com.ufpi.systematicmap.learn.model.LearningStats;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.User;

@RequestScoped
public class MapStudyDao extends Dao<MapStudy> {

	private final EntityManager entityManager;

	/**
	 * @deprecated CDI eyes only
	 */
	protected MapStudyDao() {
		this(null);
	}
	
	@Inject
	public MapStudyDao(EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
	}

	public void add(MapStudy map) {
		entityManager.persist(map);
	}

	public List<MapStudy> searchSimilarTitle(String title) {
		List<MapStudy> musics = entityManager
			.createQuery("select m from MapStudy m where lower(m.title) like lower(:title)", MapStudy.class)
				.setParameter("title", "%" + title+ "%")
				.getResultList();
		return musics;
	}
	
	public List<MapStudy> mapStudys(User user) {
		List<MapStudy> maps = entityManager.createQuery("select m from MapStudy m left join m.usersMapStudys ums where ums.user = :user and m.removed = false and ums.removed = false", MapStudy.class).setParameter("user", user).getResultList();
		return maps;
	}
	
	@Override
	public MapStudy find(Long id) {
		MapStudy mapStudy = null;
		
		try {
			mapStudy = entityManager.createQuery("select m from MapStudy m where m.id =:id and m.removed = false", MapStudy.class).setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return mapStudy;
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