package de.uniol.inf.is.odysseus.monitoring;

abstract public class AbstractPeriodicalMonitoringData<T> extends AbstractPublisher<T> implements IPeriodicalMonitoringData<T>{
	
	private IMonitoringDataProvider target;
	private String type;
	
	public AbstractPeriodicalMonitoringData(IMonitoringDataProvider target, String type) {
		super();
		this.target = target;
		this.type = type;
	}

	@Override
	public IMonitoringDataProvider getTarget() {
		return target;
	}
	
	@Override
	public abstract AbstractPeriodicalMonitoringData<T> clone();
	
	@Override
	public void cancelMonitoring() {
		synchronized (this.subscribers) {
			this.subscribers.clear();
		}
		MonitoringDataScheduler.getInstance().cancelPeriodicalMetadataItem(this);
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	final public String getType() {
		return type;
	}
}
