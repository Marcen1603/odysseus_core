package de.uniol.inf.is.odysseus.rcp.editor.text.cql;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class CQLFileExecutor implements IFileExecutor {

	@Override
	public String getFileExtension() {
		return "cql_old";
	}

	@Override
	public void run(String text, Context context) {
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
        executor.addQuery(text, "CQL_old", OdysseusRCPPlugIn.getActiveSession(),context);
	}

}
