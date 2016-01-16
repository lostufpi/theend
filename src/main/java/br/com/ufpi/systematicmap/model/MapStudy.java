package br.com.ufpi.systematicmap.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.model.enums.Roles;


@Entity 
@Table(name = "mapstudy")
public class MapStudy implements Serializable{

	private static final long serialVersionUID = 1;
	
	@Id
	@GeneratedValue
	private Long id;

	@NotNull(message = "required")
	@Size(min = 3, message="mapstudy.min.title")
	private String title;

	@NotNull(message = "required")
    @Size(min = 3, message="mapstudy.min.description")
	private String description;
    
    private boolean removed;
        
    @OneToMany(mappedBy = "mapStudy", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)  
	private Set<UsersMapStudys> usersMapStudys = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
	private Set<Article> articles = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
	private Set<InclusionCriteria> inclusionCriterias = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
	private Set<ExclusionCriteria> exclusionCriterias = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
	private Set<Evaluation> evaluations= new HashSet<>();
    
//    @OneToMany(mappedBy="mapStudy")
//	private Set<DataExtractionForm> dataExtractionForm = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public Set<InclusionCriteria> getInclusionCriterias() {
		return inclusionCriterias;
	}

	public void setInclusionCriterias(Set<InclusionCriteria> inclusionCriterias) {
		this.inclusionCriterias = inclusionCriterias;
	}

	public Set<ExclusionCriteria> getExclusionCriterias() {
		return exclusionCriterias;
	}

	public void setExclusionCriterias(Set<ExclusionCriteria> exclusionCriterias) {
		this.exclusionCriterias = exclusionCriterias;
	}

	public Set<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(Set<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}
	
	public void addArticle(Article article) {
		getArticles().add(article);
		article.setMapStudy(this);
	}
	
	public void addInclusionCriteria(InclusionCriteria criteria) {
		getInclusionCriterias().add(criteria);
		criteria.setMapStudy(this);
	}
	
	public void addExclusionCriteria(ExclusionCriteria criteria) {
		getExclusionCriterias().add(criteria);
		criteria.setMapStudy(this);
	}
	
	public String percentEvaluated(Double percentEvaluated){
		return String.format("%.2f", percentEvaluated);
	}
	
	public Double percentEvaluatedDouble(ArticleDao articleDao, User user){
		int total = articleDao.countArticleNotRefined(this).intValue(),
		    toEvaluate = articleDao.countArticleToEvaluate(user, this).intValue();
		 
		BigDecimal tot = new BigDecimal(total);
		BigDecimal dontEval = new BigDecimal(toEvaluate);
		BigDecimal bigdecimal;
		
		if(tot.equals(BigDecimal.ZERO)){
			bigdecimal = BigDecimal.ZERO;
		}else{
			if(dontEval.equals(BigDecimal.ZERO)){
				bigdecimal = new BigDecimal(100);
			}else{
				bigdecimal = tot.subtract(dontEval).divide(tot, 100, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			}
		}
		
		return bigdecimal.doubleValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MapStudy other = (MapStudy) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MapStudy [id=" + id + ", title=" + title + 
			", description=" + description + "]";
	}
	

	/**
	 * @return the removed
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/**
	 * @return the usersMapStudys
	 */
	public Set<UsersMapStudys> getUsersMapStudys() {
		return usersMapStudys;
	}

	/**
	 * @param usersMapStudys the usersMapStudys to set
	 */
	public void setUsersMapStudys(Set<UsersMapStudys> usersMapStudys) {
		this.usersMapStudys = usersMapStudys;
	}

	//TODO Matheus analisar
	
	public void addParticipant(User user) {
		for (UsersMapStudys u : usersMapStudys) {
			if (u.getUser().equals(user) && u.isRemoved()){
				u.setRemoved(false);
				return;
			}
		}
		
		UsersMapStudys usersMapStudys = new UsersMapStudys();
		usersMapStudys.setUser(user);
		usersMapStudys.setMapStudy(this);
		usersMapStudys.setRole(Roles.PARTICIPANT);		
		getUsersMapStudys().add(usersMapStudys);
	}
	
	public void removeParticipant(User user) {
		for (UsersMapStudys u : usersMapStudys) {
			if (u.getUser().equals(user)){
				u.setRemoved(true);
				return;
			}
		}
	}
	
	public void addCreator(User user) {
		UsersMapStudys usersMapStudys = new UsersMapStudys();
		usersMapStudys.setUser(user);
		usersMapStudys.setMapStudy(this);
		usersMapStudys.setRole(Roles.CREATOR);
		getUsersMapStudys().add(usersMapStudys);
	}
	
	public boolean isCreator(User user) {
		for (UsersMapStudys u : usersMapStudys) {
			if (u.getUser().equals(user)){
				if (u.getRole().equals(Roles.CREATOR)){
					return true;
				}else{
					return false;
				}
			}
		}	
		
		return false;
	}
	
	public List<User> members(){
		List<User> members = new ArrayList<>();
		for (UsersMapStudys u : usersMapStudys) {
			if (!u.isRemoved()){
				members.add(u.getUser());
			}			
		}
		return members;
	}

//	/**
//	 * @return the dataExtractionForm
//	 */
//	public Set<DataExtractionForm> getDataExtractionForm() {
//		return dataExtractionForm;
//	}
//
//	/**
//	 * @param dataExtractionForm the dataExtractionForm to set
//	 */
//	public void setDataExtractionForm(Set<DataExtractionForm> dataExtractionForm) {
//		this.dataExtractionForm = dataExtractionForm;
//	}
	
	
}
