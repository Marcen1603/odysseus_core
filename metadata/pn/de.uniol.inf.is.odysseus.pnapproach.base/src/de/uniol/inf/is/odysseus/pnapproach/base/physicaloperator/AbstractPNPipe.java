package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POPortEvent;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Andre Bolles, Jonas Jacobi
 */
public abstract class AbstractPNPipe<R extends IMetaAttributeContainer<? extends IPosNeg>, W extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPipe<R, W>{
		
	protected POEvent[] processInitNegEvent = null;
	protected POEvent[] processDoneNegEvent = null;
	protected POEvent pushInitNegEvent = new POEvent(this,
			POEventType.PushInitNeg);
	protected POEvent pushDoneNegEvent = new POEvent(this,
			POEventType.PushDoneNeg);
	
	public AbstractPNPipe(){};
	
	public AbstractPNPipe(AbstractPNPipe<R,W> pipe){
		super(pipe);
		initNegPOEvents(pipe.getInputPortCount());
	}
	
	
	@Override
	public void process(R object, int port, boolean exclusive) {
		// if (!isOpen()) System.err.println(this+" PROCESS BEFORE OPEN!!!");
		// evtl. spaeter wieder einbauen? Exception?
		if(object.getMetadata().getElementType() == ElementType.POSITIVE ){
			super.process(object, port, exclusive);
		}
		else{
			fire(processInitNegEvent[port]);
			process_next(object, port);
			fire(processDoneNegEvent[port]);
		}
	}
	
	
	@Override
	public void subscribeToSource(ISource<? extends R> source, int sinkPort, int sourcePort, SDFAttributeList schema) {
		super.subscribeToSource(source, sinkPort, sourcePort, schema);
		int portCount = delegateSink.getInputPortCount();
		initNegPOEvents(portCount);
	}

	private void initNegPOEvents(int portCount) {
		processInitNegEvent = new POEvent[portCount];
		processDoneNegEvent = new POEvent[portCount];
		for (int i = 0; i < portCount; ++i) {
			processInitNegEvent[i] = new POPortEvent(this,
					POEventType.ProcessInitNeg, i);
			processDoneNegEvent[i] = new POPortEvent(this,
					POEventType.ProcessDoneNeg, i);
		}
	}
	
	@Override
	final public void transfer(W object) {
		if(object.getMetadata().getElementType() == ElementType.POSITIVE ){
			super.transfer(object);
		}
		else{
			fire(this.pushInitNegEvent);
			process_transfer(object);
			fire(this.pushDoneNegEvent);
		}
	}
}
