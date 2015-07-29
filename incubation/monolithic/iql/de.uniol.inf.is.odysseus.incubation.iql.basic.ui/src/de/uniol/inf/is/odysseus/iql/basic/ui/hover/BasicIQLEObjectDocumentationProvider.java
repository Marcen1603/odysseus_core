package de.uniol.inf.is.odysseus.iql.basic.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;

public class BasicIQLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<BasicIQLTypeFactory> {

	@Inject
	public BasicIQLEObjectDocumentationProvider(BasicIQLTypeFactory typeFactory) {
		super(typeFactory);
	}

}
