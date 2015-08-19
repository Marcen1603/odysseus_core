package de.uniol.inf.is.odysseus.iql.qdl.ui.scoping;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.ui.scoping.IQLJdtBasedTypeScope;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.QDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.QDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;


public class QDLUiScopeProvider extends QDLScopeProvider {

	@Inject
	public QDLUiScopeProvider(QDLTypeFactory typeFactory, QDLLookUp lookUp,	QDLExpressionEvaluator exprEvaluator, QDLTypeUtils typeUtils) {
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
