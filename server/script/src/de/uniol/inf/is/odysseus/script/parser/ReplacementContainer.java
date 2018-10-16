package de.uniol.inf.is.odysseus.script.parser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;

public final class ReplacementContainer {

	private static final InfoService INFO_SERVICE = InfoServiceFactory.getInfoService(ReplacementContainer.class);
	
	public static final String PARAMETER_KEY = "#";
	public static final String REPLACEMENT_DEFINITION_KEY = "DEFINE";
	private static final String UNDEF_KEY = "UNDEF";
	private static final String EVAL_KEY = "EVAL";

	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";

	private static final Map<String, String> defaultReplacements = Maps.newHashMap();
	private final Map<String, Serializable> replacements = Maps.newHashMap();
	private final Map<String, IReplacementProvider> replacementProviders = Maps.newHashMap();

	private Context currentContext;
	private Context context;

	public ReplacementContainer(Map<String, IReplacementProvider> providerMap) {
		// Preconditions.checkNotNull(providerMap, "Map of replacementProviders must not be null!");
		
		replacements.putAll(defaultReplacements);
		replacementProviders.putAll(providerMap);
	}

	public void connect(Context context) {
		// Preconditions.checkNotNull(context, "Context must not be null");
		this.context = context;
		this.currentContext = context;

		fromContextToReplacements();
		fromReplacementsToContext();
	}

	// TODO: this assumes that each context entry is a replacement
	private void fromContextToReplacements() {
		for (String key : context.getKeys()) {
			Serializable value = context.get(key);
			replacements.put(key, value);
		}
	}

	private void fromReplacementsToContext() {
		for (String key : replacements.keySet()) {
			context.putOrReplace(key, replacements.get(key));
			currentContext.putOrReplace(key, replacements.get(key));
		}
	}

	public String use(String lineToReplace) throws ReplacementException {
		// Preconditions.checkNotNull(lineToReplace, "Line to use replacements must not be null!");

		int posStart = lineToReplace.indexOf(REPLACEMENT_START_KEY);
		while (posStart != -1) {
			int posEnd = posStart + 1 + lineToReplace.substring(posStart + 1).indexOf(REPLACEMENT_END_KEY);
			if (posEnd == posStart) {
				// end not found
				break;
			}
			if (posEnd != -1 && posStart < posEnd) {

				String key = lineToReplace.substring(posStart + REPLACEMENT_START_KEY.length(), posEnd);
				
				// direct replacements are prioritized
				Serializable replacementSer = replacements.get(key.toUpperCase());
				
				if( replacementSer == null ) {
					IReplacementProvider provider = replacementProviders.get(key.toUpperCase());
					if( provider != null ) {
						String replacement = provider.getReplacementValue(key.toUpperCase());
						if( replacement == null || replacement == "" ) {
							INFO_SERVICE.warning("ReplacementKey '" + key.toUpperCase() + " is replaced to empty string!");
							replacementSer = "";
						} else {
							replacementSer = replacement;
						}
					}
				}
				
				if (replacementSer == null) {
					throw new ReplacementException("Replacement key " + key + " not defined or has no value!");
				}
				lineToReplace = lineToReplace.substring(0, posStart) + replacementSer.toString() + lineToReplace.substring(posEnd + REPLACEMENT_END_KEY.length());
			}
			int searchAt = posStart + REPLACEMENT_START_KEY.length();
			posStart = lineToReplace.indexOf(REPLACEMENT_START_KEY, searchAt);
		}

		return lineToReplace.trim();
	}

	public boolean parse(String line) throws OdysseusScriptException {
		// Preconditions.checkNotNull(line, "Line to parse must not be null!");

		currentContext = context.copy();

		if (line.indexOf(PARAMETER_KEY + REPLACEMENT_DEFINITION_KEY) != -1) {
			String[] parts = line.split(" |\t", 3);
			if (parts.length >= 3) {
				String key = parts[1].trim().toUpperCase();
				String value = parts[2].trim();

				put(key, value);
			}
			return true;
		}

		if (line.indexOf(PARAMETER_KEY + UNDEF_KEY) != -1) {
			String[] parts = line.trim().split(" |\t", 3);
			String key = parts[1].toUpperCase();

			removeImpl(key);

			return true;
		}
		
		if( line.indexOf(PARAMETER_KEY + EVAL_KEY) != -1 ) {
			line = line.trim();
			
			String key = null;
			String expressionString = null;
			int equalPos = line.indexOf("=");
			
			if( equalPos != -1 ) {
				String left = line.substring(0, equalPos);
				String[] parts = left.split(" |\t", 2);
				key = parts[1].trim().toUpperCase();
				expressionString = line.substring(equalPos + 1).trim();
			} else {
				String[] parts = line.split(" |\t", 3);
				key = parts[1].toUpperCase();
				expressionString = parts[2].trim();
			}

			SDFExpression expression = new SDFExpression(expressionString, null, MEP.getInstance());
			
			List<SDFAttribute> attributes = expression.getAllAttributes();
			List<Object> values = Lists.newArrayList();

			for( SDFAttribute attribute : attributes ) {
				String name = attribute.getAttributeName().toUpperCase();
				if( !replacements.containsKey(name)) {
					throw new OdysseusScriptException("Replacementkey " + name + " not known in #EVAL-statement");
				} 
				
				Serializable value = replacements.get(name);
				values.add(tryToDouble(value));
			}
			expression.bindVariables(values.toArray());
			
			replacements.put(key, expression.getValue().toString());
			return true;
		}

		return false;
	}

	private static Object tryToDouble(Serializable value) {
		try {
			return Double.valueOf(value.toString());
		} catch( Throwable t ) {
			return value;
		}
	}

	public void put(String key, String value) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Key for replacement not be null or empty");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "Value for replacement must not be null or empty");

		putImpl(key.toUpperCase(), value);
	}

	private void removeImpl(String key) {
		replacements.remove(key);
		context.remove(key);
	}

	private void putImpl(String key, String value) {
		if( replacementProviders.containsKey(key.toUpperCase())) {
			INFO_SERVICE.warning("Replacement '" + key + "' was previously provided by replacementProvider-service and its now overwritten!");
		}
		
		replacements.put(key.toUpperCase(), value);
		context.putOrReplace(key.toUpperCase(), value);
	}

	public static void addDefault(String key, String value) {
		defaultReplacements.put(key, value);
	}

	public Map<String, Serializable> toMap() {
		return Maps.newHashMap(replacements);
	}

	public Context getCurrentContext() {
		return currentContext;
	}

	public Context getNowContext() {
		return context;
	}
}
