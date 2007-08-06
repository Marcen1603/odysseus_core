/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Olaf Twesten
 */
public class SwitchSelectionUnionDifference extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private SelectPO poToHandle = null;

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
		// Auf Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach SelectPO im Plan
		ArrayList<SelectPO> allSelects = new ArrayList<SelectPO>();
		findSelects(logicalplan, allSelects);
		for (int i = 0; i < allSelects.size(); i++) {
			SelectPO spo = allSelects.get(i);
			AlgebraPO pre = spo.getInputPO();
			// Vorgänger muss DifferencePO oder UnionPO sein.
			if ((pre instanceof DifferencePO) || (pre instanceof UnionPO)) {
				SDFAttributeList attribsLeft = pre.getInputPO(0)
						.getOutElements();
				SDFAttributeList attribsRight = pre.getInputPO(1)
						.getOutElements();
				SDFAttributeList reqAttribs = spo.getPredicate()
						.getAllAttributes();
				// Testen, ob bei einem der Vorgänger des Vorgängers alle
				// für die Selektionsbedingung notwendigen Attribute
				// vorhanden sind.
				if (SDFAttributeList.subset(reqAttribs, attribsLeft)
						&& SDFAttributeList.subset(reqAttribs, attribsRight)) {
					poToHandle = spo;
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
			spoChildPos = 0;
		} else {
			father.setInputPO(1, poToHandle.getInputPO());
			spoChildPos = 1;
		}

		// Neuen SelectPO erstellen.
		// Eingabe von poToHandle auf die Vorgänger des Vorgängers setzen.
		// Vorgänger von father bzw. früherer Vorgänger von poToHandle (union
		// oder difference)an dem korrekten Eingabeport poToHandle und
		// poToHandle2 als Eingabe zuweisen.
		SelectPO poToHandle2 = new SelectPO();
		poToHandle2.setPOName(poToHandle.getPOName() + " Kopie");
		poToHandle2.setOutElements(poToHandle.getOutElements());
		poToHandle2.setPredicate(poToHandle.getPredicate());

		poToHandle2.setInputPO(poToHandle.getInputPO(0).getInputPO(1));
		poToHandle.setInputPO(poToHandle.getInputPO(0).getInputPO(0));

		father.getInputPO(spoChildPos).setInputPO(0, poToHandle);
		father.getInputPO(spoChildPos).setInputPO(1, poToHandle2);

		// Ausgabeelemente der beiden SelectPO auf die Ausgabeelemente
		// der Vorgänger setzen.
		poToHandle.setOutElements(poToHandle.getInputPO().getOutElements());
		poToHandle2.setOutElements(poToHandle2.getInputPO().getOutElements());
		
		return logicalplan;
	}
}
