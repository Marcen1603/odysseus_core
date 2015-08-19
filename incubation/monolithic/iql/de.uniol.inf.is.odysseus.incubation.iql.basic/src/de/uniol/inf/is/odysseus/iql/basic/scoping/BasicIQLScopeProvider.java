package de.uniol.inf.is.odysseus.iql.basic.scoping;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.BasicIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;


public class BasicIQLScopeProvider extends AbstractIQLScopeProvider<BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLExpressionEvaluator, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLScopeProvider(BasicIQLTypeFactory typeFactory,BasicIQLLookUp lookUp, BasicIQLExpressionEvaluator exprEvaluator,BasicIQLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprEvaluator, typeUtils);
	}

	@Override
	protected IScope getJdtScope(ResourceSet set,IIQLJdtTypeProvider typeProvider) {
		return IScope.NULLSCOPE;
	}


}
