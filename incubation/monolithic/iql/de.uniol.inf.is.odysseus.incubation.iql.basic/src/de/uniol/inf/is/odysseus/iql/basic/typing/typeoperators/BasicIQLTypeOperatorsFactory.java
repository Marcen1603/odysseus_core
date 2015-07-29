package de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;

@Singleton
public class BasicIQLTypeOperatorsFactory extends AbstractIQLTypeOperatorsFactory<BasicIQLTypeFactory> {

	@Inject
	public BasicIQLTypeOperatorsFactory(BasicIQLTypeFactory typeFactory) {
		super(typeFactory);
	}

}
