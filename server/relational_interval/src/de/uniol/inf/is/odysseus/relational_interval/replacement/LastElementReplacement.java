package de.uniol.inf.is.odysseus.relational_interval.replacement;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.IClone;

public class LastElementReplacement<T extends IClone> extends AbstractReplacement<T> {

	@Override
	public List<Object> determineReplacements(T lastObject, T newObject,
			int noMissingValues, int valueAttributePos) {
		List<Object> vals = new ArrayList<>(noMissingValues);
		for (int i=1;i<noMissingValues;i++){
			vals.add(lastObject.clone());
		}
		return vals;
	}

	@Override
	public String getName() {
		return "Last";
	}

}
