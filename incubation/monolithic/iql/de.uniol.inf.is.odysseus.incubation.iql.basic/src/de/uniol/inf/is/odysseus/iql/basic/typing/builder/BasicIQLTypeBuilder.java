package de.uniol.inf.is.odysseus.iql.basic.typing.builder;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLTypeBuilder extends AbstractIQLTypeBuilder<BasicIQLTypeFactory, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLTypeBuilder(BasicIQLTypeFactory typeFactory, BasicIQLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}
}
