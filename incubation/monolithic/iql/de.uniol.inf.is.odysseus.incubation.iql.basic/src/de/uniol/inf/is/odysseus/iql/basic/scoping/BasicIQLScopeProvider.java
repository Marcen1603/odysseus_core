package de.uniol.inf.is.odysseus.iql.basic.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;


public class BasicIQLScopeProvider extends AbstractIQLScopeProvider<BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLExpressionParser> {

	@Inject
	public BasicIQLScopeProvider(BasicIQLTypeFactory typeFactory,BasicIQLLookUp lookUp, BasicIQLExpressionParser exprParser) {
		super(typeFactory, lookUp, exprParser);
	}

}
