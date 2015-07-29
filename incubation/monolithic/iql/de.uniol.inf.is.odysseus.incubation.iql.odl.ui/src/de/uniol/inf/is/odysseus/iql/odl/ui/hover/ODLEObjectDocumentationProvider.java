package de.uniol.inf.is.odysseus.iql.odl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;

public class ODLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<ODLTypeFactory> {

	@Inject
	public ODLEObjectDocumentationProvider(ODLTypeFactory typeFactory) {
		super(typeFactory);
	}

}
