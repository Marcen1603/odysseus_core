package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.logicaloperator.HashFragmentAO;

/**
 * A concrete modifier of {@link ILogicalQueryPart}s, which fragments data streams horizontally by hash from a given source 
 * into parallel query parts and inserts operators to merge the result sets of the parallel fragments 
 * for each relative sink within every single query part. <br />
 * Usage in Odysseus Script: <br />
 * #PEER_MODIFICATION fragmentation_horizontal_hash &lt;source name&gt; &lt;number of fragments&gt; 
 * where the whole tuple forms the hash key or <br />
 * #PEER_MODIFICATION fragmentation_horizontal_hash &lt;source name&gt; &lt;number of fragments&gt; &lt;attribute1&gt ... &lt;attributeN&gt 
 * where the attributes form the hash key.
 * @author Michael Brand
 */
public class HashHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {
	
	/**
	 * Determines the attributes given by the user (Odysseus script), if there are any. <br />
	 * #PEER_MODIFICATION fragmentation_horizontal_hash &lt;source name&gt; &lt;number of fragments&gt; &lt;attribute1&gt ... &lt;attributeN&gt
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter fragmentation_horizontal_hash.
	 * @return The attributes given by the user, if there are any.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null.
	 */
	private static Optional<List<String>> determineAttributes(List<String> modificationParameters) 
			throws NullPointerException {
		
		// Preconditions 1
		if(modificationParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");
		else if(modificationParameters.size() == 2)
			return Optional.absent();
		
		String sourceName = modificationParameters.get(0);
		List<String> attributes = Lists.newArrayList();
		
		for(int index = 2; index < modificationParameters.size(); index++)
			attributes.add(sourceName + "." + modificationParameters.get(index));
		
		return Optional.of(attributes);
		
	}

	@Override
	public String getName() {
		
		return "fragmentation_horizontal_hash";
		
	}

	@Override
	protected ILogicalOperator createOperatorForFragmentation(
			int numFragments,
			List<String> modificationParameters) 
			throws QueryPartModificationException {
		
		if(numFragments < 1)
			throw new QueryPartModificationException("Invalid number of fragments: " + numFragments);
		
		Optional<List<String>> attributes = HashHorizontalFragmentationQueryPartModificator.determineAttributes(modificationParameters);
			
		HashFragmentAO fragmentAO = new HashFragmentAO();
		fragmentAO.setNumberOfFragments(numFragments);
		if(attributes.isPresent())
			fragmentAO.setAttributes(attributes.get());
		
		return fragmentAO;
		
	}

}