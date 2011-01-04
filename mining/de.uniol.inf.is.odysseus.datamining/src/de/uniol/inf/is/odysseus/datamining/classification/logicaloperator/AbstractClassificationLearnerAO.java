package de.uniol.inf.is.odysseus.datamining.classification.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractClassificationLearnerAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5930667720134167936L;
	protected SDFAttributeList attributes;
	protected SDFAttribute labelAttribute;
	protected AbstractClassificationLearnerAO() {
		
	}
	public AbstractClassificationLearnerAO(AbstractClassificationLearnerAO copy) {
		super(copy);
		this.attributes = copy.getAttributes().clone();
		this.labelAttribute =copy.labelAttribute.clone();
	}
	
	public SDFAttributeList getAttributes() {
		return attributes;
	}

	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(), attributes);
	}
	
	public int getLabelPosition() {
		int i = 0;
		for (SDFAttribute attribute : getInputSchema()) {
			if (attribute.equals(labelAttribute)) {
				return i;
			}
			i++;
		}
		return i;
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
