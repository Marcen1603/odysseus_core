package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IncreaseOperatorMem extends SolutionObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8921261071958526979L;
	/**
	 * @uml.property  name="additionalMem"
	 */
	private int additionalMem = 0;
	
	public IncreaseOperatorMem(int additionalMem) {
		super();
		this.additionalMem = additionalMem;	
		
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.solutionobjects.SolutionObject#executeSolution()
	 */
	public int executeSolution() {
				
		return 0;
	}

	public String toString() {
		
		return "Operatorenspeicher um " + additionalMem + " vergrößern";
			
	}
}
