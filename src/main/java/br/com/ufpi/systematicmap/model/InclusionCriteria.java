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
@Table(name = "inclusioncriteria")
public class InclusionCriteria implements Serializable {

	private static final long serialVersionUID = 1;

	//@Enumerated(EnumType.STRING)
	
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
	@ManyToMany(mappedBy = "inclusionCriterias")
	private Set<Evaluation> evaluations = new HashSet<>();
	
	/**
	 * 
	 */
	public InclusionCriteria() {
	}

	/**
	 * @param description
	 */
	public InclusionCriteria(String description) {
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
		evaluation.addInclusion(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InclusionCriteria [id=" + id + ", description=" + description
				+ ", mapStudy=" + mapStudy + "]";
	}
}
