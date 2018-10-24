package br.com.ufpi.systematicmap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.caelum.vraptor.serialization.SkipSerialization;
import br.com.ufpi.systematicmap.dao.EvaluationExtractionFinalDao;
import br.com.ufpi.systematicmap.learn.model.ArticleStatusSelection;
import br.com.ufpi.systematicmap.learn.model.EvaluationArticleAlgorithm;
import br.com.ufpi.systematicmap.learn.model.LearningAlgorithms;
import br.com.ufpi.systematicmap.model.enums.ArticleSourceEnum;
import br.com.ufpi.systematicmap.model.enums.ClassificationEnum;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;

@Entity
@Table(name = "article")
public class Article implements Serializable {

	private static final long serialVersionUID = 1;

	//Falta o anotation do artigo
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private boolean removed;
	
	private int score;

	@SkipSerialization
//	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGenerator")
//	@SequenceGenerator(allocationSize=1, schema="myschema",  name="MySequenceGenerator", sequenceName = "mysequence")
	private Long number;
	
	@ManyToOne
	@JoinColumn(name = "mapStudy_id")
	@SkipSerialization
	private MapStudy mapStudy;
	
	@OneToMany(mappedBy="article", cascade= {CascadeType.REMOVE})
	@SkipSerialization
	private Set<Evaluation> evaluations = new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	private EvaluationStatusEnum finalEvaluation;
	
	@Size(max=2000, message="article.author.maxlength")
	private String author;
	
	@NotNull(message="article.title.required")
	@Size(max=2000, message="article.title.maxlength")
	private String title;
	
	@SkipSerialization
	@Size(max=2000, message="article.journal.maxlength")
	private String journal;
	@SkipSerialization
	private Integer year;
	@SkipSerialization
	private String volume;
	@SkipSerialization
	@Size(max=500)
	private String pages;
	@SkipSerialization
	private String doi;
	@SkipSerialization
	@Lob
	private String note;
	@SkipSerialization
	private String url;
	@SkipSerialization
	private String docType;//
	private String source;
	@SkipSerialization
	private String language;//
	
	@Lob
	private String abstrct;//
	
	@Size(max=2000, message="article.keywords.maxlength")
	private String keywords;
	
	@SkipSerialization
	@Enumerated(EnumType.STRING)
	private ClassificationEnum classification;
	@SkipSerialization
	private Integer regexTitle = 0;
	@SkipSerialization
	private Integer regexAbs = 0;
	@SkipSerialization
	private Integer regexKeys = 0;
	
	@OneToOne
    @JoinColumn(name="same_article_id")
	@SkipSerialization
	private Article paperMinLevenshteinDistance;
	@SkipSerialization
	private Integer minLevenshteinDistance;
	
	@OneToMany(mappedBy="article", cascade=CascadeType.ALL)
	@OrderBy("question")
	@SkipSerialization
	private Set<EvaluationExtraction> evaluationExtractions = new HashSet<>();
	
	@OneToMany(mappedBy="article", cascade=CascadeType.ALL)
	@OrderBy("question")
	@SkipSerialization
	private Set<EvaluationExtractionFinal> evaluationExtractionsFinal = new HashSet<>();
	
	@Lob
	private String infos;
	
	@OneToMany(mappedBy="article", cascade= {CascadeType.REMOVE})
	@SkipSerialization
	private List<EvaluationArticleAlgorithm> evaluationsAlgorithms = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private ArticleStatusSelection statusSelection = ArticleStatusSelection.NOT_USED;
	
	public void addEvaluationAlgorith(LearningAlgorithms algorithm, EvaluationStatusEnum evaluation){
		EvaluationArticleAlgorithm evaluationArticleAlgorithm = new EvaluationArticleAlgorithm(this, algorithm, evaluation);
//		setStatusSelection(ArticleStatusSelection.TRANING);
		evaluationsAlgorithms.add(evaluationArticleAlgorithm);
	}
	
	public void addExtractionFinal(EvaluationExtraction evaluationExtraction){
		EvaluationExtractionFinal evaluationExtractionFinal = new EvaluationExtractionFinal();
		evaluationExtractionFinal.setMapStudy(mapStudy);
		evaluationExtractionFinal.setAlternative(evaluationExtraction.getAlternative());
		evaluationExtractionFinal.setArticle(evaluationExtraction.getArticle());
		evaluationExtractionFinal.setQuestion(evaluationExtraction.getQuestion());
		
		this.evaluationExtractionsFinal.add(evaluationExtractionFinal);		
	}
	
	public Set<EvaluationExtractionFinal> getEvaluationExtractionsFinal() {
        return this.evaluationExtractionsFinal;
    }

    public void setEvaluationExtractionsFinal(Set<EvaluationExtractionFinal> evaluationExtractionsFinal) {
        this.evaluationExtractionsFinal = evaluationExtractionsFinal;
    }	
	
	//FIXME Add question a tabela mudar logica
	public Alternative alternative(Question question){
		for (EvaluationExtraction ev : evaluationExtractions) {
			if (ev.getQuestion().equals(question)){
				return ev.getAlternative();
			}
		}
		
		return null;
	}
	
	//FIXME Add question a tabela mudar logica
		public List<Alternative> alternativeFinal(Question question){
			List<Alternative> alternatives = new ArrayList<Alternative>();
			
			for (EvaluationExtractionFinal ev : evaluationExtractionsFinal) {
				if (ev.getQuestion().equals(question)){
					alternatives.add(ev.getAlternative());
				}
			}
			
			return alternatives;
		}
	
	public List<Alternative> alternative(Question question, User user) {
		List<Alternative> alternatives = new ArrayList<Alternative>();

		for (EvaluationExtraction ev : evaluationExtractions) {
			if (ev.getQuestion().equals(question) && ev.getUser().equals(user)) {
				alternatives.add(ev.getAlternative());
			}
		}
		
		return alternatives;
	}
		
	
	public ClassificationEnum getClassification() {
		return classification;
	}

	public void setClassification(ClassificationEnum classification) {
		this.classification = classification;
	}

	public Integer getRegexTitle() {
		return regexTitle;
	}

	public void setRegexTitle(Integer regexTitle) {
		this.regexTitle = regexTitle;
	}

	public Integer getRegexAbs() {
		return regexAbs;
	}

	public void setRegexAbs(Integer regexAbs) {
		this.regexAbs = regexAbs;
	}

	public Integer getRegexKeys() {
		return regexKeys;
	}

	public void setRegexKeys(Integer regexKeys) {
		this.regexKeys = regexKeys;
	}

	public Article getPaperMinLevenshteinDistance() {
		return paperMinLevenshteinDistance;
	}

	public void setPaperMinLevenshteinDistance(Article paperMinLevenshteinDistance) {
		this.paperMinLevenshteinDistance = paperMinLevenshteinDistance;
	}

	public Integer getMinLevenshteinDistance() {
		return minLevenshteinDistance;
	}

	public void setMinLevenshteinDistance(Integer minLevenshteinDistance) {
		this.minLevenshteinDistance = minLevenshteinDistance;
	}

	public MapStudy getMapStudy() {
		return mapStudy;
	}

	public void setMapStudy(MapStudy mapStudy) {
		this.mapStudy = mapStudy;
	}

	public Set<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(Set<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAbstrct() {
		return abstrct;
	}

	public void setAbstrct(String abstrct) {
		this.abstrct = abstrct;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public EvaluationStatusEnum getFinalEvaluation() {
		return finalEvaluation;
	}

	public void setFinalEvaluation(EvaluationStatusEnum finalEvaluation) {
		this.finalEvaluation = finalEvaluation;
	}

	public String getEvaluationClassification(User user){
		for(Evaluation e : evaluations){
			if(e.getUser().equals(user)){
				return e.getClassification();
			}
		}
		return EvaluationStatusEnum.NOT_EVALUATED.getDescription();
	}
	
	public Evaluation getEvaluation(User user){
		for(Evaluation e : evaluations){
			if(e.getUser().equals(user)){
				return e;
			}
		}
		return null;
	}
	
	public EvaluationStatusEnum showFinalEvaluation(){
		return finalEvaluation != null ? finalEvaluation : EvaluationStatusEnum.NOT_EVALUATED;
	}
	
	public String sourceView(String source){
		return ArticleSourceEnum.valueOf(source).getDescription();
	}

	/**
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * @return the evaluationExtractions
	 */
	public Set<EvaluationExtraction> getEvaluationExtractions() {
		return evaluationExtractions;
	}

	/**
	 * @param evaluationExtractions the evaluationExtractions to set
	 */
	public void setEvaluationExtractions(
			Set<EvaluationExtraction> evaluationExtractions) {
		this.evaluationExtractions = evaluationExtractions;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public List<EvaluationExtraction> getEvaluationExtraction(User user){
		List<EvaluationExtraction> eval = new ArrayList<>();
		for (EvaluationExtraction ev : evaluationExtractions) {
			if (ev.getUser().equals(user)){
//				System.out.println("EV: " + ev);
				eval.add(ev);
			}
		}
		return eval;		
	}
	
//	public EvaluationExtraction getEvaluationExtraction(User user){
//		for (EvaluationExtraction ev : evaluationExtractions) {
//			if (ev.getUser().equals(user)){
//				return ev;
//			}
//		}
//		return null;		
//	}

	public void addExtraction(Alternative[] alternatives, Article article, User user) {
		for (Alternative alternative : alternatives) {
			
			EvaluationExtraction evaluationExtraction = new EvaluationExtraction();
			evaluationExtraction.setAlternative(alternative);
			evaluationExtraction.setArticle(article);
			evaluationExtraction.setUser(user);
			
			this.getEvaluationExtractions().add(evaluationExtraction);
		}
		
	}

	public void addExtraction(Alternative alternative, Article article, User user) {
			
			EvaluationExtraction evaluationExtraction = new EvaluationExtraction();
			evaluationExtraction.setAlternative(alternative);
			evaluationExtraction.setArticle(article);
			evaluationExtraction.setUser(user);
			
			this.getEvaluationExtractions().add(evaluationExtraction);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public void AddEvaluationExtractions(EvaluationExtraction evaluationExtraction) {
		EvaluationExtraction ev = findEvaluationExtraction(evaluationExtraction);
		if (ev != null){
			ev.setAlternative(evaluationExtraction.getAlternative());	
		}else{
			this.evaluationExtractions.add(evaluationExtraction);
		}
	}
	
	private EvaluationExtraction findEvaluationExtraction(EvaluationExtraction evaluationExtraction){
		for (EvaluationExtraction ev : evaluationExtractions) {
			if (ev.equals(evaluationExtraction)){
				return ev;
			}
		}
		return null;
	}
	
	public HashMap<String, String> getEvaluateFinalExtractionAlternative(){
		HashMap<String, String> questionsAndAlternative = new HashMap<String, String>();
		
		for (EvaluationExtractionFinal ee : getEvaluationExtractionsFinal()) {
			questionsAndAlternative.put(ee.getQuestion().getName(), ee.getAlternative().getValue());
		}
		
		return questionsAndAlternative;
	}
	

	public HashMap<String, String> getEvaluateFinalExtractionAlternative(User user) {
		HashMap<String, String> questionsAndAlternative = new HashMap<String, String>();
		
		for (EvaluationExtraction ee : getEvaluationExtractions()) {
			if (ee.getUser().equals(user)){
				questionsAndAlternative.put(ee.getQuestion().getName(), ee.getAlternative().getValue());
			}
		}
		
		return questionsAndAlternative;
	}
	
	public  HashMap<String, List<String>> getEvaluateFinalExtractionAlternatives(){
		 HashMap<String, List<String>> questionsAndAlternative =  new HashMap<String, List<String>>();
		 List<String> alternatives = null;
		
		for (EvaluationExtractionFinal ee : getEvaluationExtractionsFinal()) {
			if (questionsAndAlternative.containsKey(ee.getQuestion().getName())){
				alternatives = questionsAndAlternative.get(ee.getQuestion().getName());
			}else{
				alternatives = new ArrayList<String>();				
			}			
			
			alternatives.add(ee.getAlternative().getValue());
			questionsAndAlternative.put(ee.getQuestion().getName(), alternatives);	
		}
		
		return questionsAndAlternative;
	}
	
	public  HashMap<String, List<String>> getEvaluateFinalExtractionAlternatives(User user) {
		HashMap<String, List<String>> questionsAndAlternative =  new HashMap<String, List<String>>();
		 List<String> alternatives = null;
		
		for (EvaluationExtraction ee : getEvaluationExtractions()) {
			if (ee.getUser().equals(user)){
				if (questionsAndAlternative.containsKey(ee.getQuestion().getName())){
					alternatives = questionsAndAlternative.get(ee.getQuestion().getName());
				}else{
					alternatives = new ArrayList<String>();				
				}			
				
				alternatives.add(ee.getAlternative().getValue());
				questionsAndAlternative.put(ee.getQuestion().getName(), alternatives);
			}
		}
		
		return questionsAndAlternative;
	}
	
	
	
	
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	public Long alternativesCount(Alternative x, Alternative y) {
//		System.out.println("entrou count");
		Long c = 0l;
		for (EvaluationExtractionFinal ee : getEvaluationExtractionsFinal()) {
//			System.out.println(ee);
			if (ee.getAlternative().equals(x) || ee.getAlternative().equals(y)){
//				System.out.println("c: " +c);
				c++;
				if (c == 2) return 1l;
			}
		}
		return 0l;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Article [id=" + id + "]";
	}

	/**
	 * @return the infos
	 */
	public String getInfos() {
		return infos;
	}

	/**
	 * @param infos the infos to set
	 */
	public void setInfos(String infos) {
		this.infos = infos;
	}

	public void removeEvaluationExtractionFinal(Question question, Long alternative_id, EvaluationExtractionFinalDao eefDao) {
		List<EvaluationExtractionFinal> removes = new ArrayList<EvaluationExtractionFinal>();
		for(EvaluationExtractionFinal eef : getEvaluationExtractionsFinal()){
			if(eef.getQuestion().equals(question) && !eef.getAlternative().getId().equals(alternative_id)){
				removes.add(eef);
				eefDao.delete(eef.getId());
			}
		}
		
		getEvaluationExtractionsFinal().removeAll(removes);
		
	}

	public void removeEvaluationExtractionFinal(Question question, EvaluationExtractionFinalDao eefDao) {
		List<EvaluationExtractionFinal> removes = new ArrayList<EvaluationExtractionFinal>();
		for(EvaluationExtractionFinal eef : getEvaluationExtractionsFinal()){
			if(eef.getQuestion().equals(question)){
				removes.add(eef);
				eefDao.delete(eef.getId());
			}
		}
		
		getEvaluationExtractionsFinal().removeAll(removes);		
	}

	public String getCommentsUser(Long id2) {
		for (EvaluationExtraction ev : evaluationExtractions) {
			if (ev.getUser().getId().equals(id2) && ev.getArticle().getId().equals(this.getId())){
				return ev.getComment();
			}
		}
		return "";	
	}
	
	public List<Comment> getComments() {
		List<Comment> comments = new ArrayList<>();

		for (EvaluationExtraction ev : evaluationExtractions) {
			if(ev.getComment() != null && !ev.getComment().isEmpty())
				comments.add(new Comment(ev.getUser(), ev.getComment()));
		}
		
		return comments;
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
	 * @return the statusSelection
	 */
	public ArticleStatusSelection getStatusSelection() {
		return statusSelection;
	}

	/**
	 * @param statusSelection the statusSelection to set
	 */
	public void setStatusSelection(ArticleStatusSelection statusSelection) {
		this.statusSelection = statusSelection;
	}

	/**
	 * @return the evaluationsAlgorithms
	 */
	public List<EvaluationArticleAlgorithm> getEvaluationsAlgorithms() {
		return evaluationsAlgorithms;
	}

	public EvaluationArticleAlgorithm getEvaluationsAlgorithms(LearningAlgorithms valueOf) {
		for(EvaluationArticleAlgorithm eaa : evaluationsAlgorithms){
			if(eaa.getAlgorithm().equals(valueOf)){
				return eaa;
			}
		}
		
		return null;
	}
	
}