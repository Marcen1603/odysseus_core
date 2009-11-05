package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

public class SDFOrOperator extends SDFLogicalOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -97278497055824968L;

	public SDFOrOperator(String URI) {
		super(URI);
	}

	@Override
	public String toString() {
		return "OR";
	}

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFLogicalOperator#evaluate(boolean, boolean)
     */
    @Override
	public boolean evaluate(boolean left, boolean right) {
        return (left||right);
    }
}