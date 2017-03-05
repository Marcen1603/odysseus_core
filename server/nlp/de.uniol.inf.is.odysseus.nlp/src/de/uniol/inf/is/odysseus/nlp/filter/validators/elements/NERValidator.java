package de.uniol.inf.is.odysseus.nlp.filter.validators.elements;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.NamedEntities;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.NamedEntity;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.filter.ExpressionAtom;
import de.uniol.inf.is.odysseus.nlp.filter.validators.IElementValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.ValidationAnnotatedException;

/**
 * Validator-class for finding named-entities.
 */
public class NERValidator implements IElementValidator {
	public static final String NAME = "ne";
	@Override
	public boolean validate(ExpressionAtom element, Annotated annotated, AtomicInteger tokenId) {
		NamedEntities annotation = (NamedEntities) annotated.getAnnotations().get(NamedEntities.NAME);
		String[] tokens = ((Tokens) annotated.getAnnotations().get(Tokens.NAME)).getTokens();

		if(tokenId.get() >= tokens.length)
			return false;
			
		if(annotation == null)
			throw new ValidationAnnotatedException();
		
		String needle = element.getNeedle(); //in this case: type of named-entity
		Object object = annotation.getAnnotations().get(needle);
		if(object instanceof NamedEntity){
			NamedEntity possibilities = (NamedEntity) object;
			Set<Span> spans = possibilities.getSpans();
			for(Span span: spans){
				if(tokenId.get() >= span.getStart() && tokenId.get() < span.getEnd()){
					tokenId.set(span.getEnd());
					return true;
				}
			}
		}
		
		tokenId.addAndGet(1);
		return false;
	}

}
