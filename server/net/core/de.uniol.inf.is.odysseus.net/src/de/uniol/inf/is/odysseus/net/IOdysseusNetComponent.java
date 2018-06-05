package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetComponent {

	public void init( IOdysseusNode localNode ) throws OdysseusNetException;
	
	public void start() throws OdysseusNetException;
	public void startFinished() throws OdysseusNetException;	
	
	public void stop();
	
	public void terminate( IOdysseusNode localNode );
}
