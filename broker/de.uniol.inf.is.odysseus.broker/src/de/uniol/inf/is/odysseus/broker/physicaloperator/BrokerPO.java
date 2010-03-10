package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.transaction.TransactionType;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerPO<T extends IMetaAttributeContainer<?>> extends AbstractPipe<T, T> {
	
	private volatile T currentProcessing;
	private String identifier;
	//private SweepArea<T> sweepArea = new SweepArea<T>();
	
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
		TransactionType.Write type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, port);
		System.out.println("Broker processing from "+type+": "+object.toString());
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
	public void subscribeToSource(ISource<? extends T> source, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		TransactionType.Write type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, sinkInPort);
		System.err.println("Adding "+source+" of type "+type.toString()+" to port "+sinkInPort);
		super.subscribeToSource(source, sinkInPort, sourceOutPort, schema);					
	};

	@Override
	public OutputMode getOutputMode() { 
		return OutputMode.INPUT;
	}			
	
	@Override
	public String toString(){
		return super.toString()+" ("+this.identifier+")";
	}
}
