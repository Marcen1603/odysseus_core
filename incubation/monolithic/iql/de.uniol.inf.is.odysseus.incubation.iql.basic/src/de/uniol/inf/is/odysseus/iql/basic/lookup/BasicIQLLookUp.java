package de.uniol.inf.is.odysseus.iql.basic.lookup;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;

public class BasicIQLLookUp extends AbstractIQLLookUp<BasicIQLTypeFactory>{

	@Inject
	public BasicIQLLookUp(BasicIQLTypeFactory typeFactory) {
		super(typeFactory);
	}

}
