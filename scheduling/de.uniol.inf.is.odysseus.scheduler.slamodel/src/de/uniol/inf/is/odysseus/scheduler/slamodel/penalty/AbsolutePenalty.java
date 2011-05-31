package de.uniol.inf.is.odysseus.scheduler.slamodel.penalty;

public class AbsolutePenalty extends AbstractPenalty {

	private int cost;

	public AbsolutePenalty(int cost) {
		super();
		this.setCost(cost);
	}

	@Override
	public int getCost() {
		return this.cost;
	}

	public void setCost(int cost) {
		if (cost < 0)
			throw new IllegalArgumentException("negative costs");
		this.cost = cost;
	}

}
