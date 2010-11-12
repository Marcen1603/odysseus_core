package de.uniol.inf.is.odysseus.rcp.editor.text.groups;

import de.uniol.inf.is.odysseus.rcp.editor.text.IKeywordGroup;
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryBuildConfigurationRegistry;

public class TransCfgKeywordGroup implements IKeywordGroup {

	@Override
	public String[] getKeywords() {
		return QueryBuildConfigurationRegistry.getInstance().getQueryBuildConfigurationNames().toArray(new String[0]);
	}

}
