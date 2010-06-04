package de.uniol.inf.is.odysseus.monitoring;

abstract public class AbstractPeriodicalMonitoringData<T> extends AbstractPublisher<T> implements IPeriodicalMonitoringData<T>{
	
	private IMonitoringDataProvider target;
	
	public AbstractPeriodicalMonitoringData(IMonitoringDataProvider target) {
		super();
		this.target = target;
	}

	public IMonitoringDataProvider getTarget() {
		return target;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AbstractPeriodicalMonitoringData<T> clone() {
		AbstractPeriodicalMonitoringData<T> ret = null;
		try {
			ret = (AbstractPeriodicalMonitoringData<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.target = this.target;
		return ret;
	}
	
	public void cancelMonitoring() {
		synchronized (this.subscribers) {
			this.subscribers.clear();
		}
		MonitoringDataScheduler.getInstance().cancelPeriodicalMetadataItem(this);
	}
}
