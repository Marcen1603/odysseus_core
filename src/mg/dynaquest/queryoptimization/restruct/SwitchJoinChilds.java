/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;

/**
 * @author  Olaf Twesten
 */
public class SwitchJoinChilds extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private JoinPO poToHandle = null;

	/*
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#test(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Auf Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach JoinPO im Plan
		ArrayList<JoinPO> allJoins = new ArrayList<JoinPO>();
		findJoins(logicalplan, allJoins);
		for (int i = 0; i < allJoins.size(); i++) {
			JoinPO jpo = allJoins.get(i);
			if (determineBiggerChild(jpo) == 0) {
				poToHandle = jpo;
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
		//Kindknoten tauschen
		AlgebraPO leftChild = poToHandle.getInputPO(0);
		poToHandle.setInputPO(0, poToHandle.getInputPO(1));
		poToHandle.setInputPO(1, leftChild);
		return logicalplan;
	}
	
	protected int determineBiggerChild(JoinPO jpo) {
		//TODO Grösseren Kindknoten bestimmen und zurückliefern
		return 1;
	}
}
