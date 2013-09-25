package de.uniol.inf.is.odysseus.rcp.editor.text.templates;

import java.util.Map;

import org.testng.util.Strings;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public final class OdysseusScriptTemplateRegistry {

	private static OdysseusScriptTemplateRegistry instance;
	
	private final Map<String, IOdysseusScriptTemplate> templateMap = Maps.newHashMap();
	
	public static OdysseusScriptTemplateRegistry getInstance() {
		if( instance == null ) {
			instance = new OdysseusScriptTemplateRegistry();
		}
		return instance;
	}
	
	public void register( IOdysseusScriptTemplate template ) {
		Preconditions.checkNotNull(template, "OdysseusScriptTemplate to register must not be null!");
		Preconditions.checkArgument( !isRegistered(template), "OdysseusScriptTemplate '%s' already registered", template.getName());
				
		templateMap.put(template.getName(), template);
	}
	
	public void unregister(IOdysseusScriptTemplate template) {
		Preconditions.checkNotNull(template, "OdysseusScriptTemplate to remove must not be null!");
		Preconditions.checkArgument(isRegistered(template), "OdysseusScriptTemplate '%s' already registered", template.getName());

		templateMap.remove(template.getName());
	}
	
	public boolean isRegistered( IOdysseusScriptTemplate template ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(template.getName()), "Name of OdysseusScriptTemplate must not be null or empty");

		return templateMap.containsKey(template.getName());
	}
	
	public void unregisterAll() {
		templateMap.clear();
	}
	
	// just for test-classes
	void reset() {
		templateMap.clear();
	}
	
	public ImmutableCollection<String> getTemplateNames() {
		return ImmutableList.copyOf(templateMap.keySet());
	}
	
	public IOdysseusScriptTemplate getTemplate(String name) {
		Preconditions.checkNotNull(name, "Name to get OdysseusScriptTemplate must not be null!");
		Preconditions.checkArgument(!name.isEmpty(), "Name to get OdysseusScriptTemplate must not be empty!");
		Preconditions.checkArgument(templateMap.containsKey(name), "OdysseusScriptTemplate '%s' already registered", name);

		return templateMap.get(name);
	}
}
