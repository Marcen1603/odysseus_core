package de.uniol.inf.is.odysseus.iql.qdl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectHoverProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;

public class QDLEObjectHoverProvider extends AbstractIQLEObjectHoverProvider<QDLTypeFactory, QDLLookUp> {

	@Inject
	public QDLEObjectHoverProvider(QDLTypeFactory typeFactory,QDLLookUp lookUp) {
		super(typeFactory, lookUp);
	}

}
