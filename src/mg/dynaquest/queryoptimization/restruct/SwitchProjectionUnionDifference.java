/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Olaf Twesten
 */
public class SwitchProjectionUnionDifference extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private ProjectPO poToHandle = null;

	/**
	 * @uml.property  name="ppoChildPos"
	 */
	private int ppoChildPos = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.CARule#test(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Aus Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach ProjectPO im Plan
		ArrayList<ProjectPO> allProjects = new ArrayList<ProjectPO>();
		findProjects(logicalplan, allProjects);
		for (int i = 0; i < allProjects.size(); i++) {
			ProjectPO ppo = allProjects.get(i);
			AlgebraPO pre = ppo.getInputPO();
			// Vorgänger muss DifferencePO oder UnionPO sein.
			if ((pre instanceof DifferencePO) || (pre instanceof UnionPO)) {
				SDFAttributeList attribsLeft = pre.getInputPO(0)
						.getOutElements();
				SDFAttributeList attribsRight = pre.getInputPO(1)
						.getOutElements();
				SDFAttributeList reqAttribs = ppo.getProjectAttributes();
				// Testen, ob bei einem der Vorgänger des Vorgängers alle
				// für die Projektionsbedingung notwendigen Attribute
				// vorhanden sind.
				if (SDFAttributeList.subset(reqAttribs, attribsLeft)
						&& SDFAttributeList.subset(reqAttribs, attribsRight)) {
					poToHandle = ppo;
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.CARule#process(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public SupportsCloneMe process(AlgebraPO logicalplan) {
		// Falls irgendwas schief gegangen ist, gebe den unveränderten Plan
		// zurück
		if (poToHandle == null)
			return logicalplan;

		// Den Vaterknoten von poToHandle ermitteln
		AlgebraPO father = determineFatherNode(logicalplan, poToHandle);

		// Dann den Nachfolgerknoten von poToHandle als neuen Eingabeknoten
		// für den Vater festlegen. Merken welcher Kindknoten poToHandle von
		// father ist.
		if (father.getInputPO(0).equals(poToHandle)) {
			father.setInputPO(0, poToHandle.getInputPO());
			ppoChildPos = 0;
		} else {
			father.setInputPO(1, poToHandle.getInputPO());
			ppoChildPos = 1;
		}

		// Neuen ProjectPO erstellen.
		// Eingabe von poToHandle auf die Vorgänger des Vorgängers setzen.
		// Vorgänger von father bzw. früherer Vorgänger von poToHandle (union
		// oder difference)an dem korrekten Eingabeport poToHandle und
		// poToHandle2 als Eingabe zuweisen.
		ProjectPO poToHandle2 = new ProjectPO();
		poToHandle2.setPOName(poToHandle.getPOName() + " Kopie");
		poToHandle2.setOutElements(poToHandle.getOutElements());
		poToHandle2.setProjectAttributes(poToHandle.getProjectAttributes());

		poToHandle2.setInputPO(poToHandle.getInputPO(0).getInputPO(1));
		poToHandle.setInputPO(poToHandle.getInputPO(0).getInputPO(0));

		father.getInputPO(ppoChildPos).setInputPO(0, poToHandle);
		father.getInputPO(ppoChildPos).setInputPO(1, poToHandle2);

		// Ausgabelemente des früheren Vorgängers des ProjectPO auf die
		// Projektionsattribute setzen.
		father.getInputPO(ppoChildPos).setOutElements(poToHandle.getProjectAttributes());
		return logicalplan;
	}
}
