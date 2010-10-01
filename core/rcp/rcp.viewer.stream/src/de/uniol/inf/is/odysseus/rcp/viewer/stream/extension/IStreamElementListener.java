package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import de.uniol.inf.is.odysseus.metadata.PointInTime;


public interface IStreamElementListener<In> {

	public void streamElementRecieved( In element, int port );
	public void punctuationElementRecieved( PointInTime point, int port );
}
