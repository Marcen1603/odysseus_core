package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.probabilistic.math.PBox;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Probabilistic implements IProbabilistic {
	private final Map<Integer, PBox> pBoxes = new HashMap<Integer, PBox>();

	/**
	 * 
	 */
	private static final long serialVersionUID = -147594856639774242L;
	private double[] probabilities;

	public Probabilistic() {

	}

	public Probabilistic(Probabilistic probability) {
		this.probabilities = probability.probabilities.clone();

	}

	@Override
	public double getProbability(int pos) {
		// TODO Auto-generated method stub
		return this.probabilities[pos];
	}

	@Override
	public void setProbability(int pos, double value) {
		this.probabilities[pos] = value;
	}

	@Override
	public String csvToString() {
		return "" + this.probabilities;
	}

	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}

	@Override
	public String getCSVHeader() {
		return "probability";
	}

	@Override
	public IProbabilistic clone() {
		return new Probabilistic(this);
	}

}
