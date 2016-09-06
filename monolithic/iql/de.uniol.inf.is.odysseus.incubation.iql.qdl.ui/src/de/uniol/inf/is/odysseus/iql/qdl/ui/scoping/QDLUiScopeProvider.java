package de.uniol.inf.is.odysseus.iql.qdl.ui.scoping;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtBasedTypeScope;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;


public class QDLUiScopeProvider extends QDLScopeProvider {

	@Inject
	public QDLUiScopeProvider(IQDLTypeDictionary typeDictionary, IQDLLookUp lookUp,	IQDLExpressionEvaluator exprEvaluator, IQDLTypeUtils typeUtils) {
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
