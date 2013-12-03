package de.uniol.inf.is.odysseus.hmm;

import java.util.ArrayList;

/**
 * @author Michael Möbes
 * @author Christian Pieper
 * 
 *         Contains all HmmAlphas for each Gesture for a certain set timestamp
 * 
 */
public class HmmAlphaGroup {
	// Attributes
	private ArrayList<HmmAlphas> alphas;

	// Constructors
	public HmmAlphaGroup() {
		alphas = new ArrayList<HmmAlphas>();
	}

	// Methods
	public void addRow(HmmAlphas pAlphaRow) {
		alphas.add(pAlphaRow);
	}

	public ArrayList<HmmAlphas> getAlphas() {
		return alphas;
	}

	public void setAlphas(ArrayList<HmmAlphas> alphas) {
		this.alphas = alphas;
	}

}
