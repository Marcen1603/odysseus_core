package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import de.uniol.inf.is.odysseus.nlp.datastructure.Annotation;;

/**
 * Token annotation defines a set of tokens contained in an analyzed text.
 */
public class Tokens extends Annotation {

	public Tokens(String[] tokens){
		this.tokens = tokens;
	}
	
	private String[] tokens;

	
	public String[] getTokens(){
		return tokens;
	}


	@Override
	public Object toObject() {
		return tokens;
	}

}
