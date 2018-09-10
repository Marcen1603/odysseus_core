package de.uniol.inf.is.odysseus.nlp.filter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.nlp.filter.exception.FilterNotFoundException;
import de.uniol.inf.is.odysseus.nlp.filter.filters.SimpleFilter;

/**
 * Manages the filters (eg. simple) and allows to create new filters of a specific kind and expression.
 */
public class FilterFactory {
	private static Map<String, Class<? extends IFilter>> filters = new HashMap<>();
	
	static{
		register("simple", SimpleFilter.class);
	}
	
	public static void register(String type, Class<? extends IFilter> filter){
		filters.put(type, filter);
	}
	
	public static IFilter get(String type, String expression) throws FilterNotFoundException{
		IFilter filter = null;
		try {
			Class<?> clazz = filters.get(type);
			if(clazz == null)
				throw new FilterNotFoundException();
			filter = filters.get(type).getConstructor(String.class).newInstance(expression);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException ignored) {
		} catch (NoSuchMethodException e) {
			throw new ParameterException("Constructor with String parameter not implemented in "+type+" filter-class");
		} catch (InvocationTargetException e) {
			if(e.getCause() instanceof MalformedExpression)
				throw new ParameterException("Expression string is not correct.");
			
			e.printStackTrace();
		}
		
		
		return filter;
	}
}
