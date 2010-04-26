package de.uniol.inf.is.odysseus.broker.transaction;

/**
 * QueuePortMapping maps a writing port of a queue stream to a reading port of a data stream.
 * 
 * @author Dennis Geesen
 */
public class QueuePortMapping extends AbstractPortTuple{

	/**
	 * Instantiates a new queue port mapping.
	 *
	 * @param dataReadingPort the data reading port
	 * @param queueWritingPort the queue writing port
	 */
	public QueuePortMapping(int dataReadingPort, int queueWritingPort) {
		super(2);
		super.setPort(0, dataReadingPort);
		super.setPort(1, queueWritingPort);
	}
	
	/**
	 * Gets the data reading port.
	 *
	 * @return the data reading port
	 */
	public int getDataReadingPort(){
		return super.getPort(0);		
	}
	
	/**
	 * Gets the queue writing port.
	 *
	 * @return the queue writing port
	 */
	public int getQueueWritingPort(){
		return super.getPort(1);
	}
	

}
