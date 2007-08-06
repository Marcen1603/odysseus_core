/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Olaf Twesten
 */
public class SwitchSelectionProjection extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private ProjectPO poToHandle = null;

	/*
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#test(mg.dynaquest.queryexecution.po.base.NAryPlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Auf Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach SelectPO im Plan
		ArrayList<ProjectPO> allProjects = new ArrayList<ProjectPO>();
		findProjects(logicalplan, allProjects);
		for (int i = 0; i < allProjects.size(); i++) {
			ProjectPO ppo = allProjects.get(i);
			AlgebraPO pre =  ppo.getInputPO();
			// Vorgänger muss SelectPO sein. Vorgänger dieses SelectPO darf
			// kein ProjectPO sein.
			if ((pre instanceof SelectPO)
					&& !(pre.getInputPO(0) instanceof ProjectPO)) {
				poToHandle = ppo;
				return true;
			}
		}
		return false;
	}

	/*
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#process(mg.dynaquest.queryexecution.po.base.NAryPlanOperator)
	 */
	public SupportsCloneMe process(AlgebraPO logicalplan) {
		// Falls irgendwas schief gegangen ist, gebe den unveränderten Plan
		// zurück
		if (poToHandle == null)
			return logicalplan;

		// Neuen ProjectPO erstellen. Ausgabeattribute und Projektionsattribut
		// als Vereinigung von ProjectPO und dem SelectPO Sohn einfügen.
		// Neuen ProjectPO als Sohn des SelectPO einfügen.
		ProjectPO poToHandle2 = new ProjectPO();
		poToHandle2.setPOName(poToHandle.getPOName() + " Neu");

		SDFAttributeList newOut = SDFAttributeList.union(poToHandle
				.getProjectAttributes(), poToHandle.getInputPO().getInputPO(0)
				.getOutElements());
		poToHandle2.setOutElements(newOut);

		SDFAttributeList newAttribs = SDFAttributeList.union(poToHandle
				.getProjectAttributes(), poToHandle.getInputPO().getPredicate()
				.getAllAttributes());
		poToHandle2.setProjectAttributes(newAttribs);

		poToHandle2.setInputPO(poToHandle.getInputPO().getInputPO(0));
		poToHandle.getInputPO().setInputPO(0, poToHandle2);

		return logicalplan;
	}
}
