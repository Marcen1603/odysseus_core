package de.uniol.inf.is.odysseus.rcp.editor.text.editor;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;


public class SimpleEditor extends AbstractDecoratedTextEditor {

	private ColorManager colorManager = new ColorManager();
	
	public SimpleEditor() {
		super();
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" }); 
		internal_init();
	}

	protected void internal_init() {
		configureInsertMode(SMART_INSERT, false);
		setDocumentProvider(new SimpleDocumentProvider());
		setSourceViewerConfiguration(new SimpleSourceViewerConfiguration(colorManager));
	}
	
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
}
