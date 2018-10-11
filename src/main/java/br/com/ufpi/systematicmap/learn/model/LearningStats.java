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
@Table(name = "learningstats")
public class LearningStats implements Serializable{

	private static final long serialVersionUID = 1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
    @JoinColumn(name="mapStudy_id")
	private MapStudy mapStudy;
	
	@Enumerated(EnumType.STRING)
	private LearningAlgorithms algorithm;
	
	private Integer numberarticles;
	private Integer numberArticlesAccepted;
	private Integer numberArticlesRejected;
	
	private Double wss;
	private Double recall;
	
	private Double accuracy;
	private Double error;
	private Double rocArea;
	private Double fMeasure;
	
	private Integer numberArticlesTraining;

	/**
	 * @param mapStudy
	 * @param algorithm
	 */
	public LearningStats(MapStudy mapStudy, LearningAlgorithms algorithm) {
		super();
		this.mapStudy = mapStudy;
		this.algorithm = algorithm;
	}
	
	public LearningStats(){}

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
	 * @return the numberarticles
	 */
	public Integer getNumberarticles() {
		return numberarticles;
	}

	/**
	 * @return the numberArticlesAccepted
	 */
	public Integer getNumberArticlesAccepted() {
		return numberArticlesAccepted;
	}

	/**
	 * @return the numberArticlesRejected
	 */
	public Integer getNumberArticlesRejected() {
		return numberArticlesRejected;
	}

	/**
	 * @return the wss
	 */
	public Double getWss() {
		return wss;
	}

	/**
	 * @return the recall
	 */
	public Double getRecall() {
		return recall;
	}

	/**
	 * @return the accuracy
	 */
	public Double getAccuracy() {
		return accuracy;
	}

	/**
	 * @return the error
	 */
	public Double getError() {
		return error;
	}

	/**
	 * @return the rocArea
	 */
	public Double getRocArea() {
		return rocArea;
	}

	/**
	 * @return the fMeasure
	 */
	public Double getfMeasure() {
		return fMeasure;
	}

	/**
	 * @return the numberArticlesTraining
	 */
	public Integer getNumberArticlesTraining() {
		return numberArticlesTraining;
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
	 * @param numberarticles the numberarticles to set
	 */
	public void setNumberarticles(Integer numberarticles) {
		this.numberarticles = numberarticles;
	}

	/**
	 * @param numberArticlesAccepted the numberArticlesAccepted to set
	 */
	public void setNumberArticlesAccepted(Integer numberArticlesAccepted) {
		this.numberArticlesAccepted = numberArticlesAccepted;
	}

	/**
	 * @param numberArticlesRejected the numberArticlesRejected to set
	 */
	public void setNumberArticlesRejected(Integer numberArticlesRejected) {
		this.numberArticlesRejected = numberArticlesRejected;
	}

	/**
	 * @param wss the wss to set
	 */
	public void setWss(Double wss) {
		this.wss = wss;
	}

	/**
	 * @param recall the recall to set
	 */
	public void setRecall(Double recall) {
		this.recall = recall;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Double error) {
		this.error = error;
	}

	/**
	 * @param rocArea the rocArea to set
	 */
	public void setRocArea(Double rocArea) {
		this.rocArea = rocArea;
	}

	/**
	 * @param fMeasure the fMeasure to set
	 */
	public void setfMeasure(Double fMeasure) {
		this.fMeasure = fMeasure;
	}

	/**
	 * @param numberArticlesTraining the numberArticlesTraining to set
	 */
	public void setNumberArticlesTraining(Integer numberArticlesTraining) {
		this.numberArticlesTraining = numberArticlesTraining;
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
		if (!(obj instanceof LearningStats)) {
			return false;
		}
		LearningStats other = (LearningStats) obj;
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
				"LearningStats [mapStudy=%s, algorithm=%s, numberarticles=%s, numberArticlesAccepted=%s, numberArticlesRejected=%s, wss=%s, recall=%s, accuracy=%s, error=%s, rocArea=%s, fMeasure=%s, numberArticlesTraining=%s]",
				mapStudy, algorithm, numberarticles, numberArticlesAccepted, numberArticlesRejected, wss, recall,
				accuracy, error, rocArea, fMeasure, numberArticlesTraining);
	}
}
