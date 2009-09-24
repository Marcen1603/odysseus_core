package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

public class ElementsRead extends AbstractMonitoringData<Long> implements POEventListener{

	private long readCount;
	
	public ElementsRead(IPhysicalOperator target) {
		super(target);
		reset();
		target.subscribe(this, POEventType.ProcessDone);
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
	
	public void poEventOccured(POEvent poEvent) {
		this.readCount++;
	}

}
