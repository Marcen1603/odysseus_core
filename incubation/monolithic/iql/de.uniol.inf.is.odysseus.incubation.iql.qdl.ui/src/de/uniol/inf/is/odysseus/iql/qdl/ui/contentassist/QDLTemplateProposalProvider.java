package de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;







import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractIQLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;

public class QDLTemplateProposalProvider extends AbstractIQLTemplateProposalProvider<QDLExpressionParser, QDLTypeFactory, QDLLookUp> {

	@Inject
	public QDLTemplateProposalProvider(TemplateStore templateStore,
			ContextTypeRegistry registry, ContextTypeIdHelper helper, QDLExpressionParser exprParser, QDLTypeFactory factory, QDLLookUp lookUp) {
		super(templateStore, registry, helper, exprParser, factory, lookUp);
	}
	
	@Override
	protected void createIQLVariableStatementProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		super.createIQLVariableStatementProposals(templateContext, context, acceptor);
		for (IOperatorBuilder builder :  factory.getOperators()) {
			createOperatorBuilderTemplate(builder, templateContext, context, acceptor);
		}
	}
	
	protected void createOperatorBuilderTemplate(IOperatorBuilder builder, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		Collection<IParameter<?>> mandatoryParameters = new HashSet<>();
		Collection<IParameter<?>> allParameters = new HashSet<>();
		for (IParameter<?> parameter : builder.getParameters()) {
			allParameters.add(parameter);
			if (parameter.isMandatory()) {
				mandatoryParameters.add(parameter);
			}
		}
		createOperatorTemplate(builder, mandatoryParameters, templateContext, context, acceptor, "operator"+builder.getName()+"1");
		if (mandatoryParameters.size() != allParameters.size()) {
			createOperatorTemplate(builder, allParameters, templateContext, context, acceptor, "operator"+builder.getName()+"2");
		}
	}
		
	protected void createOperatorTemplate(IOperatorBuilder builder,Collection<IParameter<?>> parameters, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor, String id) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(builder.getName());
		descBuilder.append("(){");
		descBuilder.append("{");
		int i = 0;
		for (IParameter<?> parameter : parameters) {
			if (i > 0) {
				descBuilder.append(", ");
			}
			i++;
			descBuilder.append(parameter.getName().toLowerCase());
		}
		descBuilder.append("}");

		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(firstCharUpperCase(builder.getName().toLowerCase()));
		patternBuilder.append(" ${var}()");
		patternBuilder.append("{");
		int j = 0;
		for (IParameter<?> parameter : parameters) {
			if (j > 0) {
				patternBuilder.append(", ");
			}
			j++;
			patternBuilder.append(parameter.getName().toLowerCase()+"=${"+parameter.getName().toLowerCase()+"}");
		}
		patternBuilder.append("}");	
	
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);
	}

}
