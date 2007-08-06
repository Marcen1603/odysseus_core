package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SubstituteProducerPO extends SolutionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2956632215727574424L;
	/**
	 * @uml.property  name="internalPOName"
	 */
	private String internalPOName; 
	/**
	 * 
	 */
	public SubstituteProducerPO(String internalPOName) {
		super();
		this.internalPOName = internalPOName;
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.solutionobjects.SolutionObject#executeSolution()
	 */
	public int executeSolution() {
				
		return 0;
	}

	public String toString() {
		
		return "Produzenten durch " + internalPOName + " ersetzen.";

	}
}
