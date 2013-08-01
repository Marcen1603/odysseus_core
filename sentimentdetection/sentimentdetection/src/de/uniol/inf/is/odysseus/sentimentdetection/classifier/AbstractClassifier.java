package de.uniol.inf.is.odysseus.sentimentdetection.classifier;



public abstract class AbstractClassifier implements IClassifier{

	protected String domain;
	protected int ngram;


	public void setNgram(int ngram){
		this.ngram = ngram;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}

	
	


}
