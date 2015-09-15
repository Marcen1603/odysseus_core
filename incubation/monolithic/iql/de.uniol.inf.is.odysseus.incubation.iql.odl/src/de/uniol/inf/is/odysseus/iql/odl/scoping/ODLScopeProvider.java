package de.uniol.inf.is.odysseus.iql.odl.scoping;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;


public class ODLScopeProvider extends AbstractIQLScopeProvider<IODLTypeDictionary, IODLLookUp, IODLExpressionEvaluator, IODLTypeUtils> implements IODLScopeProvider{

	@Inject
	public ODLScopeProvider(IODLTypeDictionary typeDictionary, IODLLookUp lookUp,IODLExpressionEvaluator exprEvaluator, IODLTypeUtils typeUtils) {
		super(typeDictionary, lookUp, exprEvaluator, typeUtils);
	}
	
	@Override
	public Collection<IEObjectDescription> getScopeIQLMemberSelection(IQLMemberSelectionExpression expr) {
		Collection<IEObjectDescription> result = super.getScopeIQLMemberSelection(expr);
		
		TypeResult typeResult = exprEvaluator.eval(expr.getLeftOperand());
		if(!typeResult.isNull()){
			boolean isThis = expr.getLeftOperand() instanceof IQLThisExpression;

			ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);

			if (operator != null && isThis) {
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
		if (type instanceof ODLOperator) {
			ODLOperator op = (ODLOperator) type;
			
			JvmTypeReference thisType = typeUtils.createTypeRef(op);

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
