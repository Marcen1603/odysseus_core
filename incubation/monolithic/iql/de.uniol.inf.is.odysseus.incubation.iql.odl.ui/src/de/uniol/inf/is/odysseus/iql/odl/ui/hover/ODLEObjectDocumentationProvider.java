package de.uniol.inf.is.odysseus.iql.odl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.factory.IODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<IODLTypeFactory, IODLTypeUtils> {

	@Inject
	public ODLEObjectDocumentationProvider(IODLTypeFactory typeFactory, IODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

}
