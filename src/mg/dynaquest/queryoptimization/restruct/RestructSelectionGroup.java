/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicateFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;

/**
 * @author  Olaf Twesten
 */
public class RestructSelectionGroup extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private SelectPO poToHandle = null;

	/**
	 * @uml.property  name="poToRemove"
	 * @uml.associationEnd  
	 */
	private AlgebraPO poToRemove = null;

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
			// Vorgänger muss SelectPO sein.
			if (pre instanceof SelectPO) {
				poToHandle = spo;
				poToRemove = pre;
				return true;
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

		// Prädikat des Vorgänger SelectPO zu poToHandle hinzufügen
		SDFPredicate newPred = SDFComplexPredicateFactory
				.combinePredicates(
						"http://www-is.informatik.uni-oldenburg.de/~grawund/" +
						"rdf/2003/04/sdf_predicates.sdf#AndPredicate",
						poToHandle.getPredicate(), poToRemove.getPredicate());
		poToHandle.setPredicate(newPred);

		// Den Nachfolgerknoten von poToRemove als neuen Eingabeknoten
		// für poToHandle festlegen
		poToHandle.setInputPO(0, poToRemove.getInputPO(0));

		return logicalplan;
	}
}
