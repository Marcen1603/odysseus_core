package de.uniol.inf.is.odysseus.monitoring;

public abstract class AbstractMonitoringData<T> 
		implements IMonitoringData<T> {

	private IMonitoringDataProvider target;
	
	public AbstractMonitoringData(IMonitoringDataProvider target) {
		super();
		this.target = target;
	}

	public IMonitoringDataProvider getTarget() {
		return target;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AbstractMonitoringData<T> clone() {
		AbstractMonitoringData<T> ret = null;
		try {
			ret = (AbstractMonitoringData<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.target = this.target;
		return ret;
	}
	
}