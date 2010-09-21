package de.uniol.inf.is.odysseus.monitoring;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.IEventListener;
import de.uniol.inf.is.odysseus.base.IEventType;

public interface IMonitoringDataProvider {
	public Collection<String> getProvidedMonitoringData();

	public boolean providesMonitoringData(String type);

	public <T> IMonitoringData<T> getMonitoringData(String type);

	public <T> IPeriodicalMonitoringData<T> getMonitoringData(String type,
			long period);

	public void addMonitoringData(String type, IMonitoringData<?> item);
	public void removeMonitoringData(String type);
}
