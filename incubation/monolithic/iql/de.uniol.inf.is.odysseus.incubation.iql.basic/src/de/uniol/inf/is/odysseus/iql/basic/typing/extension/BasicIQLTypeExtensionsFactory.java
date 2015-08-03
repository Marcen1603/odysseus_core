package de.uniol.inf.is.odysseus.iql.basic.typing.extension;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

@Singleton
public class BasicIQLTypeExtensionsFactory extends AbstractIQLTypeExtensionsFactory<BasicIQLTypeFactory, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLTypeExtensionsFactory(BasicIQLTypeFactory typeFactory, BasicIQLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

}
