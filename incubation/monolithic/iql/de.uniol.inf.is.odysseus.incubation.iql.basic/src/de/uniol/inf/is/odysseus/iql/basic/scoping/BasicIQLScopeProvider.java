package de.uniol.inf.is.odysseus.iql.basic.scoping;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.BasicIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;


public class BasicIQLScopeProvider extends AbstractIQLScopeProvider<BasicIQLTypeFactory, BasicIQLLookUp, BasicIQLExpressionParser, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLScopeProvider(BasicIQLTypeFactory typeFactory,BasicIQLLookUp lookUp, BasicIQLExpressionParser exprParser,BasicIQLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprParser, typeUtils);
	}

}
