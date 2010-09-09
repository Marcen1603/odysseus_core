package de.uniol.inf.is.odysseus.monitoring;

public abstract class AbstractMonitoringData<T> 
		implements IMonitoringData<T> {

	private IMonitoringDataProvider target;
	
	public AbstractMonitoringData(IMonitoringDataProvider target) {
		super();
		this.target = target;
	}

	public AbstractMonitoringData(AbstractMonitoringData other) {
		this.target = other.target;
	}

	public IMonitoringDataProvider getTarget() {
		return target;
	}
	
	@Override
	public abstract AbstractMonitoringData<T> clone() ;

	@Override
	public void cancelMonitoring() {
	}
}