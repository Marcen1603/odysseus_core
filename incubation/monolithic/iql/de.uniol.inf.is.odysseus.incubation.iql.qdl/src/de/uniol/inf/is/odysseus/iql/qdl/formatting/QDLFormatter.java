package de.uniol.inf.is.odysseus.iql.qdl.formatting;

import javax.inject.Inject;

import org.eclipse.xtext.formatting.impl.FormattingConfig;

import de.uniol.inf.is.odysseus.iql.basic.formatting.AbstractIQLFormatter;
import de.uniol.inf.is.odysseus.iql.qdl.services.QDLGrammarAccess;

public class QDLFormatter extends AbstractIQLFormatter {
	
	@Inject
	QDLGrammarAccess qdl;
	
	@Override
	protected void configureFormatting(FormattingConfig c) {
		super.configureFormatting(c);
		
		c.setLinewrap(2, 2, 10).after(qdl.getQDLModelAccess().getNamespacesIQLNamespaceParserRuleCall_0_0());

	}
}
