package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Map;


public abstract class AbstractTextualDistance implements ITextualDistance {

	protected final Map<String, String> queryTextualAttributes;
	
	protected AbstractTextualDistance(final Map<String, String> queryTextualAttributes) {
		this.queryTextualAttributes = queryTextualAttributes;
	}
}
