package de.uniol.inf.is.odysseus.nlp.filter.validators.groups;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionSequence;

/**
 * Objects of this kind validate {@link Annotated}, if no conditions of a specific condition-set are fulfilled.
 */
public class NAndValidator extends AndValidator {
	public static final String NAME = "nand";

	@Override
	public boolean validate(ExpressionSequence sequence, Annotated annotated, AtomicInteger tokenId) {
		return !super.validate(sequence, annotated, tokenId);
	}
}
