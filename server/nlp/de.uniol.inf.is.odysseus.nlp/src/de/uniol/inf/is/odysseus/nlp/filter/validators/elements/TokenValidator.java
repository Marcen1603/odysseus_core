package de.uniol.inf.is.odysseus.nlp.filter.validators.elements;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionAtom;
import de.uniol.inf.is.odysseus.nlp.filter.validators.IElementValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.ValidationAnnotatedException;

/**
 * Validator-class for finding tokens.
 */
public class TokenValidator implements IElementValidator {
	public static final String NAME = "token";
	
	
	@Override
	public boolean validate(ExpressionAtom element, Annotated annotated, AtomicInteger tokenId) {
		String needle = element.getNeedle();
		Tokens annotation = (Tokens) annotated.getAnnotations().get(Tokens.NAME);
		
		if(annotation == null)
			throw new ValidationAnnotatedException();
		
		String[] tokens = annotation.getTokens();
		
		if(tokens.length <= tokenId.get())
			return false;
	
		if(tokens[tokenId.get()].toLowerCase().equals(needle.toLowerCase())){
			tokenId.addAndGet(1);
			return true;
		}
		tokenId.addAndGet(1);
		return false;
	}
}
