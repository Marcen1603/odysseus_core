package de.uniol.inf.is.odysseus.nlp.filter.validators;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionSequence;

public interface IGroupValidator {
	public boolean validate(ExpressionSequence sequence, Annotated annotated, AtomicInteger tokenId);
}
