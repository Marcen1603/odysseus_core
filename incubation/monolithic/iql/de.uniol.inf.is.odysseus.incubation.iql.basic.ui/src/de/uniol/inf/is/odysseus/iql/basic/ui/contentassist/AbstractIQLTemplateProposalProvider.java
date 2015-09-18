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
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.IImageHelper;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;
import org.eclipse.xtext.ui.editor.templates.DefaultTemplateProposalProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.linking.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public class AbstractIQLTemplateProposalProvider<E extends IIQLExpressionEvaluator, F extends IIQLTypeDictionary, L extends IIQLLookUp, S extends IIQLScopeProvider, U extends IIQLTypeUtils> extends DefaultTemplateProposalProvider {
	protected E exprEvaluator;
	protected F typeDictionary;
	protected L lookUp;
	protected S scopeProvider;
	protected U typeUtils;
	
	@Inject
	protected IQLQualifiedNameConverter converter;
	
	@Inject
	protected IIQLMethodFinder methodFinder;
	
	@Inject
	protected IImageHelper imageHelper;
		
	@Inject
	protected ILabelProvider labelProvider;
	
	public AbstractIQLTemplateProposalProvider(TemplateStore templateStore,	ContextTypeRegistry registry, ContextTypeIdHelper helper, E exprEvaluator, F typeDictionary, L lookUp, S scopeProvider, U typeUtils) {
		super(templateStore, registry, helper);
		this.exprEvaluator = exprEvaluator;
		this.typeDictionary = typeDictionary;
		this.lookUp = lookUp;
		this.scopeProvider = scopeProvider;
		this.typeUtils = typeUtils;
	}

	@Override
	protected void createTemplates(TemplateContext templateContext,
			ContentAssistContext context, ITemplateAcceptor acceptor) {
		super.createTemplates(templateContext, context, acceptor);
		createTemplates(templateContext.getContextType().getId(), context.getCurrentModel() ,templateContext, context, acceptor);
	}
	
	protected void createTemplates(String rule, EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLVariableStatement")) {
			createIQLVariableStatementProposals(templateContext, context, acceptor);
		} else if (node instanceof IQLMemberSelectionExpression) {
			createIQLMemberSelectionProposals((IQLMemberSelectionExpression) node, templateContext, context, acceptor);
		} else if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLOtherExpressions")) {
			createIQLJvmElementCallExpressionProposals(node, templateContext, context, acceptor);
		} else if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLArgumentsMapKeyValue")) {
			createIQLArgumentsMapKeyValueProposals(node, templateContext, context, acceptor);
		} else if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadata")) {
			createIQLMetadataProposals(node, templateContext, context, acceptor);
		} else if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLMetadataValue")) {
			createIQLMetadataValueProposals(node, templateContext, context, acceptor);
		} else if (node instanceof IQLArgumentsMapKeyValue && rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLExpression")) {
			createIQLArgumentsMapKeyValueExprProposals((IQLArgumentsMapKeyValue) node, templateContext, context, acceptor);
		} else if (node instanceof IQLNamespace) {
			createIQLNamespaceProposals((IQLNamespace) node, templateContext, context, acceptor);
		} else if (rule.equals("de.uniol.inf.is.odysseus.iql.basic.BasicIQL.IQLConstructorCallStatement")) {
			createIQLConstructorCallStatementProposals(node, templateContext, context, acceptor);
		}     	
	}
	
	protected void createIQLConstructorCallStatementProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		JvmGenericType type = EcoreUtil2.getContainerOfType(node, JvmGenericType.class);
		JvmTypeReference typeRef = typeUtils.createTypeRef(type);

		if (type.getExtendedClass() != null) {
			for (JvmExecutable constructor : lookUp.getSuperConstructors(typeRef)) {
				createConstructorTemplate("super",constructor.getParameters(), templateContext, context, acceptor);
			}
		}		
		for (JvmExecutable constructor : lookUp.getDeclaredConstructors(typeRef)) {
			createConstructorTemplate("this",constructor.getParameters(), templateContext, context, acceptor);
		}
		
	}
	
	protected void createIQLNamespaceProposals(IQLNamespace node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		if (node.isStatic()) {
			for (IEObjectDescription type : scopeProvider.getTypes(node)) {
				EObject obj = type.getEObjectOrProxy();
				if (obj instanceof JvmGenericType) {
					createTypeTemplate((JvmGenericType)obj, templateContext, context, acceptor);
				}
			}
		} else {
			for (String namespace : lookUp.getAllNamespaces()) {
				createNamespaceTemplate(namespace, templateContext, context, acceptor);
			}
			for (IEObjectDescription type : scopeProvider.getTypes(node)) {
				EObject obj = type.getEObjectOrProxy();
				if (obj instanceof JvmGenericType) {
					createTypeTemplate((JvmGenericType)obj, templateContext, context, acceptor);
				}
			}
		}
	}

	protected void createIQLVariableStatementProposals(TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {

	}
	
	protected void createIQLArgumentsMapKeyValueExprProposals(IQLArgumentsMapKeyValue node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		
	}
	
	protected void createIQLMetadataProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		
	}
	
	protected void createIQLMetadataValueProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		
	}
	
	protected void createIQLArgumentsMapKeyValueProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		Collection<IEObjectDescription> elements = scopeProvider.getScopeIQLArgumentsMapKey(node);
		for (IEObjectDescription element : elements) {
			createMapEntryTemplate(element, templateContext, context, acceptor);
		}
	}
	
	
	protected void createIQLJvmElementCallExpressionProposals(EObject node, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		Collection<IEObjectDescription> elements = scopeProvider.getScopeIQLJvmElementCallExpression(node);
		for (IEObjectDescription desc : elements) {
			EObject el = desc.getEObjectOrProxy();
			if (el instanceof IQLVariableDeclaration) {
				IQLVariableDeclaration decl = (IQLVariableDeclaration) el;
				JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(decl, JvmDeclaredType.class);
				createVariableTemplate(decl.getName(), declaredType, decl.getRef(), templateContext, context, acceptor);
			} else if (el instanceof JvmFormalParameter) {
				JvmFormalParameter parameter = (JvmFormalParameter) el;
				JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(parameter, JvmDeclaredType.class);
				createVariableTemplate(parameter.getName(),declaredType, parameter.getParameterType(), templateContext, context, acceptor);
			} else if (el instanceof JvmField) {
				JvmField attr = (JvmField) el;
				JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(attr, JvmDeclaredType.class);
				createVariableTemplate(converter.toString(desc.getQualifiedName()),declaredType, attr.getType(), templateContext, context, acceptor);
			} else if (el instanceof JvmOperation) {
				JvmOperation op = (JvmOperation) el;
				if (op.getSimpleName() != null) {
					createMethodTemplate(converter.toString(desc.getQualifiedName()), op, templateContext, context, acceptor);
				}
			}
		}
	}
	
	
	protected void createIQLMemberSelectionProposals(IQLMemberSelectionExpression expr, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		Collection<IEObjectDescription> attributes = scopeProvider.getScopeIQLMemberSelection(expr);
		for (IEObjectDescription desc : attributes) {
			EObject obj = desc.getEObjectOrProxy();
			if (obj instanceof JvmField) {
				JvmField attribute = (JvmField) obj;
				JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(attribute, JvmDeclaredType.class);
				createVariableTemplate(attribute.getSimpleName(), declaredType, attribute.getType(),templateContext, context, acceptor);
			} else {
				JvmOperation method = (JvmOperation) obj;
				String name = converter.toString(desc.getQualifiedName());
				createMethodTemplate(name, method,templateContext, context, acceptor);

			}
		}		
	}
		
	protected void createNamespaceTemplate(String name, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(name);

		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(name);
				
		String id = name;
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		Image image = imageHelper.getImage("package_obj.gif");
		finishTemplate(template, templateContext, context, image, acceptor);	
	}
	
	protected void createMapEntryTemplate(IEObjectDescription element, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String name = element.toString();
		JvmTypeReference typeRef = null;
		EObject obj = element.getEObjectOrProxy();
		if (obj instanceof JvmField) {
			typeRef = ((JvmField) obj).getType();
		} else {
			JvmOperation op = (JvmOperation) obj;
			typeRef = op.getParameters().get(0).getParameterType();
		}
		
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(name);
		descBuilder.append(" = ");
		descBuilder.append(toDescString(typeRef));

		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(name);
		patternBuilder.append(" = ");
		patternBuilder.append("${"+name+"}");
				
		String id = name+typeUtils.getLongName(typeRef, true);
		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context, acceptor);	
	}

	
	protected void createTypeTemplate(JvmType type, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		String simpleName = typeUtils.getShortName(type, false);
		String longName =  typeUtils.getLongName(type, false);
			
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(simpleName);
				
		String pattern = patternBuilder.toString();
		String id = longName;
		Template template = createTemplate(simpleName, longName, id, pattern);
		Image image = labelProvider.getImage(type);
		finishTemplate(template, templateContext, context,image, acceptor);	
	}
	
	protected void createMethodTemplate(String name, JvmOperation op,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(op, JvmDeclaredType.class);
		
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(name);
		

		descBuilder.append("(");
		if (op.getParameters() != null) {
			descBuilder.append(toDescString(op.getParameters()));
		}
		descBuilder.append(")");

		
		if (op.getReturnType() != null && !typeUtils.isVoid(op.getReturnType())) {
			descBuilder.append(" : " + toDescString(op.getReturnType()));
		}
		
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(name);
		if (op.getParameters() != null && op.getParameters().size() > 0) {
			patternBuilder.append("(");
			patternBuilder.append(toPattern(op.getParameters()));
			patternBuilder.append(")");
		}		

		String id = "";
		if (op.getParameters() != null) {
			id = methodFinder.createExecutableID(op);
		} else {
			id = name;
		}
		Template template = createTemplate(descBuilder.toString(), declaredType.getSimpleName(), id, patternBuilder.toString());
		Image image = labelProvider.getImage(op);
		finishTemplate(template, templateContext, context, image, acceptor);
	}
	
	protected void createConstructorTemplate(String prefix, EList<JvmFormalParameter> parameters,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		
		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(prefix);
		descBuilder.append("(");
		if (parameters != null) {
			descBuilder.append(toDescString(parameters));
		}		
		descBuilder.append(")");

		
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(prefix);
		patternBuilder.append("(");
		if (parameters != null) {
			patternBuilder.append(toPattern(parameters));
		}
		patternBuilder.append(")");
		
		String id = prefix;
		if (parameters != null) {
			prefix = prefix+parameters.size();
		}

		Template template = createTemplate(descBuilder.toString(), "", id, patternBuilder.toString());
		finishTemplate(template, templateContext, context,  acceptor);
	}
	
	protected void createVariableTemplate(String name, JvmDeclaredType declaredType, JvmTypeReference typeRef,TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {

		StringBuilder descBuilder = new StringBuilder();
		descBuilder.append(name);
		descBuilder.append(" : " + toDescString(typeRef));

		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(name);

		String id = name;
		Template template = createTemplate(descBuilder.toString(), declaredType.getSimpleName(), id, patternBuilder.toString());
		Image image = imageHelper.getImage("localvariable_obj.gif");
		finishTemplate(template, templateContext, context, image, acceptor);
	}
		
	
	protected String firstCharUpperCase(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	protected String firstCharLowerCase(String s) {
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}
		
	protected Template createTemplate (String name, String desc, String id, String pattern) {
		return new Template(name, desc, id, pattern, false);
	}
	
	
	protected void finishTemplate(Template template, TemplateContext templateContext,ContentAssistContext context, ITemplateAcceptor acceptor) {
		TemplateProposal tp = createProposal(template, templateContext,	context, getImage(template), getRelevance(template));
		acceptor.accept(tp);
	}
	
	protected void finishTemplate(Template template, TemplateContext templateContext,ContentAssistContext context, Image image, ITemplateAcceptor acceptor) {
		TemplateProposal tp = createProposal(template, templateContext,	context, image, getRelevance(template));
		acceptor.accept(tp);
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
	
	protected String toDescString(JvmTypeReference typeRef) {		
		StringBuilder b = new StringBuilder();
		b.append(typeUtils.getShortName(typeRef, true));
		return b.toString();
	}
	
	protected String toDescString(JvmFormalParameter parameter) {
		StringBuilder b = new StringBuilder();
		b.append(toDescString(parameter.getParameterType()));
		b.append(" ");
		b.append(parameter.getName());
		return b.toString();
	}
	
	
	protected String toDescString(EList<JvmFormalParameter> parameters) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i<parameters.size(); i++) {
			if (i > 0) {
				b.append(", ");
			}
			b.append(toDescString(parameters.get(i)));
		}
		return b.toString();
	}
	

}
