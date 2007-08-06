package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SubstitutePO extends SolutionObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4304853742435109618L;
	/**
	 * @uml.property  name="internalPOName"
	 */
	String internalPOName;
	
	public SubstitutePO(String internalPOName) {
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
		
		return "Operator durch " + internalPOName + " ersetzen.";

	}

}
