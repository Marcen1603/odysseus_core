package de.uniol.inf.is.odysseus.nlp.filter.validators;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.filter.validators.elements.LemmaValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.elements.NERValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.elements.POSValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.elements.TokenValidator;

/**
 * Manages Element-Validators, these are validators that validate {@link Annotation}-conditions.
 */
public class ElementsValidatorManager{
	private static Map<String, IElementValidator> validators = new HashMap<>();

	static{
		put(TokenValidator.NAME, new TokenValidator());
		put(POSValidator.NAME, new POSValidator());
		put(NERValidator.NAME, new NERValidator());
		put(LemmaValidator.NAME, new LemmaValidator());
	}
	
	public static IElementValidator get(String key){
		return validators.get(key);
	}
	
	public static void put(String key, IElementValidator validator){
		validators.put(key, validator);
	}
}
