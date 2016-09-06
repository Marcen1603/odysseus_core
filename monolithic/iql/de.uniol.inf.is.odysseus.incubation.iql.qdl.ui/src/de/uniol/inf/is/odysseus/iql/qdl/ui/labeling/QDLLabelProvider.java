package de.uniol.inf.is.odysseus.iql.qdl.ui.labeling;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.labeling.AbstractIQLLabelProvider;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;


public class QDLLabelProvider extends AbstractIQLLabelProvider {

	@Inject
	public QDLLabelProvider(org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}
	
	String image(QDLQuery ele) {
		return "graph.png";
	}

}
