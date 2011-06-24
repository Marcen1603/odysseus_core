package de.uniol.inf.is.odysseus.scheduler.slamodel.penalty;

/**
 * This kind of penalty is defined by a certain percentage of the price payed
 * for the service.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class PercentagePenalty extends AbstractPenalty {

	/**
	 * the percentage that must be payed if the service level is violated
	 */
	private float percentage;

	/**
	 * creates a new percentage penalty
	 * 
	 * @param percentage
	 *            the percentage
	 */
	public PercentagePenalty(float percentage) {
		this.setPercentage(percentage);
	}

	/**
	 * returns the cost of the penalty, defined as percentage of the price for
	 * the service
	 */
	@Override
	public int getCost() {
		return (int) (this.getServiceLevel().getSla().getPrice() * percentage);
	}

	/**
	 * @return the percentage
	 */
	public float getPercentage() {
		return percentage;
	}

	/**
	 * sets a new percentage
	 * @param percentage the new percentage
	 */
	public void setPercentage(float percentage) {
		if (percentage < 0.0f)
			throw new IllegalArgumentException("negative percentage");
		this.percentage = percentage;
	}

}
