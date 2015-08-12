package de.uniol.inf.is.odysseus.iql.odl.scoping;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;


public class ODLScopeProvider extends AbstractIQLScopeProvider<ODLTypeFactory, ODLLookUp, ODLExpressionParser, ODLTypeUtils>{

	@Inject
	public ODLScopeProvider(ODLTypeFactory typeFactory, ODLLookUp lookUp,ODLExpressionParser exprParser, ODLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprParser, typeUtils);
	}
	
	@Override
	public Collection<IEObjectDescription> getScopeIQLMemberSelection(IQLMemberSelectionExpression expr) {
		Collection<IEObjectDescription> result = new HashSet<>();
		TypeResult typeResult = exprParser.getType(expr.getLeftOperand());
		if (!typeResult.isNull() && typeUtils.isArray(typeResult.getRef())) {
			result.addAll(getScopeIQLAttributeSelection(typeUtils.createTypeRef(List.class, typeFactory.getSystemResourceSet()), false, false));
			result.addAll(getScopeIQLMethodSelection(typeUtils.createTypeRef(List.class, typeFactory.getSystemResourceSet()), false, false));

		} else if(!typeResult.isNull()){
			boolean isThis = exprParser.isThis(expr.getLeftOperand());
			boolean isSuper = exprParser.isSuper(expr.getLeftOperand());
			
			result.addAll(getScopeIQLAttributeSelection(typeResult.getRef(),isThis, isSuper));
			result.addAll(getScopeIQLMethodSelection(typeResult.getRef(),isThis, isSuper));
			
			ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);

			if (operator != null && isThis) {
				result.addAll(getScopeIQLAttributeSelection(exprParser.getSuperType(expr).getRef(),false, true));
				result.addAll(getScopeIQLMethodSelection(exprParser.getSuperType(expr).getRef(),false, true));
			}

		}
		return result;
	}

	@Override
	public Collection<IEObjectDescription> getIQLJvmElementCallExpression(EObject expr) {
		JvmDeclaredType type = EcoreUtil2.getContainerOfType(expr, JvmDeclaredType.class);
		if (type instanceof ODLOperator) {
			ODLOperator op = (ODLOperator) type;

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
			
			Collection<JvmTypeReference> importedTypes = typeFactory.getImportedTypes(expr);

			JvmTypeReference thisType = typeUtils.createTypeRef(op);

			elements.addAll(lookUp.getPublicAttributes(thisType, importedTypes, true));
			elements.addAll(lookUp.getProtectedAttributes(thisType, importedTypes, true));
			
			elements.addAll(lookUp.getPublicMethods(thisType, importedTypes,true));
			elements.addAll(lookUp.getProtectedMethods(thisType, importedTypes,true));
			
			JvmTypeReference superType = exprParser.getSuperType(expr).getRef();

			elements.addAll(lookUp.getPublicAttributes(superType, importedTypes, true));
			elements.addAll(lookUp.getProtectedAttributes(superType, importedTypes, true));
			
			elements.addAll(lookUp.getPublicMethods(superType, importedTypes,true));
			elements.addAll(lookUp.getProtectedMethods(superType, importedTypes,true));

	
					
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
					}
				} else if (element instanceof JvmField) {
					JvmField field = (JvmField) element;
					if (field.isStatic()) {
						JvmDeclaredType declaredType = (JvmDeclaredType) element.eContainer();
						result.add(EObjectDescription.create(declaredType.getSimpleName()+IQLQualifiedNameConverter.DELIMITER+qualifiedNameProvider.getFullyQualifiedName(field), field));
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
