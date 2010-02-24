package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class BrokerPO<T> extends AbstractPipe<T, T> {

	
	private volatile T currentProcessing;
	private String identifier;
	
	public BrokerPO(BrokerPO<T> po){
		this.identifier = po.getIdentifier();
	}

	private String getIdentifier() {
		return this.identifier;
	}

	public BrokerPO(String identifier){
		this.identifier = identifier;
	}
	//currentProcessing könnte probleme beim scheduling machen...
	@Override
	protected void process_next(T object, int port) {
		System.out.println("Broker processing: "+object.toString());
		if(currentProcessing!=null){
			if(object.equals(currentProcessing)){
				currentProcessing=null;
				// processed finished
				return;
			}
		}
		currentProcessing=object;
		transfer(object);	
		
	}

	@Override
	public OutputMode getOutputMode() { 
		return OutputMode.INPUT;
	}			
	
	@Override
	public String toString(){
		return super.toString()+" ("+this.identifier+")";
	}
}
