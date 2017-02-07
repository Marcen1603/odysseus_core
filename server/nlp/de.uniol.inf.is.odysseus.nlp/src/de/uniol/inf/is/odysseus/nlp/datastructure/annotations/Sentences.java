package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import de.uniol.inf.is.odysseus.nlp.datastructure.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.Span;

/**
 * Annotation for wrapping information about sentences in a text with the usage of token indexes.
 * 
 * @see Span
 */
public class Sentences extends Annotation {

	private Span[] sentences;
	
	/**
	 * Annotation for wrapping information about sentences in a text with the usage of token indexes.
	 * @see Sentences
	 * @param sentences Array of span objects. Each span object defines a single sentence by the start and end token index.
	 */
	public Sentences(Span[] sentences){
		this.sentences = sentences;
	}
	
	public Span[] getSentences(){
		return sentences;
	}
	
	@Override
	public Object toObject() {
		int[][] sentenceSpans = new int[sentences.length][2];
		for(int i = 0; i < sentences.length; i++){
			sentenceSpans[i] = sentences[i].toArray();
		}
		return sentenceSpans;
	}

}
