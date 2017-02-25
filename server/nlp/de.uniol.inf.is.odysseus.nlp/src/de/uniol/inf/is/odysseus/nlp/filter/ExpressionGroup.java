package de.uniol.inf.is.odysseus.nlp.filter;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.validators.GroupsValidatorManager;

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
