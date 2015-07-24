package de.uniol.inf.is.odysseus.iql.basic.typing.builder;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;

public class BasicIQLTypeBuilder extends AbstractIQLTypeBuilder<BasicIQLTypeFactory> {

	@Inject
	public BasicIQLTypeBuilder(BasicIQLTypeFactory typeFactory) {
		super(typeFactory);
	}
}
