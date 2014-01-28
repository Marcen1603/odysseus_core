package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.logicaloperator.RoundRobinFragmentAO;

/**
 * A concrete modifier of {@link ILogicalQueryPart}s, which fragments data streams horizontally and round robin from a given source 
 * into parallel query parts and inserts operators to merge the result sets of the parallel fragments 
 * for each relative sink within every single query part.
 * @author Michael Brand
 */
public class RoundRobinHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {

	@Override
	public String getName() {
		
		return "fragmentation_horizontal_roundrobin";
		
	}

	@Override
	protected ILogicalOperator createOperatorForFragmentation() {
		
		return new RoundRobinFragmentAO();
		
	}

}