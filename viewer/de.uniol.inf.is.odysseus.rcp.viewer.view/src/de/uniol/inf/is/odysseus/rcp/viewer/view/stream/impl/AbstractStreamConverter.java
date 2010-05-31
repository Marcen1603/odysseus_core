package de.uniol.inf.is.odysseus.rcp.viewer.view.stream.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.view.stream.IStreamElementConverter;
import de.uniol.inf.is.odysseus.rcp.viewer.view.stream.Parameters;



public abstract class AbstractStreamConverter<In,Out> implements IStreamElementConverter<In,Out> {

	private Parameters parameters;
	
	public AbstractStreamConverter( Parameters params ) {
		parameters = params;
	}
	
	public final Parameters getParameters() {
		return parameters;
	}

}
