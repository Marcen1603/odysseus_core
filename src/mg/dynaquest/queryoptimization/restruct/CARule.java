/*
 * Created on 14.12.2004
 *
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;

/**
 * @author Marco Grawunder
 * 
 */
abstract public class CARule {
	abstract public boolean test(AlgebraPO logicalplan);

	abstract public SupportsCloneMe process(AlgebraPO logicalplan);

	/**
	 * @param logicalplan
	 * @param father
	 * @return
	 */
	protected static AlgebraPO determineFatherNode(
			AlgebraPO logicalplan, SupportsCloneMe son) {
		// Breitensuche
		boolean found = false;
		Queue<AlgebraPO> fifo = new LinkedList<AlgebraPO>();
		fifo.offer(logicalplan);
		while (!fifo.isEmpty() && found == false) {
			AlgebraPO node = fifo.poll();
			for (int i = 0; i < node.getNumberOfInputs(); i++) {
				fifo.offer(node.getInputPO(i));
				if (node.getInputPO(i).equals(son)) {
					return node;
				}
			}
		}
        return null;
	}
	
	/**
	 * @param logicalplan
	 * @return
	 */
	protected static void findSelects(AlgebraPO logicalPlan,
			Collection<SelectPO> allSelects) {
		if (logicalPlan instanceof SelectPO) {
			allSelects.add((SelectPO) logicalPlan);
		}
		for (int i = 0; i < logicalPlan.getNumberOfInputs(); i++) {
			findSelects(logicalPlan.getInputPO(i), allSelects);
		}
	}
	
	/**
	 * @param logicalplan
	 * @return
	 */
	protected static void findProjects(AlgebraPO logicalPlan,
			Collection<ProjectPO> allProjects) {
		if (logicalPlan instanceof ProjectPO) {
			allProjects.add((ProjectPO) logicalPlan);
		}
		for (int i = 0; i < logicalPlan.getNumberOfInputs(); i++) {
			findProjects(logicalPlan.getInputPO(i), allProjects);
		}
	}
	
	/**
	 * @param logicalplan
	 * @return
	 */
	protected static void findJoins(AlgebraPO logicalPlan,
			Collection<JoinPO> allJoins) {
		if (logicalPlan instanceof JoinPO) {
			allJoins.add((JoinPO) logicalPlan);
		}
		for (int i = 0; i < logicalPlan.getNumberOfInputs(); i++) {
			findJoins(logicalPlan.getInputPO(i), allJoins);
		}
	}
}
