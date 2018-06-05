package de.uniol.inf.is.odysseus.nlp.filter.validators.groups;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionSequence;
import de.uniol.inf.is.odysseus.nlp.filter.IExpressionElement;
import de.uniol.inf.is.odysseus.nlp.filter.validators.IGroupValidator;

/**
 * Objects of this kind validate {@link Annotated}, if a number of conditions are all fulfilled.
 */
public class AndValidator implements IGroupValidator {
	public static final String NAME = "and";

	@Override
	public boolean validate(ExpressionSequence sequence, Annotated annotated, AtomicInteger tokenId) {
		int index = tokenId.get();
		boolean validated = false;
		for(IExpressionElement element : sequence.getElements()){
			tokenId.set(index);
			validated = element.validate(annotated, tokenId);
			if(!validated)
				break;
		}
		
		return validated;
	}


}
