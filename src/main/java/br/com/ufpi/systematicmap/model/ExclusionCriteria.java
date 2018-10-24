package br.com.ufpi.systematicmap.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity
@Table(name = "exclusioncriteria")
public class ExclusionCriteria  implements Serializable {

	private static final long serialVersionUID = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@SkipSerialization
	@NotNull
	@Size(max=1000)
	private String description;
	@SkipSerialization
	@ManyToOne
	@JoinColumn(name = "mapStudy_id")
	private MapStudy mapStudy;
	@SkipSerialization
	@ManyToMany(mappedBy = "exclusionCriterias")
	private Set<Evaluation> evaluations = new HashSet<>();
	
	/**
	 * 
	 */
	public ExclusionCriteria() {
	}

	/**
	 * @param description
	 */
	public ExclusionCriteria(String description) {
		super();
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public void addEvaluation(Evaluation evaluation) {
		getEvaluations().add(evaluation);
		evaluation.addExclusion(this);
	}
	
}
