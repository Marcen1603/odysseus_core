package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;


public class QDLScopeProvider extends AbstractIQLScopeProvider<QDLTypeFactory, QDLLookUp, QDLExpressionParser, QDLTypeUtils> {

	@Inject
	public QDLScopeProvider(QDLTypeFactory typeFactory, QDLLookUp lookUp,QDLExpressionParser exprParser, QDLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprParser, typeUtils);
	}


	@Override	
	public Collection<IEObjectDescription> getIQLJvmElementCallExpression(EObject expr) {
		JvmDeclaredType type = EcoreUtil2.getContainerOfType(expr, JvmDeclaredType.class);
		if (type instanceof QDLQuery) {
			QDLQuery query = (QDLQuery) type;
			Collection<JvmIdentifiableElement> elements = new HashSet<>();
			Set<String> vars = new HashSet<>();
			EObject container = expr;
			EObject lastContainer = null;
			while (container != null && !(container instanceof JvmDeclaredType)) {
				for (EObject obj : container.eContents()) {
					if (container instanceof IQLStatementBlock && obj == lastContainer) {
						break;
					}
					if (obj instanceof IQLVariableDeclaration) {
						IQLVariableDeclaration var = (IQLVariableDeclaration) obj;
						if (!vars.contains(var.getName())) {
							vars.add(var.getName());
							elements.add(var);
						}
					} else if (obj instanceof JvmFormalParameter) {
						JvmFormalParameter parameter = (JvmFormalParameter) obj;
						if (!vars.contains(parameter.getName())) {
							vars.add(parameter.getName());
							elements.add(parameter);
						}
					} else if (obj instanceof IQLVariableStatement) {
						for (IQLVariableDeclaration var : EcoreUtil2.getAllContentsOfType(obj, IQLVariableDeclaration.class)) {
							if (!vars.contains(var.getName())) {
								vars.add(var.getName());
								elements.add(var);
							}
						}
						for (JvmFormalParameter parameter : EcoreUtil2.getAllContentsOfType(obj, JvmFormalParameter.class)) {
							if (!vars.contains(parameter.getName())) {
								vars.add(parameter.getName());
								elements.add(parameter);
							}
						}
					}					
				}
				
				lastContainer = container;
				container = container.eContainer();
			}
			
			for (IQLClass source : typeFactory.getSourceTypes()) {
				for (JvmField attr : lookUp.getPublicAttributes(typeUtils.createTypeRef(source), false)) {
					if (attr.getSimpleName().equalsIgnoreCase(source.getSimpleName())) {
						elements.add(attr);
					}
				}
			}
			
			Collection<JvmTypeReference> importedTypes = typeFactory.getStaticImports(expr);
			elements.addAll(lookUp.getPublicAttributes(typeUtils.createTypeRef(query), importedTypes, true));
			elements.addAll(lookUp.getProtectedAttributes(typeUtils.createTypeRef(query), importedTypes, true));
		
			elements.addAll(lookUp.getPublicMethods(typeUtils.createTypeRef(query), importedTypes,true));
			elements.addAll(lookUp.getProtectedMethods(typeUtils.createTypeRef(query), importedTypes,true));

			for (IQLClass source : typeFactory.getSourceTypes()) {
				for (JvmField attr : lookUp.getPublicAttributes(typeUtils.createTypeRef(source), false)) {
					if (attr.getSimpleName().equalsIgnoreCase(source.getSimpleName())) {
						elements.add(attr);
					}
				}
			}	
			
			Collection<IEObjectDescription> result = new HashSet<>();
			for (JvmIdentifiableElement element : elements) {
				if (element instanceof JvmOperation) {
					JvmOperation method = (JvmOperation) element;
					if (method.getSimpleName().startsWith("set")) {
						result.add(EObjectDescription.create(firstCharLowerCase(method.getSimpleName().substring(3)), method));
					} else if (method.getSimpleName().startsWith("get")) {
						result.add(EObjectDescription.create(firstCharLowerCase(method.getSimpleName().substring(3)), method));
					} else if (method.getSimpleName().startsWith("is")) {
						result.add(EObjectDescription.create(firstCharLowerCase(method.getSimpleName().substring(2)), method));
					}
					if (method.isStatic()) {
						JvmDeclaredType declaredType = (JvmDeclaredType) method.eContainer();
						result.add(EObjectDescription.create(declaredType.getSimpleName()+IQLQualifiedNameConverter.DELIMITER+qualifiedNameProvider.getFullyQualifiedName(method), method));
					} else if (element instanceof JvmField) {
						JvmField field = (JvmField) element;
						if (field.isStatic()) {
							JvmDeclaredType declaredType = (JvmDeclaredType) element.eContainer();
							result.add(EObjectDescription.create(declaredType.getSimpleName()+IQLQualifiedNameConverter.DELIMITER+qualifiedNameProvider.getFullyQualifiedName(field), field));
						}
					}
				} 
				result.add(EObjectDescription.create(qualifiedNameProvider.getFullyQualifiedName(element), element));
			} 
			return result;
		} else {
			return super.getIQLJvmElementCallExpression(expr);
		}	
	}



	@Override
	protected IScope getJdtScope(ResourceSet set,IIQLJdtTypeProvider typeProvider) {
		return IScope.NULLSCOPE;
	}
	
}
