package de.uniol.inf.is.odysseus.iql.basic.ui.scoping;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.scoping.IScope;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.BasicIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLUiScopeProvider extends BasicIQLScopeProvider {
	
	@Inject
	public BasicIQLUiScopeProvider(BasicIQLTypeFactory typeFactory,
			BasicIQLLookUp lookUp, BasicIQLExpressionParser exprParser,
			BasicIQLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprParser, typeUtils);
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
