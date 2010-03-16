package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.List;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.transaction.CycleSubscription;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerPO<T extends IMetaAttributeContainer<?>> extends AbstractPipe<T, T> {
	
	private String identifier;
	//private SweepArea<T> sweepArea = new SweepArea<T>();
	
	public BrokerPO(BrokerPO<T> po){
		this.identifier = po.getIdentifier();
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public BrokerPO(String identifier){
		this.identifier = identifier;
	}

	
	@Override
	protected void process_next(T object, int port) {
		WriteTransaction type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, port);
		System.err.println("Process from "+port+" "+type+": "+object.toString()+"  ("+this+")");
		if(type==WriteTransaction.Cyclic){
			return;
		}
		
		int to = BrokerDictionary.getInstance().getAllReadingPorts(identifier).length;
		for(int i=0;i<to;i++){
			transfer(object, i);		
		}
	}
	
	protected void process_transfer(T object, int sourceOutPort) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.subscriptions) {
				if(sink.getSourceOutPort()==sourceOutPort){
					sink.getTarget().process(object, sink.getSinkInPort(), !this.hasSingleConsumer());
				}
			}
		}
	}
	
	
	@Override 
	public void transfer(T object, int sourceOutPort) {
		System.err.println("Transfer for "+sourceOutPort+" "+BrokerDictionary.getInstance().getReadTypeForPort(this.identifier,sourceOutPort));
		process_transfer(object, sourceOutPort);
	};
	
	@Override
	public void subscribeToSource(ISource<? extends T> source, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {				
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
	
	public void reorganizeTransactions(List<CycleSubscription> cycles){
		// change all to continuous 
		LoggerFactory.getLogger("BrokerPO - reorganize").debug("Setting transaction types");		
		BrokerDictionary.getInstance().resetAllReadingPorts(this.identifier, ReadTransaction.Continuous);
		BrokerDictionary.getInstance().resetAllWritingPorts(this.identifier, WriteTransaction.Continuous);
		for(CycleSubscription cycle : cycles){
			LoggerFactory.getLogger("BrokerPO - reorganize").debug(cycle.toString()+" - change transaction type to cyclic");
			BrokerDictionary.getInstance().setReadTypeForPort(this.identifier, cycle.getOutgoingPort(), ReadTransaction.Cyclic);
			BrokerDictionary.getInstance().setWriteTypeForPort(this.identifier, cycle.getIncomingPort(), WriteTransaction.Cyclic);
		}				
	}
	
	public void printSubs(){
		for(PhysicalSubscription<?> sub : this.subscriptions){
			System.out.println("SUBSC: "+sub);
		}
	}
}