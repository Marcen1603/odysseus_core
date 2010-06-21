package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;


public interface IStreamElementListener<In> {

	public void streamElementRecieved( In element, int port );
	
}
