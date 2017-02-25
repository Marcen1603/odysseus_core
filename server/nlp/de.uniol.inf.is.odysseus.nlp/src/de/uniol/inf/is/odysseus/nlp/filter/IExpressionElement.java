package de.uniol.inf.is.odysseus.nlp.filter;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;


public interface IExpressionElement {
	/**
	 * 
	 * @param annotated
	 * @return token-index after first occurence of expression or -1 if not found
	 */
	public boolean validate(Annotated annotated, AtomicInteger token);
}
