package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@Deprecated
public class CQLDictionaryProvider 
{

	public static Map<String, CQLDictionary> map = new HashMap<>();
	
	/** Returns a {@link CQLDictionary} with the given ISession object. 
	 *  If there is no dictionary with the specified ISession object,
	 *  a new dictionary will be created.
	 */
	public static CQLDictionary getDictionary(ISession user) 
	{
		CQLDictionary c = map.get(user.getUser().getName());
		if(c == null)
		{
			c = CQLDictionary.create(user.getUser().getName());
			map.put(user.getUser().getName(), c);
		}
		return c; 
	}
	
	public static CQLDictionary removeDictionary(ISession user)
	{
		getDictionary(user).clear();
		return map.remove(user.getUser().getName());
	}
	
	public static CQLDictionary getCurrentUsersDictionary()
	{
		return getDictionary(getCurrentUser());
	}
	
	public static ISession getCurrentUser()
	{
//		return UserManagementProvider.getSessionmanagement().loginSuperUser(null, 
//				UserManagementProvider.getDefaultTenant().getName());
		return null;
	}
	
}
