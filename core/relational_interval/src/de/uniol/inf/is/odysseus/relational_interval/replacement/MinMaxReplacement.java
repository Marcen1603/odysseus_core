package de.uniol.inf.is.odysseus.relational_interval.replacement;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class MinMaxReplacement<T extends Tuple<?>> extends
		AbstractReplacement<T> {

	private static String MIN = "MIN";
	private static String MAX = "MAX";
	
	
	private boolean isMin;

	public MinMaxReplacement(boolean isMin) {
		this.isMin = isMin;
	}

	@Override
	public List<Object> determineReplacements(T lastObject, T newObject,
			int noMissingValues, int valueAttributePos) {
		List<Object> vals = new ArrayList<>(noMissingValues);
		for (int i=1;i<noMissingValues;i++){
			Tuple<?> newTuple = lastObject.clone();
			double newValue;
			if (isMin){
				newValue = Math.min(((Number)lastObject.getAttribute(valueAttributePos)).doubleValue(), 
						((Number) newObject.getAttribute(valueAttributePos)).doubleValue());
			}else{
				newValue = Math.max(((Number)lastObject.getAttribute(valueAttributePos)).doubleValue(), 
						((Number) newObject.getAttribute(valueAttributePos)).doubleValue());
			}
			newTuple.setAttribute(valueAttributePos, newValue);

			vals.add(newTuple);
		}
		return vals;
	}

	@Override
	public String getName() {
		return isMin?MIN:MAX;
	}

}
