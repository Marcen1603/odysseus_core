package de.uniol.inf.is.odysseus.viewer.model.stream;


public interface IStreamElementListener<In> {

	public void streamElementRecieved( In element, int port );
	
}
