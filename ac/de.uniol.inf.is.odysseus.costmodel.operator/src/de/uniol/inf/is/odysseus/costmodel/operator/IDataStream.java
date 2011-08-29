package de.uniol.inf.is.odysseus.costmodel.operator;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface IDataStream {

	public IPhysicalOperator getOperator();
	
	public double getDataRate();
	public double getIntervalLength();
	
}
