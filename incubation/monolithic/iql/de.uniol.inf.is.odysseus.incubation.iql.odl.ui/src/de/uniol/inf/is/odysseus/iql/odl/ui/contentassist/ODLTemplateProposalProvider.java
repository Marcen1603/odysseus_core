package de.uniol.inf.is.odysseus.iql.odl.ui.contentassist;




import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractIQLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.scoping.IODLScopeProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.IEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;


public class ODLTemplateProposalProvider extends AbstractIQLTemplateProposalProvider<IODLExpressionEvaluator, IODLTypeDictionary, IODLLookUp, IODLScopeProvider, IODLTypeUtils> {

	@Inject
	public ODLTemplateProposalProvider(TemplateStore templateStore,
			ContextTypeRegistry registry, ContextTypeIdHelper helper, IODLExpressionEvaluator exprEvaluator, IODLTypeDictionary typeDictionary, IODLLookUp lookUp, IODLScopeProvider scopeProvider,IODLTypeUtils typeUtils) {
		super(templateStore, registry, helper, exprEvaluator, typeDictionary, lookUp, scopeProvider, typeUtils);
	}
	
	protected void createTemplates(String rule, EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		super.createTemplates(rule, node, templateContext, context, acceptor);
		if (node instanceof ODLParameter && rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.JvmTypeReference")) {
			createODLParameterProposals(node, templateContext, context, acceptor);
		}  else if (node instanceof ODLMethod) {
			createODLMethodProposals((ODLMethod) node, templateContext, context, acceptor);
		}		
	}
	
	@Override
	protected void createIQLMetadataProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		ODLParameter parameter = EcoreUtil2.getContainerOfType(node, ODLParameter.class);
		if (parameter != null) {
			for (String str : lookUp.getParameterMetadataKeys()) {
				createMetadataTemplate(str, templateContext, context, acceptor);
			}
		}
		
		ODLOperator op = EcoreUtil2.getContainerOfType(node, ODLOperator.class);
		if (op != null) {
			for (String str : lookUp.getOperatorMetadataKeys()) {
				createMetadataTemplate(str, templateContext, context, acceptor);
			}
		}		
		super.createIQLMetadataProposals(node, templateContext, context, acceptor);
	}
	
	@Override
	protected void createIQLMetadataValueProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		ODLParameter parameter = EcoreUtil2.getContainerOfType(node, ODLParameter.class);
		if (parameter != null) {
			IQLMetadata metadata = EcoreUtil2.getContainerOfType(node, IQLMetadata.class);
			if (metadata != null) {
				for (String value : lookUp.getParameterMetadataValues(metadata.getName())) {
					createMetadataValueTemplate(value, templateContext, context, acceptor);
				}				
				if (metadata.getName().equals(IODLTypeDictionary.PARAMETER_TYPE)) {
					for (JvmTypeReference typeRef : typeDictionary.getAllParameterTypes(node)) {
						createTypeTemplate(typeUtils.getInnerType(typeRef, false), templateContext, context, acceptor);
					}
				}
			}
		}
		
		ODLOperator operator = EcoreUtil2.getContainerOfType(node, ODLOperator.class);
		if (operator != null) {
			IQLMetadata metadata = EcoreUtil2.getContainerOfType(node, IQLMetadata.class);
			if (metadata != null) {
				for (String value : lookUp.getOperatorMetadataValues(metadata.getName())) {
					createMetadataValueTemplate(value, templateContext, context, acceptor);
				}
			}
		}
		super.createIQLMetadataValueProposals(node, templateContext, context, acceptor);
	}
	
	protected void createMetadataValueTemplate(String name, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String id = name;
		Template template = createTemplate(name, "", id, name);
		finishTemplate(template, templateContext, context, acceptor);
	
	}
	
	protected void createODLMethodProposals(ODLMethod node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		if (node.isOn()) {
			for (IEventMethod eventMethod : EventMethodsFactory.getInstance().getAllEventMethods()) {
				createOnMethodTemplate(eventMethod, templateContext, context, acceptor);
			}
		} else if (node.isValidate()) {
			for (ODLParameter parameter : EcoreUtil2.getAllContentsOfType(EcoreUtil2.getContainerOfType(node, ODLOperator.class), ODLParameter.class)) {
				createValidationMethodTemplate(parameter, templateContext, context, acceptor);
			}
		}	
	}
	
	protected void createOnMethodTemplate(IEventMethod eventMethod,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		
		descBuilder.append(eventMethod.getEventName());
		descBuilder.append("(");
		if (eventMethod.getEventMethodParameters() != null) {
			int i = 0;
			for (EventMethodParameter parameter : eventMethod.getEventMethodParameters()) {
				if (i > 0) {
					descBuilder.append(", ");
				}
				i++;
				descBuilder.append(parameter.getType() +" "+parameter.getName());
			}
		}
		descBuilder.append(")");
		if (eventMethod.getReturnType() != null) {
			descBuilder.append(" : " + eventMethod.getReturnType());
		}	
		
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(eventMethod.getEventName());
		patternBuilder.append("(");
		if (eventMethod.getEventMethodParameters() != null) {
			int i = 0;
			for (EventMethodParameter parameter : eventMethod.getEventMethodParameters()) {
				if (i > 0) {
					patternBuilder.append(", ");
				}
				i++;
				patternBuilder.append(parameter.getType() +" "+parameter.getName());
			}
		}
		patternBuilder.append(") ");
		if (eventMethod.getReturnType() != null) {
			patternBuilder.append(": " + eventMethod.getReturnType());
		}		
	
		patternBuilder.append("{"+System.lineSeparator());

		patternBuilder.append("\t${}"+System.lineSeparator());
		patternBuilder.append("}");

		String id = eventMethod.getEventName();
		if (eventMethod.getEventMethodParameters() != null) {
			id = id+eventMethod.getEventMethodParameters().size();
		}
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);
	}
	
	protected String toOnMethodPattern(EList<JvmFormalParameter> parameters) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i<parameters.size(); i++) {
			if (i > 0) {
				b.append(", ");
			}
			JvmFormalParameter parameter =  parameters.get(i);
			b.append(toDescString(parameter.getParameterType()));
			b.append(" " +parameters.get(i).getName());
		}
		return b.toString();
	}
	
	protected void createValidationMethodTemplate(ODLParameter parameter, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(parameter.getSimpleName());
		
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(parameter.getSimpleName() +" {"+System.lineSeparator());
		patternBuilder.append("\t${}"+System.lineSeparator());
		patternBuilder.append("}");
			
		String id = "validate"+parameter.getSimpleName();
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);	
	}
	
	
	protected void createODLParameterProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		for (JvmTypeReference typeRef : typeDictionary.getAllParameterValues(node)) {
			createParameterTemplate(node, typeRef, templateContext, context, acceptor);
		}		
	}
	
	protected void createParameterTemplate(EObject node, JvmTypeReference typeRef, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		JvmTypeReference parameterType = typeDictionary.getParameterType(typeRef, node);
		String parameterTypeName = typeUtils.getShortName(parameterType, false);
		
		String simpleName = typeUtils.getShortName(typeRef, false);
		String longName = typeUtils.getLongName(typeRef, false);
			
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(simpleName);
				
		String pattern = patternBuilder.toString();
		String id = longName;
		Template template = createTemplate(simpleName, parameterTypeName, id, pattern);
		JvmType innerType = typeUtils.getInnerType(typeRef, false);
		Image image = labelProvider.getImage(innerType);
		finishTemplate(template, templateContext, context, image, acceptor);	
	}
	
	protected void createMetadataTemplate(String name, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String desc = name;
		desc = desc+ " = " + name;
		String pattern = name; 
		pattern = pattern +" = ${"+name+"}"; 
		String id = name;
		Template template = createTemplate(desc, "", id, pattern);
		finishTemplate(template, templateContext, context, acceptor);
	}

}
