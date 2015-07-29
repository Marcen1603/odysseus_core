package de.uniol.inf.is.odysseus.iql.basic.lookup;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.BasicIQLTypeOperatorsFactory;

public class BasicIQLLookUp extends AbstractIQLLookUp<BasicIQLTypeFactory, BasicIQLTypeOperatorsFactory>{

	@Inject
	public BasicIQLLookUp(BasicIQLTypeFactory typeFactory,BasicIQLTypeOperatorsFactory typeOperatorsFactory) {
		super(typeFactory, typeOperatorsFactory);
	}

}
