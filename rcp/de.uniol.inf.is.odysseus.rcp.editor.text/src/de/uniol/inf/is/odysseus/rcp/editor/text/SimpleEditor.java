package de.uniol.inf.is.odysseus.rcp.editor.text;

import org.eclipse.ui.texteditor.AbstractTextEditor;


public class SimpleEditor extends AbstractTextEditor {

	public SimpleEditor() {
		super();
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" }); 
		internal_init();
	}

	protected void internal_init() {
		configureInsertMode(SMART_INSERT, false);
		setDocumentProvider(new SimpleDocumentProvider());
	}
	
}
