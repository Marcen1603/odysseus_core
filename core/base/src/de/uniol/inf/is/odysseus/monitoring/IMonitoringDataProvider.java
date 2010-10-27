package de.uniol.inf.is.odysseus.monitoring;

import java.util.Collection;

public interface IMonitoringDataProvider {
	public Collection<String> getProvidedMonitoringData();

	public boolean providesMonitoringData(String type);

	public <T> IMonitoringData<T> getMonitoringData(String type);

	public <T> IPeriodicalMonitoringData<T> getMonitoringData(String type,
			long period);
	public <T> IPeriodicalMonitoringData<T> getMonitoringData(
				IPeriodicalMonitoringData item, long period);


	public void addMonitoringData(String type, IMonitoringData<?> item);
	public void removeMonitoringData(String type);

}
