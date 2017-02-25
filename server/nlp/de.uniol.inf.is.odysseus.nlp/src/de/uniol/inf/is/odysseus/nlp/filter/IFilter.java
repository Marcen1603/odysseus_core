package de.uniol.inf.is.odysseus.nlp.filter;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;

public interface IFilter {
	public boolean filter(Annotated annotated);
}
