package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPToolkitNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;

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
	public static NLPToolkit instantiate(String name, List<String> pipeline, Map<String, Option> configuration) throws NLPToolkitNotFoundException{
		Class<? extends NLPToolkit> clazz = get(name);
		try {
			NLPToolkit toolkit = clazz.getConstructor(List.class, Map.class).newInstance(pipeline, configuration);
			return toolkit;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ignored) {
		}
		return null;
	}
	
	public static NLPToolkit getEmptyToolkit(String toolkitName) throws NLPToolkitNotFoundException, NLPModelNotFoundException{
		Class<? extends NLPToolkit> clazz = get(toolkitName);
		NLPToolkit toolkit;
		try {
			toolkit = clazz.newInstance();
			return toolkit;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
