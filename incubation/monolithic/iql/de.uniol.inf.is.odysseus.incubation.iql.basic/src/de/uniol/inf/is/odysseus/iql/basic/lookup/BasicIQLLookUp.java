package de.uniol.inf.is.odysseus.iql.basic.lookup;

import javax.inject.Inject;


import de.uniol.inf.is.odysseus.iql.basic.typing.extension.BasicIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLLookUp extends AbstractIQLLookUp<BasicIQLTypeFactory, BasicIQLTypeExtensionsFactory, BasicIQLTypeUtils>{

	@Inject
	public BasicIQLLookUp(BasicIQLTypeFactory typeFactory,BasicIQLTypeExtensionsFactory typeOperatorsFactory, BasicIQLTypeUtils typeUtils) {
		super(typeFactory, typeOperatorsFactory, typeUtils);
	}


}
