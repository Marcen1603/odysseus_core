package de.uniol.infs.is.odysseus.scars.operator.brokerinit;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;

public class BrokerInitPO<R> extends AbstractPipe<R, R> {

	private int size = 1;
	private int counter = 0;
	private int punctuationCounter = 0;
	
	public BrokerInitPO() {
		
	}
	
	public BrokerInitPO( BrokerInitPO<R> po ) {
		setSize( po.getSize() );
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
//		if( punctuationCounter < size ) {
			printOutput("Send Punctuation: " + timestamp);
			sendPunctuation(timestamp);
//			punctuationCounter++;
//		}
	}

	@Override
	protected void process_next(R object, int port) {
		if( counter < size ) {
			printOutput("Send tuple");
			transfer(object);
			counter++;
		}
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new BrokerInitPO<R>(this);
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize( int size ) {
		if( size < 1 ) throw new IllegalArgumentException("size of BrokerInitPO must be posistive");
		this.size = size;
	}
	  
	  private void printOutput( String txt ) {
		  System.out.println("BROKER-INIT(" + hashCode() + "):" + txt);
	  }
}
