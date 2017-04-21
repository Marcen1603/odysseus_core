package de.uniol.inf.is.odysseus.nlp.filter.validators;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.nlp.filter.validators.groups.AndValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.groups.NAndValidator;
import de.uniol.inf.is.odysseus.nlp.filter.validators.groups.OrValidator;


/**
 * Manages group-validators (eg. and, nand, or)
 */
public class GroupsValidatorManager{
	private static Map<String, IGroupValidator> validators = new HashMap<>();
	
	static{
		validators.put(AndValidator.NAME, new AndValidator());
		validators.put(OrValidator.NAME, new OrValidator());
		validators.put(NAndValidator.NAME, new OrValidator());
	}
	
	public static IGroupValidator get(String key){
		return validators.get(key);
	}
	
	public static void put(String key, IGroupValidator validator){
		validators.put(key, validator);
	}
}
