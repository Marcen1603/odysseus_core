package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;

/**
 * The fragmentation uses the given query parts and informations from
 * Odysseus-Script to fragment a data stream for each query part as follows: <br />
 * 1. Make as many copies as the degree of fragmentation from the query parts to
 * build fragments. <br />
 * 2. Insert fragment operators and reunion operators for those fragments. <br />
 * 3. Attach all query parts not to build fragments to the modified fragments. <br />
 * <br />
 * A horizontal fragmentation splits the data streams by routing complete
 * elements to different operators. <br />
 * <br />
 * The round-robin algorithm determines the next operator for the element by round-robin.
 * 
 * @author Michael Brand
 */
public class RoundRobinHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {

	@Override
	public String getName() {

		return "fragmentation_horizontal_roundrobin";

	}

	@Override
	protected ILogicalOperator createFragmentOperator(int numFragments,
			FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");

		if (numFragments < 1)
			throw new QueryPartModificationException(
					"Invalid number of fragments: " + numFragments);

		RoundRobinFragmentAO fragmentAO = new RoundRobinFragmentAO();
		fragmentAO.setNumberOfFragments(numFragments);
		return fragmentAO;

	}

}