package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

/**
 * 
 * @author Marc Preuschaft
 * 
 */
public abstract class AbstractClassifier implements IClassifier {

	protected String domain;
	protected int ngram = 1;
	protected int ngramUpTo = 1;
	protected boolean removeStopWords = false;
	protected boolean stemmWords = false;
	protected boolean debugModus = false;
	

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setNgram(int ngram) {
		this.ngram = ngram;
	}

	public void setNgramUpTo(int ngramUpTo) {
		this.ngramUpTo = ngramUpTo;
	}

	public void setRemoveStopWords(boolean removeStopWords) {
		this.removeStopWords = removeStopWords;
	}

	public void setStemmWords(boolean stemmWords) {
		this.stemmWords = stemmWords;
	}
	
	public void setDebugModus(boolean debugModus){
		this.debugModus = debugModus;
	}

	public String getDomain() {
		return domain;
	}

	public int getNgramUpTo() {
		return ngramUpTo;
	}

	public boolean getRemoveStopWords() {
		return removeStopWords;
	}

	public boolean getStemmWords() {
		return stemmWords;
	}
	
	public boolean getDebugModus(){
		return debugModus;	
	}
	
}
