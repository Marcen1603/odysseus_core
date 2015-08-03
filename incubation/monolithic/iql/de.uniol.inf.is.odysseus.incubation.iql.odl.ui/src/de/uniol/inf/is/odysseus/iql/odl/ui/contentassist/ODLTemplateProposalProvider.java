package de.uniol.inf.is.odysseus.iql.odl.ui.contentassist;


import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;

import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractIQLTemplateProposalProvider;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.scoping.ODLScopeProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLTemplateProposalProvider extends AbstractIQLTemplateProposalProvider<ODLExpressionParser, ODLTypeFactory, ODLLookUp, ODLScopeProvider, ODLTypeUtils> {

	@Inject
	public ODLTemplateProposalProvider(TemplateStore templateStore,
			ContextTypeRegistry registry, ContextTypeIdHelper helper, ODLExpressionParser exprParser, ODLTypeFactory typeFactory, ODLLookUp lookUp, ODLScopeProvider scopeProvider,ODLTypeUtils typeUtils) {
		super(templateStore, registry, helper, exprParser, typeFactory, lookUp, scopeProvider, typeUtils);
	}
	
	protected void createTemplates(String rule, EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		super.createTemplates(rule, node, templateContext, context, acceptor);
		if (node instanceof ODLParameter) {
			createODLParameterProposals(templateContext, context, acceptor);
		}  else if (rule.equals("de.uniol.inf.is.odysseus.iql.odl.ODL.ODLMethod")) {
			createODLMethodProposals(node, templateContext, context, acceptor);
		}		
	}
	
	protected void createODLMethodProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		for (ODLParameter parameter : EcoreUtil2.getAllContentsOfType(node, ODLParameter.class)) {
			createValidationMethodTemplate(parameter, templateContext, context, acceptor);
		}
		
		for (JvmOperation op : lookUp.getOnMethods()) {
			createOnMethodTemplate(op, templateContext, context, acceptor);
		}
	}
	
	protected void createOnMethodTemplate(JvmOperation op,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		String methodName = firstCharLowerCase(op.getSimpleName().substring(2));
		
		descBuilder.append("on "+ methodName);
		descBuilder.append("(");
		if (op.getParameters() != null) {
			descBuilder.append(toDescString(op.getParameters()));
		}
		descBuilder.append(")");
				
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append("on "+ methodName);
		patternBuilder.append("(");
		if (op.getParameters() != null) {
			patternBuilder.append(toOnMethodPattern(op.getParameters()));
		}
		patternBuilder.append(") {"+System.lineSeparator());
		patternBuilder.append("\t${}"+System.lineSeparator());
		patternBuilder.append("}");

		String id = op.getSimpleName();
		if (op.getParameters() != null) {
			id = id+op.getParameters().size();
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
			if (parameter.getParameterType().getSimpleName().length() == 1) {
				b.append("${"+parameter.getParameterType().getSimpleName()+"}");
			} else {
				b.append(toDescString(parameter.getParameterType()));
			}
			b.append(" " +parameters.get(i).getName());
		}
		return b.toString();
	}
	
	protected void createValidationMethodTemplate(ODLParameter parameter, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append("validate " +parameter.getSimpleName());
		
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append("validate " +parameter.getSimpleName() +" {"+System.lineSeparator());
		patternBuilder.append("\t${}"+System.lineSeparator());
		patternBuilder.append("}");
			
		String id = "validate"+parameter.getSimpleName();
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);	
	}
	
	
	protected void createODLParameterProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		for (JvmTypeReference typeRef : factory.getAllParameterValues()) {
			createParameterTemplate(typeRef, templateContext, context, acceptor);
		}		
	}
	
	protected void createParameterTemplate(JvmTypeReference typeRef, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		JvmTypeReference parameterType = factory.getParameterType(typeRef);
		String parameterTypeName = typeUtils.getShortName(parameterType, false);
		
		String simpleName = typeUtils.getShortName(typeRef, false);
		String longName = typeUtils.getLongName(typeRef, false);
			
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(simpleName);
				
		String pattern = patternBuilder.toString();
		String id = longName;
		Template template = createTemplate(simpleName, parameterTypeName, id, pattern);
		finishTemplate(template, templateContext, context, acceptor);	
	}

}
