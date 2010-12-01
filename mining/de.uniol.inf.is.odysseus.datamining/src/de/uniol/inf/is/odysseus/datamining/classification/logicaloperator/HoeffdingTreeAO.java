package de.uniol.inf.is.odysseus.datamining.classification.logicaloperator;

import de.uniol.inf.is.odysseus.datamining.logicaloperator.AbstractDataMiningAO;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HoeffdingTreeAO extends AbstractDataMiningAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2997087529890096578L;
	private SDFAttribute labelAttribute;
	private Double probability;

	public HoeffdingTreeAO(HoeffdingTreeAO hoeffdingTreeAO) {
		super(hoeffdingTreeAO);
		labelAttribute = hoeffdingTreeAO.labelAttribute.clone();
	}

	public int getLabelPosition() {
		int i = 0;
		for (SDFAttribute attribute : getInputSchema()) {
			if (attribute.equals(labelAttribute)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public HoeffdingTreeAO() {
	}

	@Override
	public SDFAttributeList getOutputSchema() {

		return getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new HoeffdingTreeAO(this);
	}

	public void setLabelAttribute(SDFAttribute labelAttribute) {
		this.labelAttribute = labelAttribute;

	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

}
