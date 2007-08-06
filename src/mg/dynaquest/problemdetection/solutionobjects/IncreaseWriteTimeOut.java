package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IncreaseWriteTimeOut extends SolutionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8097411157548718011L;
	/**
	 * @uml.property  name="additionalTime"
	 */
	int additionalTime;
	
	public IncreaseWriteTimeOut(int additionalTime) {
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
		
		return "WriteTimeOut um " + additionalTime + " vergrößern." ;
	}

}
