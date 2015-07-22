package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value;

public class ParallelIntraOperatorSettingValueElement {
	private int individualDegree = 0;
	private int individualBuffersize = 0;

	public ParallelIntraOperatorSettingValueElement(int individualDegree, int individualBuffersize){
		this.individualDegree = individualDegree;
		this.individualBuffersize = individualBuffersize;
	}

	public int getIndividualDegree() {
		return individualDegree;
	}

	public void setIndividualDegree(int individualDegree) {
		this.individualDegree = individualDegree;
	}

	public int getIndividualBuffersize() {
		return individualBuffersize;
	}

	public void setIndividualBuffersize(int individualBuffersize) {
		this.individualBuffersize = individualBuffersize;
	}
	

}
