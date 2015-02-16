package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.Map;

public abstract class AbstractQueryTrajectory<E> extends AbstractTrajectory<E, RawQueryTrajectory> implements IQueryTrajectory<E> {

	private final E convertedData;
	
	private final Map<String, String> textualAttributes;
	
	protected AbstractQueryTrajectory(final RawQueryTrajectory rawTrajectory, final E convertedData, 
			final Map<String, String> textualAttributes) {
		super(rawTrajectory);
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
