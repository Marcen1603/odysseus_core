package de.uniol.inf.is.odysseus.iql.basic.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;

public class BasicIQLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<BasicIQLTypeFactory, BasicIQLLookUp> {

	@Inject
	public BasicIQLEObjectHoverProvider(BasicIQLTypeFactory typeFactory, BasicIQLLookUp lookUp) {
		super(typeFactory, lookUp);
	}

}
