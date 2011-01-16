package de.uniol.inf.is.odysseus.datamining.classification.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * This class represents a logical hoeffding tree operator used to learn a
 * hoeffding tree from a data stream
 * 
 * @author Sven Vorlauf
 * 
 */
public class HoeffdingTreeAO extends AbstractClassificationLearnerAO {

	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = 2997087529890096578L;

	/**
	 * the probability that a chosen attribut is not the best split attribute
	 */
	private Double probability;

	/**
	 * the name of the evaluation measure to use
	 */
	private String attributeEvaluationMeasure;

	/**
	 * create a new logical hoeffding tree operator as a copy of another
	 * hoeffding tree operator
	 * 
	 * @param hoeffdingTreeAO
	 *            the operator to copy
	 */
	public HoeffdingTreeAO(HoeffdingTreeAO hoeffdingTreeAO) {
		super(hoeffdingTreeAO);
		probability = hoeffdingTreeAO.getProbability();
	}

	/**
	 * create a new hoeffding tree operator
	 */
	public HoeffdingTreeAO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema
	 * ()
	 */
	@Override
	public SDFAttributeList getOutputSchema() {

		// define the schema as one attribute for the classifier
		SDFAttributeList outputSchema = new SDFAttributeList();
		SDFAttribute classifier = new SDFAttribute("classifier");
		classifier.setDatatype(SDFDatatypeFactory.getDatatype("IClassifier"));
		outputSchema.add(classifier);
		return outputSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new HoeffdingTreeAO(this);
	}

	/**
	 * set the attribute holding the class
	 * 
	 * @param labelAttribute
	 *            the attribute to set
	 */
	public void setLabelAttribute(SDFAttribute labelAttribute) {
		this.labelAttribute = labelAttribute;

	}

	/**
	 * get the probability that a chosen attribut is not the best split
	 * attribute
	 * 
	 * @return the probability
	 */
	public Double getProbability() {
		return probability;
	}

	/**
	 * set the probability that a chosen attribut is not the best split
	 * attribute
	 * 
	 * @param probability
	 *            the probability to set
	 */
	public void setProbability(Double probability) {
		this.probability = probability;
	}

	/**
	 * te the name of teh evaluation measure to use
	 * 
	 * @param attributeEvaluationMeasure
	 *            the name of the measure
	 */
	public void setAttributeEvaluationMeasure(String attributeEvaluationMeasure) {
		this.attributeEvaluationMeasure = attributeEvaluationMeasure;

	}

	/**
	 * get the name of the evaluation measure to use
	 * 
	 * @return the name to set
	 */
	public String getAttributeEvaluationMeasure() {
		return attributeEvaluationMeasure;
	}

}
