package de.uniol.inf.is.odysseus.nlp.filter.validators;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionSequence;

/**
 * Interface for all groups (and, or, nand)
 */
public interface IGroupValidator {

	/**
	 * Validates the {@link Annotated#getText()}-String at the specific index tokenId.
	 * The tokenId must increase if it did not match or it maches. If anything goes wrong it has to stay the same.
	 * @param element
	 * @param annotated
	 * @param tokenId 
	 * @return
	 */
	public boolean validate(ExpressionSequence sequence, Annotated annotated, AtomicInteger tokenId);
}
