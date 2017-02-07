package de.uniol.inf.is.odysseus.nlp.datastructure;


/**
 * Contains the beginning and end index of a token range. 
 */
public class Span {
	int start, end;
	
	/**
	 * Creates new Span Object
	 * @param begin start index of token range
	 * @param end end index of token range
	 * @see Span
	 */
	public Span(int start, int end) {
		assert end>=start;
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
