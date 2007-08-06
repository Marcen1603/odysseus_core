/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;

/**
 * @author  Olaf Twesten
 */
public class RestructProjectionGroup extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private ProjectPO poToHandle = null;
	/**
	 * @uml.property  name="poToRemove"
	 * @uml.associationEnd  
	 */
	private AlgebraPO poToRemove = null;
	
	/* (Kein Javadoc)
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#test(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Auf Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach ProjectPO im Plan
		ArrayList<ProjectPO> allProjects = new ArrayList<ProjectPO>();
		findProjects(logicalplan, allProjects);
		for (int i = 0; i < allProjects.size(); i++) {
			ProjectPO ppo = allProjects.get(i);
			AlgebraPO pre = (AlgebraPO) ppo.getInputPO();
			// Vorgänger muss ProjectPO sein.
			if (pre instanceof ProjectPO) {
				poToHandle = ppo;
				poToRemove = pre;
				return true;
			}
		}
		return false;
	}

	/* (Kein Javadoc)
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#process(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public SupportsCloneMe process(AlgebraPO logicalplan) {
		// Falls irgendwas schief gegangen ist, gebe den unveränderten Plan
		// zurück
		if (poToHandle == null)
			return logicalplan;

		// Den Nachfolgerknoten von poToRemove als neuen Eingabeknoten
		// für poToHandle festlegen
		poToHandle.setInputPO(poToRemove.getInputPO(0));

		return logicalplan;
	}
}
