package mg.dynaquest.gui.composer;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:12 $ Version:
 * $Revision: 1.4 $ Log: $Log: JPlanOperatorModelInterface.java,v $
 * $Revision: 1.4 $ Log: Revision 1.4  2004/09/16 08:57:12  grawund
 * $Revision: 1.4 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.4 $ Log: Log:
 * Revision 1.3 2004/09/16 08:53:53 grawund Log: *** empty log message *** Log:
 * Log: Revision 1.2 2002/01/31 16:14:51 grawund Log:
 * Versionsinformationskopfzeilen eingefuegt Log:
 */

import javax.swing.event.*;

/**
 * @author  Marco Grawunder
 */
public interface JPlanOperatorModelInterface {
	/**
	 * @return
	 * @uml.property  name="pOName"
	 */
	public String getPOName();

	/**
	 * @param name
	 * @uml.property  name="pOName"
	 */
	public void setPOName(String name);

	public int getNumberOfInputs();

	public void addChangeListener(ChangeListener l);

	public void removeChangeListener(ChangeListener l);
}