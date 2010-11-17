package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import java.util.HashMap;
import java.util.Map;

public class ParameterEditorRegistry {

	public static final String NAME_SEPARATOR = "__";
	
	private static ParameterEditorRegistry instance;
	
	private final Map<String, Class<? extends IParameterEditor>> editors = new HashMap<String, Class<? extends IParameterEditor>>();
	
	private ParameterEditorRegistry() {
		
	}
	
	public static ParameterEditorRegistry getInstance() {
		if( instance == null ) 
			instance = new ParameterEditorRegistry();
		return instance;
	}
	
	public void register( String parameterName, Class<? extends IParameterEditor> editor ) {
		if( editors.containsKey(parameterName))
			throw new IllegalArgumentException("ParameterEditor with name '" + parameterName + "' already exists");
		
		// editor needs standard-constructor
		try {
			editor.getConstructor();
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Class " + editor.getName() + " has no standard-constructor", e );
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Class " + editor.getName() + " has no standard-constructor", e );
		}
		
		editors.put(parameterName, editor);
	}
	
	public IParameterEditor create( String parameterName ) {
		System.out.println("Create ParameterEditor for " + parameterName);
		Class<? extends IParameterEditor> clazz = editors.get(parameterName);
		if( clazz == null ) 
			throw new IllegalArgumentException("ParameterEditor with name '" + parameterName + "' not found");
		
		try {
			IParameterEditor newInstance = clazz.newInstance();
			return newInstance;
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Cannot create new instance of " + clazz.getName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot create new instance of " + clazz.getName(), e);
		}
	}
	
	public boolean exists( String parameterName ) {
		return editors.containsKey(parameterName);
	}
	
	public void unregister( String parameterName ) {
		editors.remove(parameterName);
	}
}
