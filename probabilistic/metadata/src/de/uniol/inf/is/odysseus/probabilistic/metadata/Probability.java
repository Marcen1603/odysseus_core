package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.probabilistic.math.PBox;
/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Probability implements IProbability {
	private final Map<Integer, PBox> pBoxes = new HashMap<Integer, PBox>();

	/**
	 * 
	 */
	private static final long serialVersionUID = -147594856639774242L;

	public Probability() {

	}
	public Probability(IProbability probability) {

	}
	@Override
	public String csvToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String csvToString(boolean withMetada) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSVHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(IProbability other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IProbability clone() {
		return new Probability(this);
	}

}
