package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.logicaloperator.RangeFragmentAO;

/**
 * A concrete modifier of {@link ILogicalQueryPart}s, which fragments data streams horizontally and by given ranges from a given source 
 * into parallel query parts and inserts operators to merge the result sets of the parallel fragments 
 * for each relative sink within every single query part.
 * @author Michael Brand
 */
public class RangeHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {
	
	/**
	 * Determines the attribute to be fragmented given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION fragmentation_horizontal_range <code>source.attribute</code> <code>range1</code> ... <code>rangeN</code>
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter fragmentation_horizontal_range.
	 * @return The attribute to be fragmented given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null or if there is no attribute to be fragmented.
	 */
	private static String determineAttribute(List<String> modificationParameters)
			throws NullPointerException {
		
		// Preconditions 1
		if(modificationParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");
		
		String attribute = modificationParameters.get(0).split("\\.")[1];
		
		if(attribute.isEmpty())
			throw new NullPointerException("No attribute given to fragment!");
		
		return attribute;
		
	}
	
	/**
	 * Determines the ranges given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION fragmentation_horizontal_range <code>source.attribute</code> <code>range1</code> ... <code>rangeN</code>
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter fragmentation_horizontal_range.
	 * @return The ranges given by the user.
	 * @throws NullPointerException if <code>modificatorParameters</code> is null or if there are no ranges given.
	 */
	private static List<String> determineRanges(List<String> modificationParameters) 
			throws NullPointerException {
		
		// Preconditions 1
		if(modificationParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");
		else if(modificationParameters.size() < 2)
			throw new NullPointerException("No ranges given!");
		
		List<String> ranges = Lists.newArrayList(modificationParameters);
		ranges.remove(0);
		return ranges;
		
	}

	@Override
	public String getName() {
		
		return "fragmentation_horizontal_range";
		
	}

	@Override
	protected ILogicalOperator createOperatorForFragmentation(
			int numFragments,
			List<String> modificationParameters) 
			throws QueryPartModificationException {
		
		if(numFragments < 1)
			throw new QueryPartModificationException("Invalid number of fragments: " + numFragments);
		
		String attribute = RangeHorizontalFragmentationQueryPartModificator.determineAttribute(modificationParameters);
		List<String> ranges = RangeHorizontalFragmentationQueryPartModificator.determineRanges(modificationParameters);
			
		RangeFragmentAO fragmentAO = new RangeFragmentAO();
		fragmentAO.setAttribute(attribute);
		fragmentAO.setRangesList(ranges);
		return fragmentAO;
		
	}
	
	@Override
	protected int determineDegreeOfFragmentation(
			List<String> modificatorParameters) 
			throws NullPointerException {
		
		// Preconditions 1
		if(modificatorParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");
		
		return modificatorParameters.size(); // +1 for the default output port
		
	}
	
	@Override
	protected String determineSourceName(
			List<String> modificatorParameters) 
			throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(modificatorParameters == null)
			throw new NullPointerException("Parameters for query part modificator must not be null!");
		else if(modificatorParameters.isEmpty())
			throw new IllegalArgumentException("Parameters for query part fragmentation stratgey does neither contain " +
					"the name of the source to be fragmented nor the degree of fragmentation!");
		
		return modificatorParameters.get(0).split("\\.")[0];
		
	}

}