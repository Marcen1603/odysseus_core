package mg.dynaquest.sourcedescription.sdf.predicate;

import mg.dynaquest.sourcedescription.sdf.SDFElement;

public abstract class SDFLogicalOperator extends SDFElement {
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