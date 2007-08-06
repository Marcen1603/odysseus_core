package mg.dynaquest.sourcedescription.sdf.query;

public class Weighted {

    /**
	 * @uml.property  name="weighting"
	 */
    private float weighting;


	public Weighted(float weighting) {
		this.weighting = weighting;
		if (weighting < 0 || weighting > 1) {
			this.weighting = -1;
		}
	}

    /**
     * 
     * @uml.property name="weighting"
     */
    public void setWeighting(float weighting) throws IllegalArgumentException {
        this.weighting = weighting;
        if (weighting < 0 || weighting > 1)
            throw new IllegalArgumentException(
                "Weighting must be between 0 and 1");
    }

    /**
     * 
     * @uml.property name="weighting"
     */
    public float getWeighting() {
        return weighting;
    }

}