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
		StaticValueMonitoringData<T> ret = null;
		try {
			ret = (StaticValueMonitoringData<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.target = this.target;
		ret.value = this.value;
		ret.type = this.type;
		return ret;
	}
	
	@Override
	public void reset() {
		// Value does not change, so not reset necessary!	
	}
	
}
