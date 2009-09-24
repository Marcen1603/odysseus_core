package de.uniol.inf.is.odysseus.monitoring;

public interface IMonitoringDataFactory {
	public <T> IMonitoringData<T> createMetadataItem(IMonitoringDataProvider provider);
}
