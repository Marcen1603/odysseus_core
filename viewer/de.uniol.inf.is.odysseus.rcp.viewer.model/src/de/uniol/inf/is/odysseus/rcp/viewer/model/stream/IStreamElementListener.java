package de.uniol.inf.is.odysseus.rcp.viewer.model.stream;


public interface IStreamElementListener<In> {

	public void streamElementRecieved( In element, int port );
	
}
