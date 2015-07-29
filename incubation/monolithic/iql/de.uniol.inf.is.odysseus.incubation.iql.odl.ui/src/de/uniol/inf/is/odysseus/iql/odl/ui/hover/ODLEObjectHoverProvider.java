package de.uniol.inf.is.odysseus.iql.odl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;

public class ODLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<ODLTypeFactory, ODLLookUp> {

	@Inject
	public ODLEObjectHoverProvider(ODLTypeFactory typeFactory, ODLLookUp lookUp) {
		super(typeFactory, lookUp);
	}

}
