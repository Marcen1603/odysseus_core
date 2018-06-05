package de.uniol.inf.is.odysseus.nlp.filter;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.filters.SimpleFilter;

/**
 * Interface of all filters (eg. {@link SimpleFilter}
 */
public interface IFilter {
	
	/**
	 * Runs the filter over an NLP-{@link Annotated}-Object
	 * @param annotated {@link Annotated}
	 * @return true if filter succeeded
	 */
	public boolean filter(Annotated annotated);
}
