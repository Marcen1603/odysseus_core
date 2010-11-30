package de.uniol.inf.is.odysseus.rcp.editor.text.groups;

import de.uniol.inf.is.odysseus.rcp.editor.text.IKeywordGroup;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;

public class TransCfgKeywordGroup implements IKeywordGroup {

	@Override
	public String[] getKeywords() {
		if (ExecutorHandler.getExecutor() != null){
			return  ExecutorHandler.getExecutor().getQueryBuildConfigurationNames().toArray(new String[0]);
		}else{
			return null;
		}
	}

}
