package de.uniol.inf.is.odysseus.nlp.toolkits.annotations;

public class SentenceAnnotation implements IAnnotation{
	public static final String NAME = "sentence";
	
	private String[] sentences;

	public SentenceAnnotation(String[] sentences){
		this.sentences = sentences;
	}
	
	public String[] getSentences(){
		return sentences;
	}
}
