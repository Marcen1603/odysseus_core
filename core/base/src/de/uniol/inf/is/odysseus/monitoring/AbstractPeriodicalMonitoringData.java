package de.uniol.inf.is.odysseus.monitoring;

abstract public class AbstractPeriodicalMonitoringData<T> extends AbstractPublisher<T> implements IPeriodicalMonitoringData<T>{
	
	private IMonitoringDataProvider target;
	
	public AbstractPeriodicalMonitoringData(IMonitoringDataProvider target) {
		super();
		this.target = target;
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
}
