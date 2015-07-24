package de.uniol.inf.is.odysseus.iql.basic.ui.contentassist;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;
import org.eclipse.xtext.ui.editor.templates.DefaultTemplateProposalProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.IIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;

public class AbstractIQLTemplateProposalProvider<E extends IIQLExpressionParser, F extends IIQLTypeFactory, L extends IIQLLookUp> extends DefaultTemplateProposalProvider {
	protected E exprParser;
	protected F factory;
	protected L lookUp;
	@Inject
	private IQLQualifiedNameConverter converter;

	public AbstractIQLTemplateProposalProvider(TemplateStore templateStore,	ContextTypeRegistry registry, ContextTypeIdHelper helper, E exprParser, F factory, L lookUp) {
		super(templateStore, registry, helper);
		this.exprParser = exprParser;
		this.factory = factory;
		this.lookUp = lookUp;
	}

	@Override
	protected void createTemplates(TemplateContext templateContext,
			ContentAssistContext context, ITemplateAcceptor acceptor) {
		super.createTemplates(templateContext, context, acceptor);
		createTemplates(templateContext.getContextType().getId(), context.getCurrentModel() ,templateContext, context, acceptor);
	}
	
	protected void createTemplates(String rule, EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		if (node instanceof IQLTerminalExpressionNew) {
			createIQLTerminalExpressionProposals(templateContext, context, acceptor);
		} else if (node instanceof IQLTypeDef && rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLAttribute")) {
			createIQLAttributeProposals(templateContext, context, acceptor);
		} else if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableDeclaration")) {
			createIQLVariableDeclarationProposals(templateContext, context, acceptor);
		} else if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableStatement")) {
			createIQLVariableStatementProposals(templateContext, context, acceptor);
		} else if (node instanceof IQLMemberSelectionExpression && rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMethodSelection")) {
			createIQLMethodSelectionProposals((IQLMemberSelectionExpression) node, templateContext, context, acceptor);
		} else if (node instanceof IQLMemberSelectionExpression && rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLAttributeSelection")) {
			createIQLAttributeSelectionProposals((IQLMemberSelectionExpression) node, templateContext, context, acceptor);
		}
	}
	
	protected void createIQLTerminalExpressionProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		for (JvmType type : lookUp.getAllInstantiateableTypes(context.getCurrentModel().eResource())) {
			Collection<JvmExecutable> constructors =  lookUp.getDeclaredConstructors(factory.getTypeRef(type));
			if (constructors.size() == 0) {
				createConstructorTemplate(type, false, null, templateContext, context, acceptor);
			} else {
				for (JvmExecutable constructor : constructors) {
					createConstructorTemplate(type, false, constructor.getParameters(),templateContext, context, acceptor);
				}
			}
		}
	}
	
	protected void createIQLAttributeSelectionProposals(IQLMemberSelectionExpression expr, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		TypeResult result = exprParser.getType(expr.getLeftOperand());
		
		if (!result.isNull()) {
			for (JvmField attribute : lookUp.getPublicAttributes(result.getRef())) {
				createAttributeTemplate(attribute,templateContext, context, acceptor);
			}
		}
	}
	
	protected void createIQLMethodSelectionProposals(IQLMemberSelectionExpression expr, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		TypeResult result = exprParser.getType(expr.getLeftOperand());
		
		if (!result.isNull()) {
			for (JvmOperation method : lookUp.getPublicMethods(result.getRef())) {
				createMethodTemplate(method,templateContext, context, acceptor);
			}
		}
	}
	
	protected void createIQLVariableStatementProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		for (JvmType type : lookUp.getAllInstantiateableTypes(context.getCurrentModel().eResource())) {
			Collection<JvmExecutable> constructors =  lookUp.getDeclaredConstructors(factory.getTypeRef(type));
			if (constructors.size() == 0) {
				createConstructorTemplate(type, true, null, templateContext, context, acceptor);
			} else {
				for (JvmExecutable constructor : constructors) {
					createConstructorTemplate(type, true, constructor.getParameters(),templateContext, context, acceptor);
				}
			}
		}
	}
	
	protected void createIQLVariableDeclarationProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		for (JvmType type : lookUp.getAllTypes(context.getCurrentModel().eResource())) {
			createTypeTemplate(type, templateContext, context, acceptor);
		}
	}
	
	
	protected void createIQLAttributeProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		for (JvmType type : lookUp.getAllTypes(context.getCurrentModel().eResource())) {
			createTypeTemplate(type, templateContext, context, acceptor);
		}
		for (JvmType type : lookUp.getAllInstantiateableTypes(context.getCurrentModel().eResource())) {
			Collection<JvmExecutable> constructors =  lookUp.getDeclaredConstructors(factory.getTypeRef(type));
			if (constructors.size() == 0) {
				createConstructorTemplate(type, true, null, templateContext, context, acceptor);
			} else {
				for (JvmExecutable constructor : constructors) {
					createConstructorTemplate(type, true, constructor.getParameters(),templateContext, context, acceptor);
				}
			}
		}
	}
	
	protected void createTypeTemplate(JvmType type, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String simpleName = factory.getShortName(type, false);
		String longName = factory.getLongName(type, false);
			
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(simpleName);
				
		String pattern = patternBuilder.toString();
		String id = longName;
		Template template = createTemplate(simpleName, longName, id, pattern);
		finishTemplate(template, templateContext, context, acceptor);	
	}
	
	protected void createMethodTemplate(JvmOperation op,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(op.getSimpleName());

		descBuilder.append("(");
		if (op.getParameters() != null) {
			descBuilder.append(toString(op.getParameters()));
		}
		descBuilder.append(")");
		
		if (op.getReturnType() != null && !factory.isVoid(op.getReturnType())) {
			descBuilder.append(" : " + toString(op.getReturnType()));
		}
		
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(op.getSimpleName());
		patternBuilder.append("(");
		if (op.getParameters() != null) {
			patternBuilder.append(toPattern(op.getParameters()));
		}
		patternBuilder.append(")");
		

		String id = op.getSimpleName();
		if (op.getParameters() != null) {
			id = id+op.getParameters().size();
		}
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);
	}
	
	protected void createAttributeTemplate(JvmField attribute,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(attribute.getSimpleName());
		descBuilder.append(" : " + toString(attribute.getType()));

		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(attribute.getSimpleName());

		String id = attribute.getSimpleName();
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);
	}

	
	protected void createConstructorTemplate(JvmType type, boolean var,  EList<JvmFormalParameter> parameters,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(type.getSimpleName());
		if (var) {
			descBuilder.append(" var ");
		}
		descBuilder.append("(");
		if (parameters != null) {
			descBuilder.append(toString(parameters));
		}
		descBuilder.append(")");
		
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(type.getSimpleName());
		if (var) {
			patternBuilder.append(" ${var}");
		}
		patternBuilder.append("(");
		if (parameters != null) {
			patternBuilder.append(toPattern(parameters));
		}
		patternBuilder.append(")");
				
		String longName = factory.getLongName(type, false);
		String id = longName;
		if (parameters != null) {
			id = id+parameters.size();
		}
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);
	}
	
	
	
	protected String toPattern(EList<JvmFormalParameter> parameters) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i<parameters.size(); i++) {
			if (i > 0) {
				b.append(", ");
			}
			b.append("${"+parameters.get(i).getName()+"}");
		}
		return b.toString();
	}
	
	protected String toString(EList<JvmFormalParameter> parameters) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i<parameters.size(); i++) {
			if (i > 0) {
				b.append(", ");
			}
			b.append(toString(parameters.get(i)));
		}
		return b.toString();
	}
		
	
	protected String firstCharUpperCase(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	protected String firstCharLowerCase(String s) {
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}
	
	protected String toString(JvmTypeReference typeRef) {		
		StringBuilder b = new StringBuilder();
		b.append(converter.toDisplayString(factory.getShortName(typeRef, true)));
		return b.toString();
	}
	
	protected String toString(JvmFormalParameter parameter) {
		StringBuilder b = new StringBuilder();
		b.append(toString(parameter.getParameterType()));
		b.append(" ");
		b.append(parameter.getName());
		return b.toString();
	}
	
	protected Template createTemplate (String name, String desc, String id, String pattern) {
		return new Template(name, desc, id, pattern, false);
	}
	
	
	protected void finishTemplate(Template template, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		TemplateProposal tp = createProposal(template, templateContext,	context, getImage(template), getRelevance(template));
		acceptor.accept(tp);
	}
	
	

}
