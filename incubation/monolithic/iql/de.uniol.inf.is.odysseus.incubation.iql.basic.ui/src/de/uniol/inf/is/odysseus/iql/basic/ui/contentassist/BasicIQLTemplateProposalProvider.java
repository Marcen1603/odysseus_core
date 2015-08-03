package de.uniol.inf.is.odysseus.iql.basic.ui.contentassist;

import javax.inject.Inject;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.BasicIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLTemplateProposalProvider extends AbstractIQLTemplateProposalProvider<BasicIQLExpressionParser, BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLScopeProvider, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLTemplateProposalProvider(TemplateStore templateStore,
			ContextTypeRegistry registry, ContextTypeIdHelper helper, BasicIQLExpressionParser exprParser, BasicIQLTypeFactory factory, BasicIQLLookUp lookUp, BasicIQLScopeProvider scopeProvider, BasicIQLTypeUtils typeUtils) {
		super(templateStore, registry, helper,exprParser, factory, lookUp, scopeProvider, typeUtils);
	}

}
