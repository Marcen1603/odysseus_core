package de.uniol.inf.is.odysseus.script.parser;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;

public final class ReplacementContainer {
	
	public static final String PARAMETER_KEY = "#";
	public static final String REPLACEMENT_DEFINITION_KEY = "DEFINE";

	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";
	
	private static final Map<String, String> defaultReplacements = Maps.newHashMap();
	private final Map<String, String> replacements = Maps.newHashMap();
	private Context context;
	
	public ReplacementContainer() {
		replacements.putAll(defaultReplacements);
	}

	public void connect(Context context) {
		Preconditions.checkNotNull(context, "Context must not be null");
		this.context = context;
		
		fromContextToReplacements();
		fromReplacementsToContext();
	}

	private void fromContextToReplacements() {
		for( String key : context.getKeys()) {
			Object value = context.get(key);
			
			replacements.put(key, value.toString());
		}
	}

	private void fromReplacementsToContext() {
		for( String key : replacements.keySet() ) {
			context.putOrReplace(key, replacements.get(key));
		}
	}
	
	public String use( String lineToReplace ) throws ReplacementException {
		Preconditions.checkNotNull(lineToReplace, "Line to use replacements must not be null!");

		int posStart = lineToReplace.indexOf(REPLACEMENT_START_KEY);
		while (posStart != -1) {
			int posEnd = posStart + 1 + lineToReplace.substring(posStart + 1).indexOf(REPLACEMENT_END_KEY);
			if (posEnd == posStart) {
				// end not found
				break;
			}
			if (posEnd != -1 && posStart < posEnd) {

				String key = lineToReplace.substring(posStart + REPLACEMENT_START_KEY.length(), posEnd);
				String replacement = replacements.get(key.toUpperCase());
				if (replacement == null) {
					throw new ReplacementException("Replacement key " + key + " not defined or has no value!");
				}
				lineToReplace = lineToReplace.substring(0, posStart) + replacement + lineToReplace.substring(posEnd + REPLACEMENT_END_KEY.length());
			}
			int searchAt = posStart + REPLACEMENT_START_KEY.length();
			posStart = lineToReplace.indexOf(REPLACEMENT_START_KEY, searchAt);
		}

		return lineToReplace.trim();
	}

	public boolean parse(String line) {
		Preconditions.checkNotNull(line, "Line to parse must not be null!");

		if (line.indexOf(PARAMETER_KEY + REPLACEMENT_DEFINITION_KEY) != -1) {
			String[] parts = line.split(" |\t", 3);
			if (parts.length >= 3) {
				String key = parts[1].trim().toUpperCase();
				String value = parts[2].trim();
				
				put(key, value);
			}
			return true;
		}
		
		if (line.indexOf(PARAMETER_KEY + IfController.UNDEF_KEY) != -1) {
			String[] parts = line.trim().split(" |\t", 3);
			String key = parts[1].toUpperCase();
			
			removeImpl(key);
			
			return true;
		}
		
		return false;
	}
	
	public void put( String key, String value ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Key for replacement not be null or empty");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "Value for replacement must not be null or empty");
		
		putImpl(key, value);
	}

	private void removeImpl(String key) {
		replacements.remove(key);
		context.remove(key);
	}

	private void putImpl(String key, String value) {
		replacements.put(key, value);
		context.putOrReplace(key, value);
	}
	
	public static void addDefault( String key, String value ) {
		defaultReplacements.put(key, value);
	}
	
	public Map<String, String> toMap() {
		return Maps.newHashMap(replacements);
	}
}
