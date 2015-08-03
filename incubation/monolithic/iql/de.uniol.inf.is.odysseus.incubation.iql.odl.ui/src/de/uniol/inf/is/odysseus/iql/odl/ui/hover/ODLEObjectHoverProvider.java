package de.uniol.inf.is.odysseus.iql.odl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<ODLTypeUtils, ODLLookUp> {

	@Inject
	public ODLEObjectHoverProvider(ODLTypeUtils typeUtils, ODLLookUp lookUp) {
		super(typeUtils, lookUp);
	}

}
