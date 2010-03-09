package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerPO<T extends IMetaAttributeContainer<?>> extends AbstractPipe<T, T> {

	private int portCounter = 0;
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
		//transfer(object);	
		
	}
	
	@Override
	public void subscribeToSource(ISource<? extends T> source, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		System.out.println("BPO: Adding "+source+" to Broker on PORT "+sinkInPort);
		System.out.println("*************************");
		System.out.println("ALL SUBSCRIPTIONS BEFORE:");
		for(Subscription sub :this.getSubscribedToSource()){
			System.out.println(sub.getTarget().toString());
		}
		System.out.println("****************");
		super.subscribeToSource(source, sinkInPort, sourceOutPort, schema);	
		System.out.println("ALL SUBSCRIPTIONS AFTER:");
		
		for(Subscription sub :this.getSubscribedToSource()){
			System.out.println(sub.getTarget().toString());
		}
		System.out.println("****************");
		
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
