package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;

/**
 * Annotation for wrapping information about sentences in a text with the usage of character indices.
 * 
 * @see Span
 */
public class Sentences extends Annotation {
	public static final String NAME = "sentences";

	private Span[] sentences;
	
	/**
	 * Annotation for wrapping information about sentences in a text with the usage of character indices.
	 * @see Sentences
	 * @param sentences Array of span objects. Each span object defines a single sentence by the start and end character index.
	 */
	public Sentences(Span[] sentences){
		this.sentences = sentences;
	}
	
	public Span[] getSentences(){
		return sentences;
	}
	
	@Override
	public Object toObject() {
		String[] sentences = new String[this.sentences.length];
		for(int i = 0; i < this.sentences.length; i++){
			sentences[i] = this.sentences[i].getText();
		}
		return sentences;
	}

	@Override
	public IClone clone(){
		Span[] newSpans = new Span[sentences.length];
		for(int i = 0; i < sentences.length; i++)
			newSpans[i] = (Span) sentences[i].clone();
		return new Sentences(newSpans);
	}
}
