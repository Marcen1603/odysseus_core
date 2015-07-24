package de.uniol.inf.is.odysseus.iql.basic.ui.contentassist;

import javax.inject.Inject;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;

public class BasicIQLTemplateProposalProvider extends AbstractIQLTemplateProposalProvider<BasicIQLExpressionParser, BasicIQLTypeFactory, BasicIQLLookUp> {

	@Inject
	public BasicIQLTemplateProposalProvider(TemplateStore templateStore,
			ContextTypeRegistry registry, ContextTypeIdHelper helper, BasicIQLExpressionParser exprParser, BasicIQLTypeFactory factory, BasicIQLLookUp lookUp) {
		super(templateStore, registry, helper,exprParser, factory, lookUp);
	}

}
