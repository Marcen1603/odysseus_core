package de.uniol.inf.is.odysseus.nlp.filter.validators.elements;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.PartsOfSpeech;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionAtom;
import de.uniol.inf.is.odysseus.nlp.filter.validators.IElementValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.ValidationAnnotatedException;

/**
 * Validator-class for finding part-of-speech-types.
 */
public class POSValidator implements IElementValidator {
	public final static String NAME = "pos";
	
	@Override
	public boolean validate(ExpressionAtom element, Annotated annotated, AtomicInteger tokenId) {
		String needle = element.getNeedle();
		PartsOfSpeech annotation = (PartsOfSpeech) annotated.getAnnotations().get(PartsOfSpeech.NAME);

		if(annotation == null)
			throw new ValidationAnnotatedException();
		
		String[] tags = annotation.getTags();
		
		if(tags.length <= tokenId.get())
			return false;
	
		if(tags[tokenId.get()].toLowerCase().equals(needle.toLowerCase())){
			tokenId.addAndGet(1);
			return true;
		}
		tokenId.addAndGet(1);
		return false;
	}

}
