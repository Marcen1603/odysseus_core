package de.uniol.inf.is.odysseus.iql.qdl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<IQDLTypeUtils, IQDLLookUp> {

	@Inject
	public QDLEObjectHoverProvider(IQDLTypeUtils typeUtils,IQDLLookUp lookUp) {
		super(typeUtils, lookUp);
	}

}
