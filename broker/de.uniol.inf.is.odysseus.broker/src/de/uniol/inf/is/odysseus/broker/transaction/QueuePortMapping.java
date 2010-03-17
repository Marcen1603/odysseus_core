package de.uniol.inf.is.odysseus.broker.transaction;

public class QueuePortMapping extends AbstractPortTuple{

	public QueuePortMapping(int dataReadingPort, int queueWritingPort) {
		super(2);
		super.setPort(0, dataReadingPort);
		super.setPort(1, queueWritingPort);
	}
	
	public int getDataReadingPort(){
		return super.getPort(0);		
	}
	
	public int getQueueWritingPort(){
		return super.getPort(1);
	}
	

}
