package de.uniol.inf.is.odysseus.iql.basic.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<BasicIQLTypeUtils, BasicIQLLookUp> {

	@Inject
	public BasicIQLEObjectHoverProvider(BasicIQLTypeUtils typeUtils, BasicIQLLookUp lookUp) {
		super(typeUtils, lookUp);
	}

}
