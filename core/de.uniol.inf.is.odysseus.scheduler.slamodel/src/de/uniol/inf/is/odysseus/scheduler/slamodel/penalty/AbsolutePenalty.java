package de.uniol.inf.is.odysseus.scheduler.slamodel.penalty;

/**
 * This kind of penalty is defined by an absolute amount payable on violation of
 * a service level.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class AbsolutePenalty extends AbstractPenalty {

	/**
	 * the absolute cost for service level violation
	 */
	private int cost;

	/**
	 * creates a new absolute penalty object
	 * 
	 * @param cost
	 *            the absolute costs of teh penalty
	 */
	public AbsolutePenalty(int cost) {
		super();
		this.setCost(cost);
	}

	/**
	 * returns the absolute costs of the penalty
	 */
	@Override
	public int getCost() {
		return this.cost;
	}

	/**
	 * sets the new absolute costs of the penalty
	 * 
	 * @param cost
	 *            the new cost
	 */
	public void setCost(int cost) {
		if (cost < 0)
			throw new IllegalArgumentException("negative costs");
		this.cost = cost;
	}

}
