package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import de.uniol.inf.is.odysseus.rcp.editor.text.IKeywordGroup;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;

public class TransCfgKeywordGroup implements IKeywordGroup {

	@Override
	public String[] getKeywords() {
		return ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfigurationNames().toArray(new String[0]);
	}

}
