package de.uniol.inf.is.odysseus.relational_interval.replacement;

import java.util.List;

public interface IReplacement<T> {

	List<Object> determineReplacements(
			T lastObject,
			T newObject, int noMissingValues,
			int valueAttributePos);

	String getName();

}
