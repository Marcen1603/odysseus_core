package de.uniol.inf.is.odysseus.loadshedding.monitoring;

import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;

public class AvgInputRate extends AbstractMonitoringData<Double> implements
		POEventListener {

	public static final double UNKOWN = -1;

	public AvgInputRate(IMonitoringDataProvider target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	private int sources = 0;
	private int read = 0;

	public void addSource(IPhysicalOperator source) {
		sources++;
	}

	public void removeSource(IPhysicalOperator source) {
		sources--;
	}

	@Override
	public void poEventOccured(POEvent poEvent) {
		read++;

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return MonitoringDataTypes.ELEMENTS_READ.name;
	}

	@Override
	public Double getValue() {
		if (sources == 0) {
			return UNKOWN;
		} else {
			return new Double(read / sources);
		}
	}

	@Override
	public void reset() {
		read = 0;
		sources = 0;

	}

}
