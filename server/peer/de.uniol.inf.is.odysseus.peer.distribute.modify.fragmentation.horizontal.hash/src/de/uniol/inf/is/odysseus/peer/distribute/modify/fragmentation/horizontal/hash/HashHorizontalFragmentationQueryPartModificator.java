package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
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
 * where the attributes form the hash key. <br />
 * Note: source name can also be an unique identifier of an operator.
 * @author Michael Brand
 */
public class HashHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {
	
	private static final Logger log = LoggerFactory.getLogger(HashHorizontalFragmentationQueryPartModificator.class);
	
	@Override
	protected Optional<List<String>> determineKeyAttributes(List<String> modificationParameters) {
		
		int firstIndexOfKeyAttributes = 2;
		
		Preconditions.checkNotNull(modificationParameters, "Modification parameters must be not null!");
		Preconditions.checkArgument(modificationParameters.size() >= firstIndexOfKeyAttributes, "There must be at least " + firstIndexOfKeyAttributes + " modification parameters!");
		
		if(modificationParameters.size() == firstIndexOfKeyAttributes) {
			
			HashHorizontalFragmentationQueryPartModificator.log.debug("Found no attributes to form a key.");
			return Optional.absent();
			
		}
		
		// The return value
		List<String> attributes = Lists.newArrayList(modificationParameters.subList(firstIndexOfKeyAttributes, modificationParameters.size()));		
		HashHorizontalFragmentationQueryPartModificator.log.debug("Found '" + attributes +  "' as attributes to form a key.");
		return Optional.of(attributes);
		
	}
	
	/**
	 * Determines the attributes to be fragmented given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION fragmentation_horizontal_hash <code>source</code> <code>number of fragments</code> <code>attribute1</code> ... <code>attributeN</code>
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter fragmentation_horizontal_range.
	 * @return The attributes to be fragmented given by the user, if there are any.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null.
	 */
	private Optional<List<String>> determineAttributes(List<String> modificationParameters)
			throws NullPointerException {
		
		// Preconditions 1
		if(modificationParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");
		
		Optional<List<String>> attributes = this.determineKeyAttributes(modificationParameters);
		
		if(!attributes.isPresent())
			return attributes;
		
		// The return value
		List<String> fullQualifiedAttributes = Lists.newArrayList();
		for(String attribute : attributes.get())
			fullQualifiedAttributes.add(attribute);
		return Optional.of(fullQualifiedAttributes);
		
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
		
		Optional<List<String>> attributes = this.determineAttributes(modificationParameters);
			
		HashFragmentAO fragmentAO = new HashFragmentAO();
		fragmentAO.setNumberOfFragments(numFragments);
		if(attributes.isPresent())
			fragmentAO.setAttributes(attributes.get());
		
		return fragmentAO;
		
	}

}