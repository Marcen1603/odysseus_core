package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.rcp.editor.text.IKeywordGroup;

public class PQLKeywords implements IKeywordGroup {

	@Override
	public String[] getKeywords() {
		return OperatorBuilderFactory.getOperatorBuilderNames().toArray(new String[0]);
	}

}
