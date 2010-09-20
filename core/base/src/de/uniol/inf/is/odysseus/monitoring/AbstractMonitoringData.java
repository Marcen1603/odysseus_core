package de.uniol.inf.is.odysseus.monitoring;

public abstract class AbstractMonitoringData<T> 
		implements IMonitoringData<T> {

	private IMonitoringDataProvider target;
	
	public AbstractMonitoringData(){
		super();
		this.target = null;
	}
	
	public AbstractMonitoringData(IMonitoringDataProvider target) {
		super();
		this.target = target;
	}

	public AbstractMonitoringData(AbstractMonitoringData<T> other) {
		this.target = other.target;
	}

	public IMonitoringDataProvider getTarget() {
		return target;
	}
	
	public void setTarget(IMonitoringDataProvider target) {
		this.target = target;
	}
	
	@Override
	public abstract AbstractMonitoringData<T> clone() ;

	@Override
	public void cancelMonitoring() {
	}
	
	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}

}