package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.logicaloperator.RoundRobinFragmentAO;

// TODO javaDoc
public class RoundRobinHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {
	
	/**
	 * The name of this modificator.
	 */
	private static final String name = "roundrobinfragmentation";
	
	/**
	 * The class of the used operator for fragmentation.
	 */
	private static final Class<? extends ILogicalOperator> fragmentationClass = RoundRobinFragmentAO.class;

	@Override
	public String getName() {
		
		return RoundRobinHorizontalFragmentationQueryPartModificator.name;
		
	}

	@Override
	protected Class<? extends ILogicalOperator> getFragmentationClass() {
		
		return RoundRobinHorizontalFragmentationQueryPartModificator.fragmentationClass;
		
	}

}
