package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.text.Collator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;

/**
 * A {@link RangeFragmentPO} can be used to realize a {@link RangeFragmentAO}. <br />
 * The {@link RangeFragmentPO} uses a String or Double comparator to transfer {@link StreamObject}s to different output ports and can handle {@link IPunctuations}.
 * @author Michael Brand
 */
public class RangeFragmentPO<T extends IStreamObject<IMetaAttribute>> 
		extends AbstractStaticFragmentPO<T> {
	
	private static final Logger log = LoggerFactory.getLogger(RangeFragmentPO.class);
	
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
	 * Constructs a new {@link RangeFragmentPO}.
	 * @param fragmentAO the {@link RangeFragmentAO} transformed to this {@link RangeFragmentPO}.
	 */
	public RangeFragmentPO(RangeFragmentAO fragmentAO) {
		
		super(fragmentAO);
		
		// Determine index of attribute
		for(int index = 0; index < fragmentAO.getInputSchema().size(); index++) {
			
			if(fragmentAO.getInputSchema().get(index).getURI().equals(fragmentAO.getAttribute()))
				this.attributeIndex = index;
			
		}
		
		if(attributeIndex == -1)
			throw new QueryParseException("Could not find " + fragmentAO.getAttribute() + " within " + 
					fragmentAO.getInputSchema() + "!");
		
		this.ranges = fragmentAO.getRanges();
		this.numericRanges = fragmentAO.areRangesNumeric();
		
	}

	
	@Override
	protected int route(IStreamObject<IMetaAttribute> object) {
		
		// XXX Only for tuple M.B.
		
		Collator collator = Collator.getInstance();
		
		if(object instanceof Tuple) {
			
			Tuple<IMetaAttribute> tuple = (Tuple<IMetaAttribute>) object;
			
			for(int rangeNo = 0; rangeNo < this.ranges.size(); rangeNo++) {
				
				try {
				
					if(this.numericRanges) {
						
						Double range = Double.valueOf(this.ranges.get(rangeNo));
						Double attr = ((Number) tuple.getAttribute(this.attributeIndex)).doubleValue();
						
						if(Double.compare(attr, range) >= 0)
							return rangeNo;
						
					} else { // String ranges
						
						String range = this.ranges.get(rangeNo);
						String attr = (String) tuple.getAttribute(this.attributeIndex);
						
						if(collator.compare(attr, range) >= 0)
							return rangeNo;
						
					}
					
				} catch(Exception e) {
					
					RangeFragmentPO.log.error("Could not cast {}! \n{}", tuple.getAttribute(this.attributeIndex), e.getStackTrace());
					return this.ranges.size();
					
				}
				
			}
			
		}
		
		return this.ranges.size();
		
	}
	
}