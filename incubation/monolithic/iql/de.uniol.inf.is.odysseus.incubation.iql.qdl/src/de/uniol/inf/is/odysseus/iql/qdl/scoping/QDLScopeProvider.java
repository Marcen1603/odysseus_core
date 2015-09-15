package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
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
	public Collection<IEObjectDescription> getScopeIQLMemberSelection(IQLMemberSelectionExpression expr) {
		Collection<IEObjectDescription> result = super.getScopeIQLMemberSelection(expr);
		TypeResult typeResult = exprEvaluator.eval(expr.getLeftOperand());
		
		if(!typeResult.isNull()){
			boolean isThis = expr.getLeftOperand() instanceof IQLThisExpression;
			
			QDLQuery query  = EcoreUtil2.getContainerOfType(expr, QDLQuery.class);

			if (query != null && isThis) {
				result.addAll(getScopeIQLAttributeSelection(lookUp.getSuperType(expr),false, true));
				result.addAll(getScopeIQLMethodSelection(lookUp.getSuperType(expr),false, true));
			}

		}
		return result;
	}

	
	@Override
	protected Collection<JvmIdentifiableElement> getElementsIQLJvmElementCallExpression(EObject expr) {
		Collection<JvmIdentifiableElement> elements = super.getElementsIQLJvmElementCallExpression(expr);
		JvmDeclaredType type = EcoreUtil2.getContainerOfType(expr, JvmDeclaredType.class);
		if (type instanceof QDLQuery) {
			QDLQuery query = (QDLQuery) type;
			
			for (IQLClass source : typeDictionary.getSourceTypes()) {
				for (JvmField attr : lookUp.getPublicAttributes(typeUtils.createTypeRef(source), false)) {
					if (attr.getSimpleName().equalsIgnoreCase(source.getSimpleName())) {
						elements.add(attr);
					}
				}
			}
			
			JvmTypeReference thisType = typeUtils.createTypeRef(query);

			Collection<JvmTypeReference> importedTypes = typeDictionary.getStaticImports(expr);			

			elements.addAll(lookUp.getPublicAttributes(thisType, importedTypes, true));
			elements.addAll(lookUp.getProtectedAttributes(thisType, importedTypes, true));
			
			elements.addAll(lookUp.getPublicMethods(thisType, importedTypes,true));
			elements.addAll(lookUp.getProtectedMethods(thisType, importedTypes,true));
		}
		return elements;
	}




	@Override
	protected IScope getJdtScope(ResourceSet set,IIQLJdtTypeProvider typeProvider) {
		return IScope.NULLSCOPE;
	}
	
}
