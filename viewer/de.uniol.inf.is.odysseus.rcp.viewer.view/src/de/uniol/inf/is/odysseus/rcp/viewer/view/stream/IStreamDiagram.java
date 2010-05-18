package de.uniol.inf.is.odysseus.rcp.viewer.view.stream;


public interface IStreamDiagram<Out> {

	public void dataElementRecived( Out element, int port );
	public void reset();
	
}
