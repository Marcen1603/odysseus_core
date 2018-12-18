package de.uniol.inf.is.odysseus.relational_interval.replacement;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReplacementRegistry {
	private static Map<String, IReplacement<?>> reg = new HashMap<>();
	private static List<String> keys = new LinkedList<>();
	
	static{
		addReplacement2(new LinearReplacement());
		addReplacement2(new LastElementReplacement<>());
		addReplacement2(new MinMaxReplacement<>(true));
		addReplacement2(new MinMaxReplacement<>(false));
	}

	public static void addReplacement2(IReplacement<?> replacement){
		if (!reg.containsKey(replacement.getName().toUpperCase())){
			reg.put(replacement.getName().toUpperCase(), replacement);
			keys.add(replacement.getName().toUpperCase());
		}
	}
	public void addReplacement(IReplacement<?> replacement){
		addReplacement2(replacement);
	}
	
	public void removeReplacement(IReplacement<?> replacement){
		if (reg.containsKey(replacement.getName().toUpperCase())){
			reg.remove(replacement.getName().toUpperCase());
			keys.remove(replacement.getName().toUpperCase());
		}
	}
	
	public static IReplacement<?> getReplacement(String name){
		return reg.get(name.toUpperCase());
	}

	public static List<String> getKeys(){
		return Collections.unmodifiableList(keys);
	}
}
