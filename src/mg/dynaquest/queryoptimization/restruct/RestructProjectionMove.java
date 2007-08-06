/*
 * Created on 14.12.2004
 *
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Olaf Twesten  KLASSE NICHT VERWENDEN, STATTDESSEN DIE ANDEREN SPEZIALISIERTEN  KLASSEN DIESES PAKETES BENUTZEN!
 */
public class RestructProjectionMove extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private ProjectPO poToHandle = null;
	/**
	 * @uml.property  name="preChildPos"
	 */
	private int preChildPos = 0;
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
		// Auf Effizienzgr�nden merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach ProjectPO im Plan
		ArrayList<ProjectPO> allProjects = new ArrayList<ProjectPO>();
		findProjects(logicalplan, allProjects);
		for (int i = 0; i < allProjects.size(); i++) {
			ProjectPO ppo = allProjects.get(i);
			AlgebraPO pre = ppo.getInputPO();
			// Ein Project kann nicht vor einen PhysicalAccessPO geschoben
			// werden. Vorg�nger darf nicht auch ProjectPO sein. Bei
			// Vorg�nger UnionPO oder DifferencePO eigene Regel vorhanden.
			if (!(pre instanceof SchemaTransformationAccessPO) && !(pre instanceof ProjectPO)
					&& !(pre instanceof UnionPO)
					&& !(pre instanceof DifferencePO)) {
				for (int j = 0; j < pre.getNumberOfInputs(); j++) {
					SDFAttributeList attribs = pre.getInputPO(j)
							.getOutElements();
					SDFAttributeList reqAttribs = ppo.getProjectAttributes();
					SDFAttributeList preAttribs = pre.getOutElements();
					// Die zu verschiebende Projektion muss die n�tigen
					// Attribute f�r den "noch"-Kindknoten liefern.
					if (!SDFAttributeList.subset(preAttribs, reqAttribs)) {
						continue;
					}
					// Testen, ob bei einem der Vorg�nger des Vorg�ngers alle
					// f�r die Projektionsbedingung notwendigen Attribute
					// vorhanden sind. Merken, welcher der evtl. 2 Kindknoten der
					// passende ist.
					if (SDFAttributeList.subset(reqAttribs, attribs)) {
						poToHandle = ppo;
						preChildPos = j;
						return true;
					}
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
		// Falls irgendwas schief gegangen ist, gebe den unver�nderten Plan
		// zur�ck
		if (poToHandle == null)
			return logicalplan;

		// Den Vaterknoten von poToHandle ermitteln
		AlgebraPO father = determineFatherNode(logicalplan, poToHandle);

		// Dann den Nachfolgerknoten von poToHandle als neuen Eingabeknoten
		// f�r den Vater festlegen. Merken welcher Kindknoten poToHandle von
		// father ist.
		if (father.getInputPO(0).equals(poToHandle)) {
			father.setInputPO(0, poToHandle.getInputPO());
			ppoChildPos = 0;
		}
		else {
			father.setInputPO(1, poToHandle.getInputPO());
			ppoChildPos = 1;
		}
		
		// Eingabe von poToHandle auf die unter preChildPos angegebene Position
		// des Vorg�ngers des Vorg�ngers setzen.
		// Vorg�nger von father bzw. fr�herer Vorg�nger von poToHandle an dem
		// korrekten Eingabeport poToHandle als Eingabe zuweisen.
		poToHandle.setInputPO(poToHandle.getInputPO(0).getInputPO(preChildPos));
		father.getInputPO(ppoChildPos).setInputPO(preChildPos, poToHandle);
		
		// Ausgabeelemente des fr�heren Vorg�ngers des ProjectPO auf die
		// Projektionsattribute setzen.
		father.getInputPO(ppoChildPos).setOutElements(poToHandle.getProjectAttributes());

		return logicalplan;
	}
}