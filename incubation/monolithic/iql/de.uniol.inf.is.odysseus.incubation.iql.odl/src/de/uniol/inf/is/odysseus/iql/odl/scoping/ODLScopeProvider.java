package de.uniol.inf.is.odysseus.iql.odl.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;


public class ODLScopeProvider extends AbstractIQLScopeProvider<ODLTypeFactory, ODLLookUp, ODLExpressionParser>{

	@Inject
	public ODLScopeProvider(ODLTypeFactory typeFactory, ODLLookUp lookUp,ODLExpressionParser exprParser) {
		super(typeFactory, lookUp, exprParser);
	}

}
