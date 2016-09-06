package de.uniol.inf.is.odysseus.iql.basic.ui.scoping;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.BasicIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.BasicIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;

public class BasicIQLUiScopeProvider extends BasicIQLScopeProvider {
	
	@Inject
	public BasicIQLUiScopeProvider(BasicIQLTypeDictionary typeDictionary,
			BasicIQLLookUp lookUp, BasicIQLExpressionEvaluator exprEvaluator,
			BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, lookUp, exprEvaluator, typeUtils);
	}
	

	@Override
	protected IScope getJdtScope(ResourceSet set,IIQLJdtTypeProvider typeProvider) {
		if (typeProvider != null) {
			return new IQLJdtBasedTypeScope(typeProvider, converter, null);
		} else {
			return IScope.NULLSCOPE;
		}
	}
	
	@Override
	protected Collection<String> getImplicitImports(EObject obj) {
		Collection<String> result = super.getImplicitImports(obj);
		String packageName = BasicIQLUiTypeUtils.getPackage(obj.eResource());
		if (packageName != null && packageName.length() > 0) {
			result.add(packageName+IQLQualifiedNameConverter.DELIMITER+"*");
		}
		return result;
	}


}
