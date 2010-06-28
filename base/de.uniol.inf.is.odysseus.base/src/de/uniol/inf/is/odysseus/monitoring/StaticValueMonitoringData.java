package de.uniol.inf.is.odysseus.monitoring;

/**
 * @author Jonas Jacobi
 */
public class StaticValueMonitoringData<T> implements IMonitoringData<T> {

	private IMonitoringDataProvider target;
	private T value;
	private String type;

	public StaticValueMonitoringData(IMonitoringDataProvider target, String type, T value) {
		this.target = target;
		this.value = value;
		this.type = type;
	}
	public StaticValueMonitoringData(
			StaticValueMonitoringData<T> staticValueMonitoringData) {
		this.target = staticValueMonitoringData.target;
		this.value = staticValueMonitoringData.value;
		this.type = staticValueMonitoringData.type;
	}
	@Override
	public IMonitoringDataProvider getTarget() {
		return this.target;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@Override
	public StaticValueMonitoringData<T> clone() {
		return new StaticValueMonitoringData(this);
	}
	
	@Override
	public void reset() {
		// Value does not change, so not reset necessary!	
	}
	
	@Override
	public void cancelMonitoring() {
	}
	
}
