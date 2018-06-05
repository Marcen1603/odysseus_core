package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.incubation.iql.odl.ui.internal.OdlActivator;



public class ODLTemplateRegistry {
	public static final String EMPTY_TEMPLATE_NAME = "<empty>";
	
	private static ODLTemplateRegistry instance;
	
	private final Map<String, IODLTemplate> templateMap = Maps.newHashMap();
	
	private ODLTemplateRegistry() {
		insertEmptyTemplate();
		insertDefaultTemplate();

	}
	
	private void insertEmptyTemplate() {
		register(new ODLEmptyScriptTemplate());

	}
	
	private void insertDefaultTemplate() {
		for (IODLTemplate template : ODLTemplateReader.readTemplates(OdlActivator.getInstance().getBundle())) {
			register(template);
		}
	}

	public static ODLTemplateRegistry getInstance() {
		if( instance == null ) {
			instance = new ODLTemplateRegistry();
		}
		return instance;
	}
	
	public void register( IODLTemplate template ) {
		Preconditions.checkNotNull(template, "ODLTemplate  to register must not be null!");
		Preconditions.checkArgument( !isRegistered(template), "ODLTemplate '%s' already registered", template.getName());
				
		templateMap.put(template.getName(), template);
	}
	
	public void unregister(IODLTemplate template) {
		Preconditions.checkNotNull(template, "ODLTemplate to remove must not be null!");
		Preconditions.checkArgument(!template.getClass().equals(ODLEmptyScriptTemplate.class), "Unregistering empty template not allowed");

		templateMap.remove(template.getName());
	}
	
	public boolean isRegistered( IODLTemplate template ) {
		return isRegistered(template.getName());
	}
	
	public boolean isRegistered( String templateName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(templateName), "Name of ODLTemplate must not be null or empty");
		
		return templateMap.containsKey(templateName);
	}
	
	public void unregisterAll() {
		templateMap.clear();
		insertEmptyTemplate();
		insertDefaultTemplate();
	}
	
	// just for test-classes
	void reset() {
		templateMap.clear();
		insertEmptyTemplate();
		insertDefaultTemplate();
	}
	
	public ImmutableCollection<String> getTemplateNames() {
		return ImmutableList.copyOf(templateMap.keySet());
	}
	
	public IODLTemplate getTemplate(String name) {
		Preconditions.checkNotNull(name, "Name to get ODLTemplate must not be null!");
		Preconditions.checkArgument(!name.isEmpty(), "Name to get ODLTemplate must not be empty!");
		Preconditions.checkArgument(templateMap.containsKey(name), "ODLTemplate '%s' already registered", name);

		return templateMap.get(name);
	}
}

