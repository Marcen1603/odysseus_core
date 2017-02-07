package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkitNotFoundException;

/**
 * This class manages installed NLP-Toolkits. New Toolkits can be registered with {@link ToolkitFactory#register(String, NLPToolkit)}
 * @see {@link NLPToolkit}
 */
public class ToolkitFactory {
	private static Map<String, Class<? extends NLPToolkit>> toolkits = new HashMap<>();
	
	/**
	 * Registers a new NLPToolkit with a name.
	 * @param name of the toolkit
	 * @param toolkit Class-Object
	 */
	public static void register(String name, Class<? extends NLPToolkit> toolkit){
		toolkits.put(name, toolkit);
	}
	
	/**
	 * Returns a toolkit-class-object with the specified name. Throws {@link NLPToolkitNotFoundException} if not found.
	 * @param name of the toolkit
	 * @return Class<? extends NLPToolkit> Class-Object
	 * @throws NLPToolkitNotFoundException if no toolkit is found
	 */
	public static Class<? extends NLPToolkit> get(String name) throws NLPToolkitNotFoundException{
		Class<? extends NLPToolkit> toolkit = toolkits.get(name);
		if(toolkit == null)
			throw new NLPToolkitNotFoundException();
		return toolkit;
	}
	
	/**
	 * Returns configured NLPToolkit-Object 
	 * @param name
	 * @param information included in pipeline
	 * @param configuration map containing model-configuration
	 * @return new NLPToolkit instance of specified Toolkit
	 * @throws NLPToolkitNotFoundException if no toolkit found
	 */
	public static NLPToolkit instantiate(String name, Set<String> information, HashMap<String, Option> configuration) throws NLPToolkitNotFoundException{
		Class<? extends NLPToolkit> clazz = get(name);
		try {
			NLPToolkit toolkit = clazz.getConstructor(Set.class, HashMap.class).newInstance(information, configuration);
			return toolkit;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ignored) {
		}
		return null;
	}
}
