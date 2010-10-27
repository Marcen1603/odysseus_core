package de.uniol.inf.is.odysseus.monitoring;

public abstract class AbstractMonitoringData<T> 
		implements IMonitoringData<T> {

	private IMonitoringDataProvider target;
	String type = null;
	
	public AbstractMonitoringData(String type){
		super();
		this.target = null;
		setType(type);
	}
	
	public AbstractMonitoringData(IMonitoringDataProvider target, String type) {
		super();
		this.target = target;
		this.type = type;
	}

	public AbstractMonitoringData(AbstractMonitoringData<T> other) {
		this.target = other.target;
		this.type = other.type;
	}

	@Override
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
	final public String getType() {
		return type;
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
}