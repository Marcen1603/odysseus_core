package de.uniol.inf.is.odysseus.iql.basic.types;

public class Range {
	private final int from;
	
	private final int to;
	
	public Range(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}
	
	
}
