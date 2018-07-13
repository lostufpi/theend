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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.caelum.vraptor.serialization.SkipSerialization;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.model.enums.Roles;


@Entity 
@Table(name = "mapstudy")
public class MapStudy implements Serializable{

	private static final long serialVersionUID = 1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "required")
	@Size(min = 3, message="mapstudy.min.title")
	private String title;

	@NotNull(message = "required")
    @Size(min = 3, message="mapstudy.min.description")
	@Lob
	private String description;
    
    private boolean removed;
        
    @OneToMany(mappedBy = "mapStudy", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)  
    @SkipSerialization
    private Set<UsersMapStudys> usersMapStudys = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
    @SkipSerialization
    @OrderBy("id ASC")
	private Set<Article> articles = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
    @SkipSerialization
	private Set<InclusionCriteria> inclusionCriterias = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
    @SkipSerialization
	private Set<ExclusionCriteria> exclusionCriterias = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
    @SkipSerialization
	private Set<Evaluation> evaluations= new HashSet<>();
    
//    @OneToMany(mappedBy="mapStudy")
//	private Set<DataExtractionForm> dataExtractionForm = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
    @SkipSerialization
   	private Set<ResearchQuestion> researchQuestions = new HashSet<>();
    
    @OneToMany(mappedBy="mapStudy")
    @SkipSerialization
   	private Set<SearchString> searchString = new HashSet<>();
    
    @Lob
    @SkipSerialization
    private String goals;   
    
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="form_id")
    private Form form;
    
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="refinement_id")
    private RefinementParameters refinementParameters = new RefinementParameters();;


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
	
	public Double percentExtractedDouble(ArticleDao articleDao, User user) {
		int total = articleDao.countArticlesFinalAccepted(this).intValue(),
			    toExtrantion = articleDao.countArticleToEvaluateExtraction(user, this).intValue();
		
			BigDecimal tot = new BigDecimal(total);
			BigDecimal dontEval = new BigDecimal(toExtrantion);
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
			", description=" + description + ", form="+ form +"]";
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
	
	public void addUser(User user, Roles role) {
		for (UsersMapStudys u : usersMapStudys) {
			if (u.getUser().equals(user) && u.isRemoved()){
				u.setRole(role);
				u.setRemoved(false);
				return;
			}
		}
		
		UsersMapStudys usersMapStudys = new UsersMapStudys();
		usersMapStudys.setUser(user);
		usersMapStudys.setMapStudy(this);
		usersMapStudys.setRole(role);		
		getUsersMapStudys().add(usersMapStudys);		
	}
	
	public void removeUserMap(User user) {
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
	
	public boolean isSupervisor(User user) {
		for (UsersMapStudys u : usersMapStudys) {
			if (u.getUser().equals(user)){
				if (u.getRole().equals(Roles.SUPERVISOR)){
					return true;
				}else{
					return false;
				}
			}
		}	
		
		return false;
	}
	
	public Roles role(User user) {
		for (UsersMapStudys u : usersMapStudys) {
			if (u.getUser().equals(user)){
				return u.getRole();
			}
		}	
		
		return null;
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
	
	public void removeResearchQuestion(Long id){
		for (ResearchQuestion rq : researchQuestions) {
			if (rq.getId().equals(id)){
				rq.setMapStudy(null);
				researchQuestions.remove(rq);
				break;
			}
		}
	}

	/**
	 * @return the researchQuestions
	 */
	public Set<ResearchQuestion> getResearchQuestions() {
		return researchQuestions;
	}

	/**
	 * @param researchQuestions the researchQuestions to set
	 */
	public void setResearchQuestions(Set<ResearchQuestion> researchQuestions) {
		this.researchQuestions = researchQuestions;
	}

	/**
	 * @return the goals
	 */
	public String getGoals() {
		return goals;
	}

	/**
	 * @param goals the goals to set
	 */
	public void setGoals(String goals) {
		this.goals = goals;
	}

	/**
	 * @return the searchString
	 */
	public Set<SearchString> getSearchString() {
		return searchString;
	}

	/**
	 * @param searchString the searchString to set
	 */
	public void setSearchString(Set<SearchString> searchString) {
		this.searchString = searchString;
	}
	
	/**
	 * @return the form
	 */
	public Form getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(Form form) {
		this.form = form;
	}

	/**
	 * Adiicona formulário ao mapeamento
	 * @param form
	 */
	public void addForm(Form form) {
		if (this.form != null){
//			form.setId(this.form.getId());
//			form.setMapStudy(this);
			for (Question q : form.getQuestions()) {
				this.form.addQuestion(q);
			}
			form = this.form;
		}else{
			this.form = form;
			form.setMapStudy(this);
		}
	}

	/**
	 * @return the refinementParameters
	 */
	public RefinementParameters getRefinementParameters() {
		return refinementParameters;
	}

	/**
	 * @param refinementParameters the refinementParameters to set
	 */
	public void setRefinementParameters(RefinementParameters refinementParameters) {
		this.refinementParameters = refinementParameters;
	}

	public void setRefinementParameters(Integer levenshtein, String regex, Integer limiartitulo, Integer limiarabstract,
			Integer limiarkeywords, Integer limiartotal, boolean filterAuthor, boolean filterAbstract,
			boolean filterLevenshtein) {
		this.getRefinementParameters().setLevenshtein(levenshtein);
		this.getRefinementParameters().setRegex(regex);
		this.getRefinementParameters().setLimiarTitle(limiartitulo);
		this.getRefinementParameters().setLimiarAbstract(limiarabstract);
		this.getRefinementParameters().setLimiarKeywords(limiarkeywords);
		this.getRefinementParameters().setLimiarTotal(limiartotal);
		this.getRefinementParameters().setFilterAbstract(filterAbstract);
		this.getRefinementParameters().setFilterAuthor(filterAuthor);
		this.getRefinementParameters().setFilterLevenshtein(filterLevenshtein);
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
