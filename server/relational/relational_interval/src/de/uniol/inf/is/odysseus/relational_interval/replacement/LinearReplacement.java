package de.uniol.inf.is.odysseus.relational_interval.replacement;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class LinearReplacement extends  AbstractReplacement<Tuple<? extends ITimeInterval>>{
	
	public static final String NAME = "Linear"; 

	@Override
	public List<Object> determineReplacements(
			Tuple<? extends ITimeInterval> lastObject,
			Tuple<? extends ITimeInterval> newObject, int noMissingValues,
			int valueAttributePos) {
		List<Object> missing = new ArrayList<Object>(noMissingValues);
		double oldValue = lastObject.getAttribute(valueAttributePos);
		double newValue = newObject.getAttribute(valueAttributePos);
		double factor = (newValue - oldValue) / noMissingValues;
		for (int i = 1; i < noMissingValues; i++) {
			missing.add(oldValue + (factor * i));
		}
		return missing;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}
