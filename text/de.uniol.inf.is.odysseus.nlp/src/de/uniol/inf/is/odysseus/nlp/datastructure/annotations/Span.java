package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;

/**
 * Contains the beginning and end index of an integer range. 
 */
public class Span implements IClone{
	int start, end;
	private String text;
	
	/**
	 * Creates new Span Object
	 * @param begin start index
	 * @param end end index
	 * @param text of span
	 * @throws InvalidSpanException if the end index is lower than the start index
	 * @see Span
	 */
	public Span(int start, int end, String text) throws InvalidSpanException {
		if(end < start){
			throw new InvalidSpanException();
		}
		this.start = start;
		this.end = end;
		this.text = text;
	}
	

	@Override
	public IClone clone(){
		try {
			return new Span(start, end, text);
		} catch (InvalidSpanException ignored) {
			//will never be thrown
			return null;
		}
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
	
	/**
	 * Returns text of span
	 * @return text
	 */
	public String getText(){
		return text;
	}

	@Override
	public int hashCode() {
		return start*31+end;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Span){
			return this.hashCode() == obj.hashCode();
		}
		return false;
	}
}
