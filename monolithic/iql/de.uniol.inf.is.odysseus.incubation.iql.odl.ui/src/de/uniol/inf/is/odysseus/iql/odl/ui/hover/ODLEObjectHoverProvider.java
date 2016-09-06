package de.uniol.inf.is.odysseus.iql.odl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<IODLTypeUtils, IODLLookUp> {

	@Inject
	public ODLEObjectHoverProvider(IODLTypeUtils typeUtils, IODLLookUp lookUp) {
		super(typeUtils, lookUp);
	}

}
