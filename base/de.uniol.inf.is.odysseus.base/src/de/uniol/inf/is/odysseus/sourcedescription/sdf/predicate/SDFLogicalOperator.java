package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public abstract class SDFLogicalOperator extends SDFElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2268751063961164179L;

	public SDFLogicalOperator(String URI) {
		super(URI);
	}

    /**
     * Wertet des Audruck mit Hilfe des eigenen logischen Operators
     * aus
     * @param left
     * @param right
     * @return die Wahrheitswert der Auswertung
     */
	abstract public boolean evaluate(boolean left, boolean right);
}