package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;

public class BrokerPO<T extends IMetaAttributeContainer<?>> extends AbstractPipe<T, T> {

	
	private volatile T currentProcessing;
	private String identifier;
	private SweepArea<T> sweepArea = new SweepArea<T>();
	
	public BrokerPO(BrokerPO<T> po){
		this.identifier = po.getIdentifier();
	}

	private String getIdentifier() {
		return this.identifier;
	}

	public BrokerPO(String identifier){
		this.identifier = identifier;
	}

	
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
