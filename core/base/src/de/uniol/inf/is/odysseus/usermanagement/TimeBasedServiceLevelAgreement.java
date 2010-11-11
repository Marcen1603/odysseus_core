package de.uniol.inf.is.odysseus.usermanagement;

public class TimeBasedServiceLevelAgreement extends ServiceLevelAgreement implements ITimeBasedServiceLevelAgreement{

	private static final long serialVersionUID = 7881811060507272864L;
	private long timeperiod;

	/**
	 * 
	 * @param name
	 * @param timeperiod in milliseconds
	 */
	public TimeBasedServiceLevelAgreement(String name, long timeperiod) {
		super(name);
		this.timeperiod = timeperiod;
	}
	
	@Override
	public long getTimeperiod() {
		return timeperiod;
	}
	
}
