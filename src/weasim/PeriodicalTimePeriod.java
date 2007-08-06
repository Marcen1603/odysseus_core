package weasim;

public class PeriodicalTimePeriod implements TimePeriod {

	private long period;

	public PeriodicalTimePeriod(long l) {
		this.period = l;
	}

	public long getNextWaitTime() {
		return period;
	}

}
