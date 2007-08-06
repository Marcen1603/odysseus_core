package mg.dynaquest.problemdetection.solutionobjects;

import java.io.Serializable;

/**
 * @author Joerg Baeumer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class SolutionObject implements Serializable {

	/**
	 * 
	 */
	public SolutionObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public abstract int executeSolution();

}
