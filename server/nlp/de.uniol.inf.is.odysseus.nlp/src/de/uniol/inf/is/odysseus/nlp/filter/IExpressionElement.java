package de.uniol.inf.is.odysseus.nlp.filter;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;


public interface IExpressionElement {
	/**
	 * Validates the annotated-object at the token-index. Returns true if it contains one or more tokens that match the conditions specified in the subclasses.
	 * @param annotated
	 * @return token-index after first occurence of expression or -1 if not found
	 */
	public boolean validate(Annotated annotated, AtomicInteger token);
}
