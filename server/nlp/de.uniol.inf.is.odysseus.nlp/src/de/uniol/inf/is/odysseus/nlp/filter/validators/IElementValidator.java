package de.uniol.inf.is.odysseus.nlp.filter.validators;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionAtom;

public interface IElementValidator {
	/**
	 * The tokenId must increase if it did not match or it maches. If anything goes wrong it has to stay the same.
	 * @param element
	 * @param annotated
	 * @param tokenId 
	 * @return
	 */
	public boolean validate(ExpressionAtom element, Annotated annotated, AtomicInteger tokenId);
}
