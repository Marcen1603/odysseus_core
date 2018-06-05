package de.uniol.inf.is.odysseus.nlp.filter.validators;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionAtom;

/**
 * Interface for all element-validators (POS, Token, ...)
 */
public interface IElementValidator {
	/**
	 * Validates the {@link Annotated#getText()}-String at the specific index tokenId.
	 * Eg. is true if there is a pos of the type NN in an POS-Implementation.
	 * The tokenId must increase if it did not match or it maches. If anything goes wrong it has to stay the same.
	 * @param element
	 * @param annotated
	 * @param tokenId 
	 * @return
	 */
	public boolean validate(ExpressionAtom element, Annotated annotated, AtomicInteger tokenId);
}
