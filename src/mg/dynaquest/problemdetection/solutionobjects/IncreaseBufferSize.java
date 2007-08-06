package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IncreaseBufferSize extends SolutionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5590233349314809626L;
	/**
	 * @uml.property  name="additionalSize"
	 */
	private int additionalSize;

	/**
	 * 
	 */
	public IncreaseBufferSize(int additionalSize) {
		super();
		this.additionalSize = additionalSize;
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.solutionobjects.SolutionObject#executeSolution()
	 */
	public int executeSolution() {
				
		return 0;
	}
	
	public String toString() {
		
		return "Vergrößern des Pufferspeichers um " + additionalSize;
			
	}

}
