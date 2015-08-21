package de.uniol.inf.is.odysseus.iql.qdl.ui.scoping;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtBasedTypeScope;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.factory.IQDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;


public class QDLUiScopeProvider extends QDLScopeProvider {

	@Inject
	public QDLUiScopeProvider(IQDLTypeFactory typeFactory, IQDLLookUp lookUp,	IQDLExpressionEvaluator exprEvaluator, IQDLTypeUtils typeUtils) {
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
