package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CQLDictionaryProvider 
{

	public static Map<ISession, CQLDictionary> map = new HashMap<>();
	
	/** Returns a {@link CQLDictionary} with the given ISession object. 
	 *  If there is no dictionary with the specified ISession object,
	 *  a new dictionary will be created.
	 **/
	public static CQLDictionary getDictionary(ISession user) 
	{
		CQLDictionary c = map.get(user);
		if(c == null)
		{
			c = CQLDictionary.create(user);
			map.put(user, c);
		}
		return c; 
	}
	
	public static CQLDictionary removeDictionary(ISession user)
	{
		return map.remove(user);
	}
	
}
