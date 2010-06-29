package de.uniol.inf.is.odysseus.rcp.editor.model;


public class EditorModel {

	private static EditorModel instance = null;
	
	private EditorModel() {
		
	}
	
	public static EditorModel getInstance() {
		if( instance == null ) 
			instance = new EditorModel();
		return instance;
	}
	
}
