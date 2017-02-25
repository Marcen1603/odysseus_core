package de.uniol.inf.is.odysseus.nlp.filter.validators.groups;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionSequence;

public class NAndValidator extends AndValidator {
	public static final String NAME = "nand";

	@Override
	public boolean validate(ExpressionSequence sequence, Annotated annotated, AtomicInteger tokenId) {
		return !super.validate(sequence, annotated, tokenId);
	}
}
