package de.uniol.inf.is.odysseus.rcp.editor.text.cql;

import org.eclipse.ui.IEditorPart;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class CQLFileExecutor implements IFileExecutor {

	@Override
	public String getFileExtension() {
		return "cql";
	}

	@Override
	public void run(String text, Context context) {
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
        executor.addQuery(text, "CQL", OdysseusRCPPlugIn.getActiveSession(),context);
	}

	@Override
	public void run(String text, Context context, IEditorPart editor) {
		run(text, context);
	}

}
