package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.transaction.CycleSubscription;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPipe<T, T> {
	private Logger logger = LoggerFactory.getLogger("BrokerPO");
	private String identifier;	
	private DefaultTISweepArea<T> sweepArea = new DefaultTISweepArea<T>();
	private PriorityQueue<TransactionTS> timestampList = new PriorityQueue<TransactionTS>();
	private List<CycleSubscription> cycles = new ArrayList<CycleSubscription>();
	
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
		//if(type==WriteTransaction.Timestamp){		
		if(port==4){
			PointInTime time = PointInTime.currentPointInTime();
			TransactionTS trans = new TransactionTS(getOutgoingPortForIncoming(port-2), time); // nromal: port-1
			timestampList.offer(trans);			
		}else{							
			sweepArea.purgeElements(object, Order.LeftRight);
			sweepArea.insert(object);
		}
		//System.out.println(sweepArea.getSweepAreaAsString(PointInTime.currentPointInTime()));
		int to = BrokerDictionary.getInstance().getAllReadingPorts(identifier).length;
		T nextObject = sweepArea.peek();
		int nextPort = -1;
		if(!timestampList.isEmpty()){				
			nextPort = timestampList.poll().getOutgoingPort();
		}
		for(int i=0;i<to;i++){
			if(i==nextPort||BrokerDictionary.getInstance().getReadTypeForPort(identifier, i)==ReadTransaction.Continuous)
			transfer(nextObject, i);		
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
		ReadTransaction type = BrokerDictionary.getInstance().getReadTypeForPort(this.identifier,sourceOutPort);
		System.err.println("Transfer to "+sourceOutPort+" "+type+": "+object.toString()+"  ("+this+")");
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
		this.cycles = cycles;
	}
	
	public int getOutgoingPortForIncoming(int income){
		for(CycleSubscription sub: this.cycles){
			if(sub.getIncomingPort()==income){
				return sub.getOutgoingPort();
			}
		}
		logger.warn("There is no cycle with incoming port "+income);
		return 0;
	}
}