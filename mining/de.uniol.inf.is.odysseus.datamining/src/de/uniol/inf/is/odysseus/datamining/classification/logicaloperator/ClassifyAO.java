package de.uniol.inf.is.odysseus.datamining.classification.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class ClassifyAO extends BinaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4257639023636590526L;
	protected SDFAttributeList attributes;
	protected SDFAttribute labelAttribute;

	public ClassifyAO() {
		super();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		if (getLabelPosition() < getInputSchema(1).size()) {
			return getInputSchema(1);
		} else {
			SDFAttributeList outputSchema = new SDFAttributeList();
			outputSchema.addAll(getInputSchema(1).clone());
			SDFAttribute classLabel = new SDFAttribute("class_label");
			classLabel.setDatatype(SDFDatatypeFactory.getDatatype("Object"));
			outputSchema.add(classLabel);
			return outputSchema;
		}
	}

	public ClassifyAO(ClassifyAO copy) {
		super(copy);
		this.attributes = copy.getAttributes().clone();
		if (copy.labelAttribute != null) {
			this.labelAttribute = copy.labelAttribute.clone();
		}
	}

	public SDFAttributeList getAttributes() {
		return attributes;
	}

	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(1), attributes);
	}

	public int getLabelPosition() {
		int i = 0;
		for (SDFAttribute attribute : getInputSchema(1)) {
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

	@Override
	public AbstractLogicalOperator clone() {
		return new ClassifyAO(this);
	}

	public void setLabelAttribute(SDFAttribute value) {
		labelAttribute = value;
	}

}
