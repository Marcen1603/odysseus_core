package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class OdysseusScriptEditor extends AbstractDecoratedTextEditor {

	private ColorManager colorManager = new ColorManager();
	private OdysseusScriptContentOutlinePage outlinePage;

	public OdysseusScriptEditor() {
		super();
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" });
		internal_init();
	}

	protected void internal_init() {
		configureInsertMode(SMART_INSERT, false);
		setDocumentProvider(new OdysseusScriptDocumentProvider());
		setSourceViewerConfiguration(new OdysseusScriptViewerConfiguration(colorManager));
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new OdysseusScriptContentOutlinePage(getDocumentProvider().getDocument(getEditorInput()).get());
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}
}
