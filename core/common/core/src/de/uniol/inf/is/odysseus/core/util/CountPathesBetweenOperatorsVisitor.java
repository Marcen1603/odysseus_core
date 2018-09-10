/**
 * 
 */
package de.uniol.inf.is.odysseus.core.util;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.util.IExtendedGraphNodeVisitor;

/**
 * May only be used with ExtendedGraphWalker going Downstream to return correct results!
 * 
 * Finds number of pathes between node root given to the ExtendedGraphWalker and the node given in this constructor.
 * 
 * @author Dennis Nowak
 *
 */
public class CountPathesBetweenOperatorsVisitor implements IExtendedGraphNodeVisitor<IPhysicalOperator, Integer> {
	
	private IPhysicalOperator end;
	private int PathesToEnd;
	
	public CountPathesBetweenOperatorsVisitor(IPhysicalOperator end) {
		this.end = end;
	}

	@Override
	public NodeActionResult nodeAction(IPhysicalOperator node) {
		if(this.end == node) {
			this.PathesToEnd++;
		}
		return NodeActionResult.CONTINUE_ONLY;
	}

	@Override
	public void beforeFromSinkToSourceAction(IPhysicalOperator sink, IPhysicalOperator source) {
	}

	@Override
	public void afterFromSinkToSourceAction(IPhysicalOperator sink, IPhysicalOperator source) {
		
	}

	@Override
	public void beforeFromSourceToSinkAction(IPhysicalOperator source, IPhysicalOperator sink) {
		
	}

	@Override
	public void afterFromSourceToSinkAction(IPhysicalOperator source, IPhysicalOperator sink) {
		
	}

	@Override
	public Integer getResult() {
		return this.PathesToEnd;
	}

}
