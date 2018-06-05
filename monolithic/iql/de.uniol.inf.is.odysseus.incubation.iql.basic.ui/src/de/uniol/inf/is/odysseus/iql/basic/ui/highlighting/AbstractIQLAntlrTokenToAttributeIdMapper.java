package de.uniol.inf.is.odysseus.iql.basic.ui.highlighting;

import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultAntlrTokenToAttributeIdMapper;

public abstract class AbstractIQLAntlrTokenToAttributeIdMapper extends DefaultAntlrTokenToAttributeIdMapper {

	@Override
	protected String calculateId(String tokenName, int tokenType) {
		return super.calculateId(tokenName, tokenType);
	}

}
