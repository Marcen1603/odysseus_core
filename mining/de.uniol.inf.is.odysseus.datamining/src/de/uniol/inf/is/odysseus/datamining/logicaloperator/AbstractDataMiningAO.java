package de.uniol.inf.is.odysseus.datamining.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractDataMiningAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5930667720134167936L;
	protected SDFAttributeList attributes;
	public AbstractDataMiningAO() {
		
	}
	public AbstractDataMiningAO(AbstractDataMiningAO copy) {
		super(copy);
		this.attributes = copy.getAttributes().clone();
	}
	
	public SDFAttributeList getAttributes() {
		return attributes;
	}

	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(), attributes);
	}

	public static int[] calcRestrictList(SDFAttributeList in,
			SDFAttributeList out) {
		int[] ret = new int[out.size()];
		int i = 0;
		for (SDFAttribute a : out) {
			int j = 0;
			int k = i;
			for (SDFAttribute b : in) {
				if (b.equals(a)) {
					ret[i++] = j;
				}
				++j;
			}
			if (k == i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	public void setAttributes(SDFAttributeList attributes) {
		this.attributes = attributes;

	}

	
	

}
