package de.uniol.inf.is.odysseus.scheduler.slamodel.penalty;


public class PercentageCost extends AbstractPenalty {
	
	private float percentage;
	
	public PercentageCost(float percentage) {
		this.setPercentage(percentage);
	}

	@Override
	public int getCost() {
		return (int) (this.getServiceLevel().getSla().getPrice() * percentage);
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		if (percentage < 0.0f)
			throw new IllegalArgumentException("negative percentage");
		this.percentage = percentage;
	}
	
}
