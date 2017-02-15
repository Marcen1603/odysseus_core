package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;

/**
 * Contains the beginning and end index of a token range. 
 */
public class Span {
	int start, end;
	
	/**
	 * Creates new Span Object
	 * @param begin start index of token range
	 * @param end end index of token range
	 * @throws InvalidSpanException if the end index is lower than the start index
	 * @see Span
	 */
	public Span(int start, int end) throws InvalidSpanException {
		if(end < start){
			throw new InvalidSpanException();
		}
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	/**
	 * Creates array containing the keys start and end.
	 * @return Array of begin and end numbers to define the range(start, end)
	 */
	public int[] toArray(){
		return new int[]{start, end};
	}
	
}
