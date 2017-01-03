package de.uniol.inf.is.odysseus.nlp.toolkits.annotations;

import java.util.HashMap;
import java.util.List;

public class TokenAnnotation implements IAnnotation {
	public static final String NAME = "token";
	
	/**
	 * Includes tokens by sentence-index.
	 */
	private List<String[]> tokens;
	private int sentence;

	public TokenAnnotation(List<String[]> tokens){
		this.tokens = tokens;
	}
	
	public List<String[]> getTokens(){
		return tokens;
	}

	public int getSentence() {
		return sentence;
	}
	
}
