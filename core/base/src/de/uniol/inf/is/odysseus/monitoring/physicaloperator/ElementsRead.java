package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.base.IEvent;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

public class ElementsRead extends AbstractMonitoringData<Long> implements IPOEventListener{

	private long readCount;
	
	public ElementsRead(IPhysicalOperator target) {
		super(target);
		reset();
		target.subscribe(this, POEventType.ProcessDone);
	}

	public ElementsRead(ElementsRead elementsRead) {
		super(elementsRead);
		this.readCount = elementsRead.readCount;
	}

	public void reset() {
		this.readCount = 0;
	}

	@Override
	public String getType() {
		return MonitoringDataTypes.ELEMENTS_READ.name;
	}

	@Override
	public Long getValue() {
		return readCount;
	}
	
	public void eventOccured(IEvent<?,?> poEvent) {
		this.readCount++;
	}

	@Override
	public ElementsRead clone() {
		return new ElementsRead(this);
	}

}
