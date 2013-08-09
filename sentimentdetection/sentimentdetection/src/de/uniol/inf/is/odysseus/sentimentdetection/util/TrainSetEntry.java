package de.uniol.inf.is.odysseus.sentimentdetection.util;

/**
 * @author Marc Preuschaft
 * 
 */
public class TrainSetEntry {

	private String record;
	private Integer truedecision;

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public Integer getTrueDecisio() {
		return truedecision;
	}

	public void setTrueDecision(Integer truedecision) {
		this.truedecision = truedecision;
	}

}
