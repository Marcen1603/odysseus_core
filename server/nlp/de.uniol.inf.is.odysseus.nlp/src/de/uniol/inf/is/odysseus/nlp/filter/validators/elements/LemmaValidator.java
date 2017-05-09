package de.uniol.inf.is.odysseus.nlp.filter.validators.elements;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Lemmas;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionAtom;
import de.uniol.inf.is.odysseus.nlp.filter.validators.IElementValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.ValidationAnnotatedException;


/**
 * Validator class for finding lemmas.
 */
public class LemmaValidator implements IElementValidator {
	public static final String NAME = "lemma";
	
	
	@Override
	public boolean validate(ExpressionAtom element, Annotated annotated, AtomicInteger tokenId) {
		String needle = element.getNeedle();
		Lemmas annotation = (Lemmas) annotated.getAnnotations().get(Lemmas.NAME);
		
		if(annotation == null)
			throw new ValidationAnnotatedException();
		
		String[] lemmas = annotation.getLemmas();
		
		if(lemmas.length <= tokenId.get())
			return false;
	
		if(lemmas[tokenId.get()].toLowerCase().equals(needle.toLowerCase())){
			tokenId.addAndGet(1);
			return true;
		}
		tokenId.addAndGet(1);
		return false;
	}

}
