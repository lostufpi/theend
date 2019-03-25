package br.com.ufpi.systematicmap.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.User;

@RequestScoped
public class UserDao extends Dao<User> {
	
	private final EntityManager entityManager;

	/**
	 * @deprecated CDI eyes only
	 */
	protected UserDao() {
		this(null);
	}

	@Inject
	public UserDao(EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
	}

	public User find(String login, String password) {
//		GenerateHashPasswordUtil generateHashPasswordUtil = new GenerateHashPasswordUtil();
		try {
			User user = entityManager
				.createQuery("select u from User u where (u.login = :login or u.email = :login) and u.password = :password", User.class)
					.setParameter("login", login)
					.setParameter("password", password)
					.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}

	public User find(String login) {
		User user = null;
		
		try{
			user = entityManager.createQuery("select u from User u where (u.login = :login or u.email = :login)", User.class)
					.setParameter("login", login)
					.getSingleResult();			
		}catch(Exception e){
		}
		
		return user;
	}
	
	public boolean containsUserWithLogin(String login) {
		Long count = -1l;
		
		try{
			count = entityManager
					.createQuery("select count(u) from User u where u.login = :login", Long.class)
					.setParameter("login", login)
					.getSingleResult();			
		}catch(Exception e){
			
		}
		return count > 0;
	}
	
	public List<User> mapStudyUsers(MapStudy mapStudy) {
		List<User> users = entityManager
				.createQuery("select u from User u left join u.usersMapStudys ums where ums.mapStudy = :mapStudy and ums.removed = false and u.removed = false", User.class)
					.setParameter("mapStudy", mapStudy)
					.getResultList();
		return users;
	}
	
	public List<User> mapStudyUsersNotSuper(MapStudy mapStudy) {
		List<User> users = entityManager
				.createQuery("select u from User u left join u.usersMapStudys ums where ums.mapStudy = :mapStudy and ums.removed = false and u.removed = false and (ums.role = 'PARTICIPANT' OR ums.role = 'CREATOR')", User.class)	
				.setParameter("mapStudy", mapStudy)
					.getResultList();
		return users;
	}
	
	public List<User> mapStudyArentUsers(MapStudy mapStudy) {
		List<User> users = entityManager
				.createQuery("select distinct u from User u where u.id not in (select distinct u.id from User u left join u.usersMapStudys ums where ums.mapStudy = :mapStudy and ums.removed = false and u.removed = false)", User.class)
					.setParameter("mapStudy", mapStudy)
					.getResultList();
		return users;
	}
	
	public List<User> findUserName(String name) {	 
		List<User> users = entityManager
				.createQuery("select u from User u where u.name like :name", User.class)
					.setParameter("name", "%" + name + "%").getResultList();
		return users;
	}
	
	public User findEmail(String email){
		try {
			return entityManager.createQuery("select u from User u where u.email = :email", User.class)
					.setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public User findCodeRecovery(String code){
		try {
			return entityManager.createQuery("select u from User u where u.recoveryCode = :recoverycode", User.class)
					.setParameter("recoverycode", code)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void clearAllCodeRecovery(){
			
	}

	public boolean containsUserWithEMail(String email) {
		Long count = -1l;
		
		try{
			count = entityManager
					.createQuery("select count(u) from User u where u.email = :email", Long.class)
					.setParameter("email", email)
					.getSingleResult();			
		}catch(Exception e){
			
		}
		
		return count > 0;
	}
	
	@Override
	public User find(Long id) {
		User user = null;
		
		try{
			user = entityManager.createQuery("select u from User u where u.id =:id and u.removed = false", User.class).setParameter("id", id).getSingleResult();
		}catch(Exception e){
			
		}
		
		return user;
	}

	
//	public JsonArray findUserNameJson(String name) {
//		JsonArray users = new JsonArray();
//		
//		List<User> listUsers = findUserName(name);
//		
//		for (User user : listUsers) {
//			JsonObject jsonObject = new JsonObject();
//			jsonObject.addProperty("label", user.getName());
//			jsonObject.addProperty("value", user.getId());
////			jsonObject.addProperty("date", user);
//			users.add(jsonObject);
//		}
//		
//		return users;
//	}

}