package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.physicaloperator.base.IObservablePhysicalOperator;

/**
 * 
 * Calculates the Selectivity of an Operator by relating the count of the written
 * objects with the <b>product</b> of all read objects. Max Value is 1 (each read object
 * is participated in every produced object --> cartesian product)
 * 
 * @author Marco Grawunder
 *
 */

public class ClassicSelectivity extends Selectivity {

	public ClassicSelectivity(IObservablePhysicalOperator po, int inputPorts) {
		super(po, inputPorts);
	}

	public Double getValue() {
		return  getWriteCount()/ getReadCountProduct();
	}

	@Override
	public String getType() {
		return MonitoringDataTypes.SELECTIVITY.name;
	}

}
