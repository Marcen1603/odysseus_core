package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POPortEvent;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

public abstract class AbstractPNPipe<R extends IMetaAttribute<? extends IPosNeg>, W extends IMetaAttribute<? extends IPosNeg>> extends AbstractPipe<R, W>{
		
	protected POEvent[] processInitNegEvent = null;
	protected POEvent[] processDoneNegEvent = null;
	protected POEvent pushInitNegEvent = new POEvent(this,
			POEventType.PushInitNeg);
	protected POEvent pushDoneNegEvent = new POEvent(this,
			POEventType.PushDoneNeg);
	
	public void process(R object, int port, boolean isReadOnly) {
		// if (!isOpen()) System.err.println(this+" PROCESS BEFORE OPEN!!!");
		// evtl. spaeter wieder einbauen? Exception?
		if(object.getMetadata().getElementType() == ElementType.POSITIVE ){
			super.process(object, port, isReadOnly);
		}
		else{
			fire(processInitNegEvent[port]);
			process_next(object, port, isReadOnly);
			fire(processDoneNegEvent[port]);
		}
	}
	
	public void setNoOfInputPort(int ports) {
		if (ports > noInputPorts) {
			this.noInputPorts = ports;
			processInitEvent = new POEvent[ports];
			processDoneEvent = new POEvent[ports];
			processInitNegEvent = new POEvent[ports];
			processDoneNegEvent = new POEvent[ports];
			for (int i = 0; i < ports; i++) {
				processInitEvent[i] = new POPortEvent(this,
						POEventType.ProcessInit, i);
				processDoneEvent[i] = new POPortEvent(this,
						POEventType.ProcessDone, i);
				processInitNegEvent[i] = new POPortEvent(this,
						POEventType.ProcessInitNeg, i);
				processDoneNegEvent[i] = new POPortEvent(this,
						POEventType.ProcessDoneNeg, i);
			}
		}
	}
	
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
