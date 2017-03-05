package de.uniol.inf.is.odysseus.nlp.filter;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.validators.GroupsValidatorManager;

/**
 * Eg. the group (and=["token"="get"]["lemma"="VB"]) is represented as an object of this class with type=and and the sequence ["token"="get"]["lemma"="VB"].
 */
public class ExpressionGroup implements IExpressionElement {
	private String type;
	private ExpressionSequence sequence;
	
	public ExpressionGroup(String type, ExpressionSequence sequence) {
		this.type = type;
		this.sequence = sequence;
	}

	@Override
	public boolean validate(Annotated annotated, AtomicInteger token) {
		return GroupsValidatorManager.get(type).validate(sequence, annotated, token);
	}
	

}
