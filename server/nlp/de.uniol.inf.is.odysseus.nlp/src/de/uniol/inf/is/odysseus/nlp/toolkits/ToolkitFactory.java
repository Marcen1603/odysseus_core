package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.toolkits.exception.NLPInformationNotSupportedException;
import de.uniol.inf.is.odysseus.nlp.toolkits.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.toolkits.exception.ToolkitNotFoundException;

public class ToolkitFactory {
	private static HashMap<String, Class<? extends NLPToolkit>> toolkits = new HashMap<>();
	
	public static void register(String toolkit, Class<? extends NLPToolkit> clazz){
		toolkits.put(toolkit, clazz);
	}

	public static NLPToolkit get(String toolkit, Set<String> information, HashMap<String, Option> configuration) throws ToolkitNotFoundException, NLPInformationNotSupportedException, NLPModelNotFoundException {
		Class<? extends NLPToolkit> clazz = toolkits.get(toolkit);
		if(clazz != null){
			try {
				return clazz.getConstructor(Set.class, HashMap.class).newInstance(information, configuration);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException ignored) {
			} catch (InvocationTargetException e){
				if(e.getCause() instanceof NLPInformationNotSupportedException){
					throw (NLPInformationNotSupportedException) e.getTargetException();
				}else if(e.getCause() instanceof NLPModelNotFoundException){
					throw (NLPModelNotFoundException) e.getTargetException();
				}else{
					e.getTargetException().printStackTrace();
				}
			}
		}
		throw new ToolkitNotFoundException();
	}

}
