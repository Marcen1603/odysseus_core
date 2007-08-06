package mg.dynaquest.sourcedescription.sdf.predicate;

public class SDFOrOperator extends SDFLogicalOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -97278497055824968L;

	public SDFOrOperator(String URI) {
		super(URI);
	}

	public String toString() {
		return "OR";
	}

    /* (non-Javadoc)
     * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFLogicalOperator#evaluate(boolean, boolean)
     */
    public boolean evaluate(boolean left, boolean right) {
        return (left||right);
    }
}