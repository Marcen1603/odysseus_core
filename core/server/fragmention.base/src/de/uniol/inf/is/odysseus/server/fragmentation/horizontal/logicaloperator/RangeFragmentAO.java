package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import java.text.Collator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

/**
 * A {@link RangeFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link RangeFragmentAO} has the attribute to be fragmented and the ranges as parameters and it must have exact one input.
 * It can be used in PQL: <br />
 * <code>output = RANGEFRAGMENT([ATTRIBUTE=x, RANGES = [r1...rn]], input)</code>
 * @author Michael Brand
 */
@LogicalOperator(category = {LogicalOperatorCategory.PROCESSING}, doc = "Can be used to fragment incoming streams", maxInputPorts = 1, minInputPorts = 1, name = "RANGEFRAGMENT")
public class RangeFragmentAO extends AbstractStaticFragmentAO {

	private static final long serialVersionUID = -2630113178519582998L;
	
	private static final Logger log = LoggerFactory.getLogger(RangeFragmentAO.class);
	
	/**
	 * Removes leading and closing double quotes.
	 * @throws QueryParseException if not every entry of <code>ranges</code> starts and ends with a double quote. 
	 * Additionally if any entries length is less than 3.
	 */
	private static List<String> preprocessRanges(List<String> ranges) throws QueryParseException {
		
		List<String> processedRanges = Lists.newArrayList();
		
		for(String range : ranges) {
			
			if(!range.startsWith("\"") || !range.endsWith("\"") || range.length() < 3) {
				
				throw new QueryParseException(range + 
						" is not a valid String range! String ranges must start and end with '\"'!");
				
			}

			processedRanges.add(range.substring(1, range.length() - 1));
			
		}
		
		return processedRanges;
		
	}

	/**
	 * Checks, if the ranges are to be interpreted as numbers.
	 * @param ranges The ranges to check.
	 * @return True, if the entries of <code>ranges</code> are to be interpreted as numbers; 
	 * false, if they are to be interpreted as Strings.
	 */
	private static boolean areRangesNumeric(List<String> ranges) {
		
		for(String range : ranges) {
			
			if(range.contains("\""))				
				return false;
			
		}
		
		return true;
		
	}
	
	/**
	 * The URI of the attribute to be fragmented.
	 */
	private String attributeURI;
	
	/**
	 * The index of the attribute to be fragmented within the input schema.
	 */
	private int attributeIndex;
	
	/**
	 * The minimum values of each range as Strings and sorted descending.
	 */
	private List<String> ranges;
	
	/**
	 * True, if the entries of <code>ranges</code> are to be interpreted as numbers; 
	 * false, if they are to be interpreted as Strings.
	 */
	private boolean numericRanges;
	
	/**
	 * Sorts the ranges descending.
	 */
	private void sortRanges() {
		
		Collator collator = Collator.getInstance();
		List<String> unsortedRanges = Lists.newArrayList(ranges);
		List<String> sortedRanges = Lists.newArrayList();
		
		while(!unsortedRanges.isEmpty()) {
			
			String maxRange = null;
			
			for(String range : unsortedRanges) {
				
				if(maxRange == null)
					maxRange = range;
				else if(this.numericRanges && Double.valueOf(range) > Double.valueOf(maxRange))
					maxRange = range;
				else if(!this.numericRanges && collator.compare(range, maxRange) > 0)
					maxRange = range;
				
			}
			
			if(maxRange != null) {
				
				sortedRanges.add(maxRange);
				unsortedRanges.remove(maxRange);
				
			}
			
		}
		
		this.ranges = sortedRanges;
		RangeFragmentAO.log.debug("Sorted ranges: {}", sortedRanges);
		
	}

	/**
	 * Constructs a new {@link RangeFragmentAO}.
	 * @see UnaryLogicalOp#UnaryLogicalOp()
	 */
	public RangeFragmentAO() {
		
		super();
		
		this.attributeURI = null;
		this.attributeIndex = -1;
		this.ranges = Lists.newArrayList();
		this.numericRanges = false;
		
	}

	/**
	 * Constructs a new {@link RangenFragmentAO} as a copy of an existing one.
	 * @param fragmentAO The {@link RangeFragmentAO} to be copied.
	 * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
	 */
	public RangeFragmentAO(RangeFragmentAO fragmentAO) {
		
		super(fragmentAO);
		
		this.attributeURI = fragmentAO.attributeURI;
		this.attributeIndex = fragmentAO.attributeIndex;
		this.ranges = Lists.newArrayList(fragmentAO.ranges);
		this.numericRanges = fragmentAO.numericRanges;
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new RangeFragmentAO(this);
		
	}
	
	@Override
	public void initialize() {
		
		// Determine index of attribute
		for(int index = 0; index < this.getInputSchema().size(); index++) {
			
			if(this.getInputSchema().get(index).getURI().equals(this.attributeURI))
				this.attributeIndex = index;
			
		}
		
		if(attributeIndex == -1)
			throw new QueryParseException("Could not find " + this.attributeURI + " within " + this.getInputSchema() + "!");
		
	}
	
	/**
	 * Returns the URI of the attribute to be fragmented.
	 */
	@GetParameter(name = "ATTRIBUTE")
	public String getAttribute() {

		return this.attributeURI;
		
	}

	/**
	 * Sets the URI of the attribute to be fragmented.
	 */
	@Parameter(type = StringParameter.class, name = "ATTRIBUTE", optional = false)
	public void setAttribute(String uri) {
		
		this.attributeURI = uri;
		this.addParameterInfo("ATTRIBUTE", "'" + this.attributeURI + "'");
		
	}
	
	/**
	 * Returns the minimum values of each range as Strings and sorted descending.
	 */
	@GetParameter(name = "RANGES")
	public List<String> getRanges() {

		return this.ranges;
		
	}

	/**
	 * Sets the minimum values of each range.
	 */
	@Parameter(type = StringParameter.class, name = "RANGES", optional = false, isList = true)
	public void setRanges(List<String> ranges) {
	
		this.ranges.clear();
		List<String> r = Lists.newArrayList();
		
		if(RangeFragmentAO.areRangesNumeric(ranges)) {
			
			this.ranges.addAll(ranges);
			this.numericRanges = true;
			
		} else {	// String ranges
			
			this.ranges.addAll(RangeFragmentAO.preprocessRanges(ranges));
			this.numericRanges = false;
			
		}
		
		this.setNumberOfFragments(ranges.size() + 1); // +1 for all elements, which do not match any range
		this.sortRanges();	
		
		for(String range : this.ranges)
			r.add("'" + range + "'");
		
		this.addParameterInfo("RANGES", r);
		
	}
	
	/**
	 * Checks, if the ranges are to be interpreted as numbers.
	 * @return True, if the entries of {@link #getRanges()} are to be interpreted as numbers; 
	 * false, if they are to be interpreted as Strings.
	 */
	public boolean areRangesNumeric() {
		
		return this.numericRanges;
		
	}

}