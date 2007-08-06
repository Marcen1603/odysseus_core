package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SwitchCollectorOP extends SolutionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5129214389156257661L;

	/**
	 * 
	 */
	public SwitchCollectorOP() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.solutionobjects.SolutionObject#executeSolution()
	 */
	public int executeSolution() {
		
		System.out.println(this + "\nCollector-Operator umschalten.");
		
		return 0;
	}
	
	public String toString() {
		
		return "Collector-Operator umschalten.";

	}

}
