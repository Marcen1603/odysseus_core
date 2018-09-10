package de.uniol.inf.is.odysseus.rcp.editor.text;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class OdysseusScriptFileExecutor implements IFileExecutor {

	@Override
	public String getFileExtension() {
		return "qry";
	}

	@Override
	public void run(String text, Context context) {
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
        executor.addQuery(text, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), context);
	}
}
