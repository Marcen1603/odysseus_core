package de.uniol.inf.is.odysseus.rcp.editor.text.groups;

import de.uniol.inf.is.odysseus.rcp.editor.text.IKeywordGroup;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class TransCfgKeywordGroup implements IKeywordGroup {

	@Override
	public String[] getKeywords() {
		if (OdysseusRCPEditorTextPlugIn.getExecutor() != null){
			return  OdysseusRCPEditorTextPlugIn.getExecutor().getQueryBuildConfigurationNames().toArray(new String[0]);
		}else{
			return null;
		}
	}

}
