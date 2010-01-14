package de.uniol.inf.is.odysseus.viewer.view.stream;



public abstract class AbstractStreamConverter<In,Out> implements IStreamElementConverter<In,Out> {

	private Parameters parameters;
	
	public AbstractStreamConverter( Parameters params ) {
		parameters = params;
	}
	
	public final Parameters getParameters() {
		return parameters;
	}

}
