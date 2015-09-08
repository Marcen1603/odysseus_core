package de.uniol.inf.is.odysseus.iql.odl.ui.labeling;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.labeling.AbstractIQLLabelProvider;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;

public class ODLLabelProvider extends AbstractIQLLabelProvider {

	@Inject
	public ODLLabelProvider(org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}
	
	String image(ODLOperator ele) {
		return "task.png";
	}
	
	String image(ODLParameter ele) {
		return "field_public_obj.gif";
	}
	
	String image(ODLMethod ele) {
		return "methpri_obj.gif";
	}

}
