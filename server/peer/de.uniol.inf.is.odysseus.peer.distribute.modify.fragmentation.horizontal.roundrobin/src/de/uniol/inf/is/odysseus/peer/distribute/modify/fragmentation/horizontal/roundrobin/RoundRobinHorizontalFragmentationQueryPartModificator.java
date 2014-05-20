package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin;

import java.util.List;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.logicaloperator.RoundRobinFragmentAO;

/**
 * A concrete modifier of {@link ILogicalQueryPart}s, which fragments data streams horizontally and round robin from a given source 
 * into parallel query parts and inserts operators to merge the result sets of the parallel fragments 
 * for each relative sink within every single query part. <br />
 * Usage in Odysseus Script: <br />
 * #PEER_MODIFICATION fragmentation_horizontal_roundrobin &lt;source name&gt; &lt;number of fragments&gt; <br />
 * Note: source name can also be an unique identifier of an operator.
 * @author Michael Brand
 */
public class RoundRobinHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {
	
	@Override
	protected Optional<List<String>> determineKeyAttributes(List<String> modificationParameters) {
		
		// There can be no key attributes for a round robin fragmentation
		return Optional.absent();
		
	}

	@Override
	public String getName() {
		
		return "fragmentation_horizontal_roundrobin";
		
	}

	@Override
	protected ILogicalOperator createOperatorForFragmentation(
			int numFragments,
			List<String> modificationParameters) 
			throws QueryPartModificationException {
		
		if(numFragments < 1)
			throw new QueryPartModificationException("Invalid number of fragments: " + numFragments);
			
		RoundRobinFragmentAO fragmentAO = new RoundRobinFragmentAO();
		fragmentAO.setNumberOfFragments(numFragments);
		return fragmentAO;
		
	}

}