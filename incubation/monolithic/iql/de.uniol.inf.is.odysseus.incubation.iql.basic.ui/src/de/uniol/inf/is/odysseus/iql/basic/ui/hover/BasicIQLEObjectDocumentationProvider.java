package de.uniol.inf.is.odysseus.iql.basic.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<BasicIQLTypeFactory, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLEObjectDocumentationProvider(BasicIQLTypeFactory typeFactory, BasicIQLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

}
