package de.uniol.inf.is.odysseus.iql.odl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<ODLTypeFactory, ODLTypeUtils> {

	@Inject
	public ODLEObjectDocumentationProvider(ODLTypeFactory typeFactory, ODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

}
