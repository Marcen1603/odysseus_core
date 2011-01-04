package de.uniol.inf.is.odysseus.datamining.classification.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class HoeffdingTreeAO extends AbstractClassificationLearnerAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2997087529890096578L;

	private Double probability;

	private Double tie;

	public HoeffdingTreeAO(HoeffdingTreeAO hoeffdingTreeAO) {
		super(hoeffdingTreeAO);
		probability = hoeffdingTreeAO.getProbability();
		tie = hoeffdingTreeAO.getTie();
	}

	public Double getTie() {
		return tie;
	}

	public HoeffdingTreeAO() {
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList outputSchema = new SDFAttributeList();
		SDFAttribute classifier = new SDFAttribute("classifier");
		classifier.setDatatype(SDFDatatypeFactory.getDatatype("IClassifier"));
		outputSchema.add(classifier);
		return outputSchema;
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

	public void setTie(Double tie) {
		this.tie = tie;
	}

}
