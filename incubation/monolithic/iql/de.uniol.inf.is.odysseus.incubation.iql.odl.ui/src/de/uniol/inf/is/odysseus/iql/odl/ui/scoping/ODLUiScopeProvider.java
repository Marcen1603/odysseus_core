package de.uniol.inf.is.odysseus.iql.odl.ui.scoping;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtBasedTypeScope;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.IODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.scoping.ODLScopeProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLUiScopeProvider extends ODLScopeProvider {
	
	@Inject
	public ODLUiScopeProvider(IODLTypeDictionary typeDictionary, IODLLookUp lookUp,	IODLExpressionEvaluator exprEvaluator, IODLTypeUtils typeUtils) {
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

}
