package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.physicaloperator.base.IObservablePhysicalOperator;

/**
 * Calculates the Productivity of an Operator by relating the count of the written
 * objects with the <b>sum</b> of all read objects
 * 
 * @author Marco Grawunder
 *
 */
public class Productivity extends Selectivity {

	public Productivity(IObservablePhysicalOperator po, int sourceCount) {
		super(po, sourceCount);	
	}

	@Override
	public Double getValue() {
		return getWriteCount()/getReadCountSum();
	}

	@Override
	public String getType() {
		return MonitoringDataTypes.PRODUCTIVITY.name;
	}

}
