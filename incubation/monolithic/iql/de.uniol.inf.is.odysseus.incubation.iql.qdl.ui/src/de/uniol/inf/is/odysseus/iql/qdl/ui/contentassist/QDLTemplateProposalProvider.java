package de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;







































import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.executor.AbstractIQLExecutor;
import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractIQLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.IQDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;


public class QDLTemplateProposalProvider extends AbstractIQLTemplateProposalProvider<IQDLExpressionEvaluator, IQDLTypeDictionary, IQDLLookUp, IQDLScopeProvider, IQDLTypeUtils> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractIQLExecutor.class);

	@Inject
	public QDLTemplateProposalProvider(TemplateStore templateStore,
			ContextTypeRegistry registry, ContextTypeIdHelper helper, IQDLExpressionEvaluator exprEvaluator, IQDLTypeDictionary typeDictionary, IQDLLookUp lookUp, IQDLScopeProvider scopeProvider, IQDLTypeUtils typeUtils) {
		super(templateStore, registry, helper, exprEvaluator, typeDictionary, lookUp, scopeProvider, typeUtils);
	}	

	@Override
	protected void createIQLMetadataProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		QDLQuery query = EcoreUtil2.getContainerOfType(node, QDLQuery.class);
		if (query != null) {
			for (String metadataKey : lookUp.getMetadataKeys()) {
				createMetadataTemplate(metadataKey, templateContext, context, acceptor);
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
				Collection<String> values = lookUp.getMetadataValues(metadata.getName());
				for (String value : values) {
					createMetadataValueTemplate(value, templateContext, context, acceptor);
				}
				
			}
		}
		super.createIQLMetadataValueProposals(node, templateContext, context, acceptor);
	}

	
	@Override
	protected void createIQLArgumentsMapKeyValueExprProposals(IQLArgumentsMapKeyValue node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		IQLVariableStatement stmt = EcoreUtil2.getContainerOfType(node, IQLVariableStatement.class);
		IQLNewExpression newExpr = EcoreUtil2.getContainerOfType(node, IQLNewExpression.class);	
		JvmTypeReference typeRef = null;
		if (stmt != null) {
			typeRef = ((IQLVariableDeclaration) stmt.getVar()).getRef();
		} else {
			typeRef = newExpr.getRef();
		}
		if (lookUp.isOperator(typeRef)) {
			IOperatorBuilder builder = typeDictionary.getOperatorBuilder(typeUtils.getShortName(typeRef, false));
			IParameter<?> parameter = typeDictionary.getOperatorParameter(typeUtils.getShortName(typeRef, false), node.getKey().getSimpleName());
			if (parameter.getPossibleValueMethod() != null && parameter.getPossibleValueMethod().length()>0) {
				createParameterPossibleValueTemplates(builder, parameter, templateContext, context, acceptor);
			}
		}
	}

	
	@Override
	protected void createIQLVariableStatementProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		super.createIQLVariableStatementProposals(templateContext, context, acceptor);
		for (IQLClass operatorType :  typeDictionary.getOperatorTypes()) {
			IOperatorBuilder builder = typeDictionary.getOperatorBuilder(operatorType.getSimpleName());
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
			LOG.error("error while creating possible values of parameters", e);
		}
	}
	
	protected void createMetadataTemplate(String name, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String desc = name.toLowerCase()+ " = " + name.toLowerCase();
	
		String pattern = name.toLowerCase() +" = ${"+name.toLowerCase()+"}"; 
		String id = name;
		Template template = createTemplate(desc, "", id, pattern);
		finishTemplate(template, templateContext, context, acceptor);
	}
	
	protected void createMetadataValueTemplate(String value, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String id =  value;
		Template template = createTemplate(value, "", id,  value);
		finishTemplate(template, templateContext, context, acceptor);		
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
		descBuilder.append(operatorType.getSimpleName());
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
		patternBuilder.append(operatorType.getSimpleName());
		patternBuilder.append(" ${var}()");
		patternBuilder.append("{");
		int j = 0;
		for (String parameter : parameters) {
			if (j > 0) {
				patternBuilder.append(", ");
			}
			j++;
			patternBuilder.append(parameter+"="+"${"+parameter+"}");
		}
		patternBuilder.append("}");	
	
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		Image image = imageHelper.getImage("java_file.gif");
		finishTemplate(template, templateContext, context,image, acceptor);
	}

}
