package mg.dynaquest.problemdetection.solutionobjects;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IncreaseProducerMemSize extends SolutionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8818928530757685085L;
	/**
	 * @uml.property  name="additionalMem"
	 */
	private int additionalMem = 0;
	
	public IncreaseProducerMemSize(int additionalMem) {
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
		
		return "Produzentenspeicher um " + additionalMem + " vergrößern";
		
	}

}
