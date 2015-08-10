package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
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
			EObject container = expr;
			while (container != null && !(container instanceof JvmDeclaredType)) {
				elements.addAll(EcoreUtil2.getAllContentsOfType(container, IQLVariableDeclaration.class));
				elements.addAll(EcoreUtil2.getAllContentsOfType(container, JvmFormalParameter.class));
				container = container.eContainer();
			}
			Collection<JvmTypeReference> importedTypes = typeFactory.getImportedTypes(expr);
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
					}
				} 
				result.add(EObjectDescription.create(qualifiedNameProvider.getFullyQualifiedName(element), element));
			}
			return result;
		} else {
			return super.getIQLJvmElementCallExpression(expr);
		}	
	}
	
}
