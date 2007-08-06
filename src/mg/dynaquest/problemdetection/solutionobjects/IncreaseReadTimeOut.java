package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IncreaseReadTimeOut extends SolutionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4578589100882442765L;
	/**
	 * @uml.property  name="additionalTime"
	 */
	private int additionalTime;
	/**
	 * 
	 */
	public IncreaseReadTimeOut(int additionalTime) {
		super();
		this.additionalTime = additionalTime;
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.solutionobjects.SolutionObject#executeSolution()
	 */
	public int executeSolution() {
				
		return 0;
	}
	
	public String toString() {
		
		return "ReadTimeOut um " + additionalTime + " vergrößern.";
	
	}

}
