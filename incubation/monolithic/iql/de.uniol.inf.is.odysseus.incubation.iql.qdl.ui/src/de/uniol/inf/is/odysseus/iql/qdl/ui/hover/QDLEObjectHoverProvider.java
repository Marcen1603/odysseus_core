package de.uniol.inf.is.odysseus.iql.qdl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;

public class QDLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<QDLTypeUtils, QDLLookUp> {

	@Inject
	public QDLEObjectHoverProvider(QDLTypeUtils typeUtils,QDLLookUp lookUp) {
		super(typeUtils, lookUp);
	}

}
