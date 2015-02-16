package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.Map;

public abstract class AbstractQueryTrajectory<E> implements IQueryTrajectory<E> {

	private final E convertedData;
	
	private final Map<String, String> textualAttributes;
	
	protected AbstractQueryTrajectory(final E convertedData, final Map<String, String> textualAttributes) {
		this.convertedData = convertedData;
		this.textualAttributes = textualAttributes;
	}
	
	@Override
	public E getData() {
		return this.convertedData;
	}
	
	@Override
	public Map<String, String> getTextualAttributes() {
		return this.textualAttributes;
	}
}
