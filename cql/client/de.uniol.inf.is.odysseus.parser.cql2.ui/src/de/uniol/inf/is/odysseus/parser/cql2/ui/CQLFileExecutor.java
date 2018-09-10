package de.uniol.inf.is.odysseus.parser.cql2.ui;

import de.uniol.inf.is.odysseus.core.collection.Context;
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
		System.out.println("CQLFileExecutor= "  + OdysseusRCPPlugIn.getActiveSession());
		OdysseusRCPEditorTextPlugIn.getExecutor().addQuery(text, "CQL", OdysseusRCPPlugIn.getActiveSession(), context);
	}
}
