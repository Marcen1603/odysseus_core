package de.uniol.inf.is.odysseus.iql.odl.ui.scoping;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtBasedTypeScope;
import de.uniol.inf.is.odysseus.iql.odl.exprevaluator.ODLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.scoping.ODLScopeProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLUiScopeProvider extends ODLScopeProvider {
	
	@Inject
	public ODLUiScopeProvider(ODLTypeFactory typeFactory, ODLLookUp lookUp,	ODLExpressionEvaluator exprEvaluator, ODLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprEvaluator, typeUtils);
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
