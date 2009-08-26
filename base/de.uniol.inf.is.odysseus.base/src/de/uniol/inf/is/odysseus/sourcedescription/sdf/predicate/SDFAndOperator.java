package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

public class SDFAndOperator extends SDFLogicalOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8991773931959700203L;

	public SDFAndOperator(String URI) {
		super(URI);
	}

	public String toString() {
		return "AND";
	}

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFLogicalOperator#evaluate(boolean, boolean)
     */
    public boolean evaluate(boolean left, boolean right) {
        return left && right;
    }
}