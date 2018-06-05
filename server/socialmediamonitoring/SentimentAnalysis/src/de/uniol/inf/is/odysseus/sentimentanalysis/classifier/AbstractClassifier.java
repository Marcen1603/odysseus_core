package de.uniol.inf.is.odysseus.sentimentanalysis.classifier;

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
	

	@Override
	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public void setNgram(int ngram) {
		this.ngram = ngram;
	}

	@Override
	public void setNgramUpTo(int ngramUpTo) {
		this.ngramUpTo = ngramUpTo;
	}

	@Override
	public void setRemoveStopWords(boolean removeStopWords) {
		this.removeStopWords = removeStopWords;
	}

	@Override
	public void setStemmWords(boolean stemmWords) {
		this.stemmWords = stemmWords;
	}
	
	@Override
	public void setDebugModus(boolean debugModus){
		this.debugModus = debugModus;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	public int getNgramUpTo() {
		return ngramUpTo;
	}

	@Override
	public boolean getRemoveStopWords() {
		return removeStopWords;
	}

	@Override
	public boolean getStemmWords() {
		return stemmWords;
	}
	
	@Override
	public boolean getDebugModus(){
		return debugModus;	
	}
	
}
