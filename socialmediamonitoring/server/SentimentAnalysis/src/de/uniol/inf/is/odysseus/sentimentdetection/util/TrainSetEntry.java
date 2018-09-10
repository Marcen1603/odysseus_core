package de.uniol.inf.is.odysseus.sentimentdetection.util;

/**
 * TrainSetEntry is needed to train the classifier
 * @author Marc Preuschaft
 * 
 */
public class TrainSetEntry {

	private String sentence;
	private Integer truedecision;

	/**
	 * return the sentence
	 * @return
	 */
	public String getSentence() {
		return sentence;
	}

	/**
	 * set the sentence 
	 * @param sentence 
	 */
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	/**
	 * return the true decision of the sentence
	 * @return
	 */
	public Integer getTrueDecisio() {
		return truedecision;
	}

	/**
	 * set the true decision of the sentence
	 * @param truedecision
	 */
	public void setTrueDecision(Integer truedecision) {
		this.truedecision = truedecision;
	}

}
