package de.uniol.inf.is.odysseus.sentimentdetection.classifier;



public abstract class AbstractClassifier implements IClassifier{

	protected String domain;
	protected int ngram = 1;
	protected boolean removeStopWords = false;
	protected boolean stemmWords = false;
	protected int ngramUpTo = 1;


	public void setNgram(int ngram){
		this.ngram = ngram;
	}
	
	public void setRemoveStopWords(boolean removeStopWords){
		this.removeStopWords = removeStopWords;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public int getNgramUpTo(){
		return ngramUpTo;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public boolean getRemoveStopWords(){
		return removeStopWords;
	}
	
	public void setStemmWords(boolean stemmWords){
		this.stemmWords = stemmWords;
	}
	
	public boolean getStemmWords(){
		return stemmWords;
	}

	
	public void setNgramUpTo(int ngramUpTo){
		this.ngramUpTo = ngramUpTo;
	}


	


}
