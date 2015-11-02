package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetComponent {

	public void init( IOdysseusNode localNode );
	
	public void start();
	public void stop();
	
	public void terminate( IOdysseusNode localNode );
}
