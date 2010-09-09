package de.uniol.inf.is.odysseus.monitoring;

import de.uniol.inf.is.odysseus.base.IClone;

public interface IMonitoringData<T> extends IClone{
	public String getType();

	public IMonitoringDataProvider getTarget();

	public T getValue();
	public void reset();	
	
	public void cancelMonitoring();
}
