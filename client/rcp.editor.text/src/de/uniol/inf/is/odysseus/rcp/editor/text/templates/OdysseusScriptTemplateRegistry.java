package de.uniol.inf.is.odysseus.rcp.editor.text.templates;

import java.util.Map;

import java.util.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public final class OdysseusScriptTemplateRegistry {

	public static final String EMPTY_TEMPLATE_NAME = "<empty>";
	
	private static OdysseusScriptTemplateRegistry instance;
	
	private final Map<String, IOdysseusScriptTemplate> templateMap = Maps.newHashMap();
	
	private OdysseusScriptTemplateRegistry() {
		insertEmptyTemplate();
	}
	
	private void insertEmptyTemplate() {
		register(new EmptyScriptTemplate());
	}

	public static OdysseusScriptTemplateRegistry getInstance() {
		if( instance == null ) {
			instance = new OdysseusScriptTemplateRegistry();
		}
		return instance;
	}
	
	public void register( IOdysseusScriptTemplate template ) {
		Objects.requireNonNull(template, "OdysseusScriptTemplate to register must not be null!");
		// Preconditions.checkArgument( !isRegistered(template), "OdysseusScriptTemplate '%s' already registered", template.getName());
				
		templateMap.put(template.getName(), template);
	}
	
	public void unregister(IOdysseusScriptTemplate template) {
		Objects.requireNonNull(template, "OdysseusScriptTemplate to remove must not be null!");
		// Preconditions.checkArgument(!template.getClass().equals(EmptyScriptTemplate.class), "Unregistering empty template not allowed");

		templateMap.remove(template.getName());
	}
	
	public boolean isRegistered( IOdysseusScriptTemplate template ) {
		return isRegistered(template.getName());
	}
	
	public boolean isRegistered( String templateName ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(templateName), "Name of OdysseusScriptTemplate must not be null or empty");
		
		return templateMap.containsKey(templateName);
	}
	
	public void unregisterAll() {
		templateMap.clear();
		insertEmptyTemplate();
	}
	
	// just for test-classes
	void reset() {
		templateMap.clear();
		insertEmptyTemplate();
	}
	
	public ImmutableCollection<String> getTemplateNames() {
		return ImmutableList.copyOf(templateMap.keySet());
	}
	
	public IOdysseusScriptTemplate getTemplate(String name) {
		Objects.requireNonNull(name, "Name to get OdysseusScriptTemplate must not be null!");
		// Preconditions.checkArgument(!name.isEmpty(), "Name to get OdysseusScriptTemplate must not be empty!");
		// Preconditions.checkArgument(templateMap.containsKey(name), "OdysseusScriptTemplate '%s' already registered", name);

		return templateMap.get(name);
	}
}
