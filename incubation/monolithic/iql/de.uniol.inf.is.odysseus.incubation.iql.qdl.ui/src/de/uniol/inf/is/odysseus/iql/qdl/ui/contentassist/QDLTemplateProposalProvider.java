package de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;































import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractIQLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.services.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class QDLTemplateProposalProvider extends AbstractIQLTemplateProposalProvider<QDLExpressionParser, QDLTypeFactory, QDLLookUp, QDLScopeProvider> {

	@Inject
	public QDLTemplateProposalProvider(TemplateStore templateStore,
			ContextTypeRegistry registry, ContextTypeIdHelper helper, QDLExpressionParser exprParser, QDLTypeFactory factory, QDLLookUp lookUp, QDLScopeProvider scopeProvider) {
		super(templateStore, registry, helper, exprParser, factory, lookUp, scopeProvider);
	}
	
	

	@Override
	protected void createIQLMetadataProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		QDLQuery query = EcoreUtil2.getContainerOfType(node, QDLQuery.class);
		if (query != null) {
			for (IPreParserKeywordProvider provider : QDLServiceBinding.getPreParserKeywordProviders()) {
				for (Entry<String, Class<? extends IPreParserKeyword>> entry : provider.getKeywords().entrySet()) {
					createMetadataTemplate(true, entry.getKey(), templateContext, context, acceptor);
					createMetadataTemplate(false, entry.getKey(), templateContext, context, acceptor);
				}
			}
		}
		super.createIQLMetadataProposals(node, templateContext, context, acceptor);
	}
	
	@Override
	protected void createIQLMetadataValueProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		QDLQuery query = EcoreUtil2.getContainerOfType(node, QDLQuery.class);
		if (query != null) {
			IQLMetadata metadata = EcoreUtil2.getContainerOfType(node, IQLMetadata.class);
			if (metadata != null) {
				for (IPreParserKeywordProvider provider : QDLServiceBinding.getPreParserKeywordProviders()) {
					for (Entry<String, Class<? extends IPreParserKeyword>> entry : provider.getKeywords().entrySet()) {
						if (metadata.getName().equalsIgnoreCase(entry.getKey())) {
							createMetadataValueTemplate(entry.getValue(), templateContext, context, acceptor);
						}
					}
				}
			}
		}
		super.createIQLMetadataValueProposals(node, templateContext, context, acceptor);
	}

	
	@Override
	protected void createIQLArgumentsMapKeyValueExprProposals(IQLArgumentsMapKeyValue node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		IQLVariableStatement stmt = EcoreUtil2.getContainerOfType(node, IQLVariableStatement.class);
		IQLTerminalExpressionNew newExpr = EcoreUtil2.getContainerOfType(node, IQLTerminalExpressionNew.class);	
		JvmTypeReference typeRef = null;
		if (stmt != null) {
			typeRef = ((IQLVariableDeclaration) stmt.getVar()).getRef();
		} else {
			typeRef = newExpr.getRef();
		}
		if (factory.isOperator(typeRef)) {
			IOperatorBuilder builder = factory.getOperatorBuilder(factory.getShortName(typeRef, false));
			IParameter<?> parameter = factory.getOperatorParameter(factory.getShortName(typeRef, false), node.getKey());
			if (parameter.getPossibleValueMethod() != null && parameter.getPossibleValueMethod().length()>0) {
				createParameterPossibleValueTemplates(builder, parameter, templateContext, context, acceptor);
			}
		}
	}

	
	@Override
	protected void createIQLVariableStatementProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		super.createIQLVariableStatementProposals(templateContext, context, acceptor);
		for (IQLClass operatorType :  factory.getOperatorTypes()) {
			IOperatorBuilder builder = factory.getOperatorBuilder(operatorType.getSimpleName());
			createOperatorBuilderTemplate(builder, operatorType, templateContext, context, acceptor);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void createParameterPossibleValueTemplates(IOperatorBuilder builder, IParameter<?> parameter,  TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		try {
			Method method = builder.getOperatorClass().getMethod(parameter.getPossibleValueMethod());
			Collection<String> possibleValues = (Collection<String>) method.invoke(builder.getOperatorClass().newInstance());
			for (String possibleValue : possibleValues) {
				String value = "\""+possibleValue+"\"";
				Template template = createTemplate(value, "", possibleValue, value);
				finishTemplate(template, templateContext, context, acceptor);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}
	
	protected void createMetadataTemplate(boolean equals, String name, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String desc = name.toLowerCase();
		if (equals) {
			desc = desc+ " = " + name.toLowerCase();
		}
		String pattern = name.toLowerCase(); 
		if (equals) {
			pattern = pattern +" = ${"+name.toLowerCase()+"}"; 
		}
		String id = name+equals;
		Template template = createTemplate(desc, "", id, pattern);
		finishTemplate(template, templateContext, context, acceptor);
	}
	
	protected void createMetadataValueTemplate(Class<? extends IPreParserKeyword> preParserKeyword, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		try {
			Collection<String> parameters = preParserKeyword.newInstance().getAllowedParameters(OdysseusRCPPlugIn.getActiveSession());
			for (String parameter : parameters) {
				String id = parameter;
				Template template = createTemplate(parameter, "", id, parameter);
				finishTemplate(template, templateContext, context, acceptor);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}		
	}
	
	protected void createOperatorBuilderTemplate(IOperatorBuilder builder, IQLClass operatorType, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		Set<String> mandatoryParameters = new HashSet<>();
		Set<String> allParameters = new HashSet<>();
		for (IParameter<?> parameter : builder.getParameters()) {
			allParameters.add(parameter.getName().toLowerCase());
			if (parameter.isMandatory()) {
				mandatoryParameters.add(parameter.getName().toLowerCase());
			}
		}
		createOperatorTemplate(builder, operatorType,  mandatoryParameters, templateContext, context, acceptor, "operator"+builder.getName()+"1");
		if (mandatoryParameters.size() != allParameters.size()) {
			createOperatorTemplate(builder, operatorType, allParameters, templateContext, context, acceptor, "operator"+builder.getName()+"2");
		}
	}
		
	protected void createOperatorTemplate(IOperatorBuilder builder, IQLClass operatorType ,Set<String> parameters, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor, String id) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(firstCharUpperCase(builder.getName().toLowerCase()));
		descBuilder.append("(){");
		descBuilder.append("{");
		int i = 0;
		for (String parameter : parameters) {
			if (i > 0) {
				descBuilder.append(", ");
			}
			i++;
			descBuilder.append(parameter.toLowerCase());
		}
		descBuilder.append("}");

		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(firstCharUpperCase(builder.getName().toLowerCase()));
		patternBuilder.append(" ${var}()");
		patternBuilder.append("{");
		int j = 0;
		for (IQLAttribute parameter : EcoreUtil2.getAllContentsOfType(operatorType, IQLAttribute.class)) {
			if (parameters.contains(parameter.getSimpleName().toLowerCase())) {
				if (j > 0) {
					patternBuilder.append(", ");
				}
				j++;
				patternBuilder.append(parameter.getSimpleName().toLowerCase()+"="+"${"+parameter.getSimpleName().toLowerCase()+"}");
			}
		}
		patternBuilder.append("}");	
	
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);
	}

}
