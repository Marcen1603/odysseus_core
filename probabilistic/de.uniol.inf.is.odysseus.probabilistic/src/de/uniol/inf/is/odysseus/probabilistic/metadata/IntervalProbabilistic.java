package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class IntervalProbabilistic extends TimeInterval implements
		IProbabilistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9030157268224460919L;
	private final IProbabilistic probabilistic;

	public IntervalProbabilistic() {
		super();
		this.probabilistic = new Probabilistic();
	}

	public IntervalProbabilistic(IntervalProbabilistic intervalProbabilistic) {
		super(intervalProbabilistic);
		this.probabilistic = intervalProbabilistic.probabilistic.clone();
	}

	@Override
	public double getProbability(int pos) {
		return this.probabilistic.getProbability(pos);
	}

	@Override
	public void setProbability(int pos, double value) {
		this.probabilistic.setProbability(pos, value);

	}

	@Override
	public double[] getProbabilities() {
		return this.probabilistic.getProbabilities();
	}

	@Override
	public void setProbabilities(double[] values) {
		this.probabilistic.setProbabilities(values);
	}

	@Override
	public IntervalProbabilistic clone() {
		return new IntervalProbabilistic(this);
	}

	@Override
	public String toString() {
		return "( i= " + super.toString() + " | " + " p=" + this.probabilistic
				+ ")";
	}

	@Override
	public String csvToString() {
		return super.csvToString() + ";" + this.probabilistic.csvToString();
	}

	@Override
	public String getCSVHeader() {
		return super.getCSVHeader() + ";" + this.probabilistic.getCSVHeader();
	}
}
