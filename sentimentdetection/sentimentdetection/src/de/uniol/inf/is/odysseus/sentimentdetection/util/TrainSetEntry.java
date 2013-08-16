package de.uniol.inf.is.odysseus.sentimentdetection.util;

/**
 * @author Marc Preuschaft
 * 
 */
public class TrainSetEntry {

	private String record;
	private Integer truedecision;

	/**
	 * return the record
	 * @return
	 */
	public String getRecord() {
		return record;
	}

	/**
	 * set the record
	 * @param record
	 */
	public void setRecord(String record) {
		this.record = record;
	}

	/**
	 * return the true decision of the record
	 * @return
	 */
	public Integer getTrueDecisio() {
		return truedecision;
	}

	/**
	 * set the true decision of the record
	 * @param truedecision
	 */
	public void setTrueDecision(Integer truedecision) {
		this.truedecision = truedecision;
	}

}
