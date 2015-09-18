package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;


public class QDLScopeProvider extends AbstractIQLScopeProvider<IQDLTypeDictionary, IQDLLookUp, IQDLExpressionEvaluator, IQDLTypeUtils> implements IQDLScopeProvider {

	@Inject
	public QDLScopeProvider(IQDLTypeDictionary typeDictionary, IQDLLookUp lookUp,IQDLExpressionEvaluator exprEvaluator, IQDLTypeUtils typeUtils) {
		super(typeDictionary, lookUp, exprEvaluator, typeUtils);
	}
	
	@Override
	protected Collection<JvmField> getIQLMemberSelectionAttributes(IQLMemberSelectionExpression expr, JvmTypeReference typeRef, boolean isThis, boolean isSuper) {
		Collection<JvmField> attributes = super.getIQLMemberSelectionAttributes(expr, typeRef, isThis, isSuper);
		if (isThis) {
			for (IQLClass source : typeDictionary.getSourceTypes()) {
				for (JvmField attr : lookUp.getPublicAttributes(typeUtils.createTypeRef(source), false)) {
					if (attr.getSimpleName().equalsIgnoreCase(source.getSimpleName())) {
						attributes.add(attr);
					}
				}
			}
		}
		return attributes;
	}
	
	@Override
	protected Collection<JvmIdentifiableElement> getAttributesIQLJvmElementCallExpression(EObject node, JvmTypeReference thisType, Collection<JvmTypeReference> importedTypes) {
		Collection<JvmIdentifiableElement> elements = super.getAttributesIQLJvmElementCallExpression(node, thisType, importedTypes);
		QDLQuery query = EcoreUtil2.getContainerOfType(node, QDLQuery.class);
		if (node instanceof QDLQuery || query != null) {			
			for (IQLClass source : typeDictionary.getSourceTypes()) {
				for (JvmField attr : lookUp.getPublicAttributes(typeUtils.createTypeRef(source), false)) {
					if (attr.getSimpleName().equalsIgnoreCase(source.getSimpleName())) {
						elements.add(attr);
					}
				}
			}
		}		
		return elements;
	}

	@Override
	protected IScope getJdtScope(ResourceSet set,IIQLJdtTypeProvider typeProvider) {
		return IScope.NULLSCOPE;
	}
	
}
