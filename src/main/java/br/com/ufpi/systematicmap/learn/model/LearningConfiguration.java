/**
 * 
 */
package br.com.ufpi.systematicmap.learn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.ufpi.systematicmap.model.MapStudy;

/**
 * @author Gleison Andrade
 *
 */
@Entity
@Table(name = "learningconfiguration")
public class LearningConfiguration implements Serializable{

	private static final long serialVersionUID = 1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
    @JoinColumn(name="mapStudy_id")
	private MapStudy mapStudy;
	
	@Enumerated(EnumType.STRING)
	private LearningAlgorithms algorithm;
	
	private Integer numberArtclesValidation = 5;
	private Boolean useResearcher = false;
	private Boolean showSelection = true;
	
	/**
	 * 
	 */
	public LearningConfiguration() {
	}

	/**
	 * @param mapStudy
	 * @param algorithm
	 */
	public LearningConfiguration(MapStudy mapStudy, LearningAlgorithms algorithm) {
		super();
		this.mapStudy = mapStudy;
		this.algorithm = algorithm;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the mapStudy
	 */
	public MapStudy getMapStudy() {
		return mapStudy;
	}

	/**
	 * @return the algorithm
	 */
	public LearningAlgorithms getAlgorithm() {
		return algorithm;
	}

	/**
	 * @return the numberArtclesValidation
	 */
	public Integer getNumberArtclesValidation() {
		return numberArtclesValidation;
	}

	/**
	 * @return the useResearcher
	 */
	public Boolean getUseResearcher() {
		return useResearcher;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param mapStudy the mapStudy to set
	 */
	public void setMapStudy(MapStudy mapStudy) {
		this.mapStudy = mapStudy;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(LearningAlgorithms algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @param numberArtclesValidation the numberArtclesValidation to set
	 */
	public void setNumberArtclesValidation(Integer numberArtclesValidation) {
		this.numberArtclesValidation = numberArtclesValidation;
	}

	/**
	 * @param useResearcher the useResearcher to set
	 */
	public void setUseResearcher(Boolean useResearcher) {
		this.useResearcher = useResearcher;
	}

	/**
	 * @return the showSelection
	 */
	public Boolean getShowSelection() {
		return showSelection;
	}

	/**
	 * @param showSelection the showSelection to set
	 */
	public void setShowSelection(Boolean showSelection) {
		this.showSelection = showSelection;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((algorithm == null) ? 0 : algorithm.hashCode());
		result = prime * result + ((mapStudy == null) ? 0 : mapStudy.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof LearningConfiguration)) {
			return false;
		}
		LearningConfiguration other = (LearningConfiguration) obj;
		if (algorithm != other.algorithm) {
			return false;
		}
		if (mapStudy == null) {
			if (other.mapStudy != null) {
				return false;
			}
		} else if (!mapStudy.equals(other.mapStudy)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"LearningConfiguration [id=%s, mapStudy=%s, algorithm=%s, numberArtclesValidation=%s, useResearcher=%s]",
				id, mapStudy, algorithm, numberArtclesValidation, useResearcher);
	}
}