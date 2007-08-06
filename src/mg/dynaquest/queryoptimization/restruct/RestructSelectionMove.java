/*
 * Created on 14.12.2004
 *
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Marco Grawunder  KLASSE NICHT VERWENDEN, STATTDESSEN DIE ANDEREN SPEZIALISIERTEN  KLASSEN DIESES PAKETES BENUTZEN!
 */
public class RestructSelectionMove extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private SelectPO poToHandle = null;
	/**
	 * @uml.property  name="preChildPos"
	 */
	private int preChildPos = 0;
	/**
	 * @uml.property  name="spoChildPos"
	 */
	private int spoChildPos = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.CARule#test(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Aus Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach SelectPO im Plan
		ArrayList<SelectPO> allSelects = new ArrayList<SelectPO>();
		findSelects(logicalplan, allSelects);
		for (int i = 0; i < allSelects.size(); i++) {
			SelectPO spo = allSelects.get(i);
			AlgebraPO pre = spo.getInputPO();
			// Ein Select kann nicht vor einen PhysicalAccessPO geschoben
			// werden. Vorgänger darf nicht auch SelectPO sein. Bei
			// Vorgänger UnionPO oder DifferencePO eigene Regel vorhanden.
			if (!(pre instanceof SchemaTransformationAccessPO) && !(pre instanceof SelectPO)
					&& !(pre instanceof UnionPO)
					&& !(pre instanceof DifferencePO)) {
				for (int j = 0; j < pre.getNumberOfInputs(); j++) {
					SDFAttributeList attribs = pre.getInputPO(j)
							.getOutElements();
					SDFAttributeList reqAttribs = spo.getPredicate()
							.getAllAttributes();
					SDFAttributeList preAttribs = pre.getOutElements();
					// Die zu verschiebende Selektion muss die nötigen
					// Attribute für den "noch"-Kindknoten liefern.
					if (!SDFAttributeList.subset(preAttribs, reqAttribs)) {
						continue;
					}
					// Testen, ob bei einem der Vorgänger des Vorgängers alle
					// für die Selektionsbedingung notwendigen Attribute
					// vorhanden sind. Merken, welcher der evtl. 2 Kindknoten der
					// passende ist.
					if (SDFAttributeList.subset(reqAttribs, attribs)) {
						poToHandle = spo;
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
			spoChildPos = 0;
		}
		else {
			father.setInputPO(1, poToHandle.getInputPO());
			spoChildPos = 1;
		}
		
		// Eingabe von poToHandle auf die unter preChildPos angegebene Position
		// des Vorgängers des Vorgängers setzen.
		// Vorgänger von father bzw. früherer Vorgänger von poToHandle an dem
		// korrekten Eingabeport poToHandle als Eingabe zuweisen.
		poToHandle.setInputPO(poToHandle.getInputPO(0).getInputPO(preChildPos));
		father.getInputPO(spoChildPos).setInputPO(preChildPos, poToHandle);
		
		// Ausgabeelemente des SelectPO auf die Ausgabeelemente des Vorgängers
		// setzen.
		poToHandle.setOutElements(poToHandle.getInputPO().getOutElements());

		return logicalplan;
	}
}
