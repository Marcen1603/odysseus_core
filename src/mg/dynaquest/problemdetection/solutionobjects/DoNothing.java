package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DoNothing extends SolutionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -232193499688997105L;

	/**
	 * 
	 */
	public DoNothing() {
		super();
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.solutionobjects.SolutionObject#executeSolution()
	 */
	public int executeSolution() {
				
		return 0;
	}

	public String toString() {
		
		return "Do Nothing!";
			
	}
}
