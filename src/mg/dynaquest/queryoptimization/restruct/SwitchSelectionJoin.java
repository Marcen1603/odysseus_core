/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Olaf Twesten
 */
public class SwitchSelectionJoin extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private SelectPO poToHandle = null;

	/**
	 * @uml.property  name="preChildPos"
	 */
	private int preChildPos = 0;

	/*
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#test(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Auf Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach SelectPO im Plan
		ArrayList<SelectPO> allSelects = new ArrayList<SelectPO>();
		findSelects(logicalplan, allSelects);
		for (int i = 0; i < allSelects.size(); i++) {
			SelectPO spo = allSelects.get(i);
			AlgebraPO pre = spo.getInputPO();
			// Vorgänger muss JoinPO sein.
			if (pre instanceof JoinPO) {
				for (int j = 0; j < pre.getNumberOfInputs(); j++) {
					SDFAttributeList attribs = pre.getInputPO(j)
							.getOutElements();
					SDFAttributeList reqAttribs = spo.getPredicate()
							.getAllAttributes();
					// Testen, ob bei einem der Vorgänger des Vorgängers alle
					// für die Selektionsbedingung notwendigen Attribute
					// vorhanden sind. Merken, welcher der evtl. 2 Kindknoten
					// der passende ist.
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
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#process(mg.dynaquest.queryexecution.po.base.PlanOperator)
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
		JoinPO join = (JoinPO)poToHandle.getInputPO();
		if (father.getInputPO(0).equals(poToHandle)) {
			father.setInputPO(0, join);
		} else {
			father.setInputPO(1, join);
		}

		// Eingabe von poToHandle auf die unter preChildPos angegebene Position
		// des Vorgänger JoinPO setzen.
		// Join PO an dem korrekten Eingabeport poToHandle als Eingabe zuweisen.
		poToHandle.setInputPO(join.getInputPO(preChildPos));
		join.setInputPO(preChildPos, poToHandle);

		// Ausgabeelemente des SelectPO auf die Ausgabeelemente des Vorgängers
		// setzen.
		poToHandle.setOutElements(poToHandle.getInputPO().getOutElements());

		// Ausgabeelemente des JoinPO auf die Vereinigung der
		// Ausgabeelemente der Vorgänger setzen.
		join.setOutElements(SDFAttributeList.union(join.getInputPO(0)
				.getOutElements(), join.getInputPO(1).getOutElements()));

		return logicalplan;
	}
}
