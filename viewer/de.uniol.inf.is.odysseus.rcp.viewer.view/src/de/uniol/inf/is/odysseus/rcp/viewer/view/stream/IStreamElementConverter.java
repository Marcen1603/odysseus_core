package de.uniol.inf.is.odysseus.rcp.viewer.view.stream;


public interface IStreamElementConverter<In, Out> {

	public Out convertStreamElement( In element );
	
}
