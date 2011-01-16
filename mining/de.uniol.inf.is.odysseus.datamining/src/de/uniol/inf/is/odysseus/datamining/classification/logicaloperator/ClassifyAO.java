package de.uniol.inf.is.odysseus.datamining.classification.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * This class represents a logical classify operator used to classify tuples of
 * a data stream
 * 
 * @author Sven Vorlauf
 * 
 */
public class ClassifyAO extends BinaryLogicalOp {

	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = 4257639023636590526L;

	/**
	 * the attributes that have been used to create the classifier
	 */
	protected SDFAttributeList attributes;

	/**
	 * the attribute holding the class
	 */
	protected SDFAttribute labelAttribute;

	/**
	 * create a new logical classification operator
	 */
	public ClassifyAO() {
		super();
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
		if (getLabelPosition() < getInputSchema(1).size()) {
			return getInputSchema(1);
		} else {
			// append the class to the schema if not already in
			SDFAttributeList outputSchema = new SDFAttributeList();
			outputSchema.addAll(getInputSchema(1).clone());
			SDFAttribute classLabel = new SDFAttribute("class_label");
			classLabel.setDatatype(SDFDatatypeFactory.getDatatype("Object"));
			outputSchema.add(classLabel);
			return outputSchema;
		}
	}

	/**
	 * create a new classification operator as a copy of another classification
	 * operator
	 * 
	 * @param copy
	 *            the operator to copy
	 */
	public ClassifyAO(ClassifyAO copy) {
		super(copy);
		this.attributes = copy.getAttributes().clone();
		if (copy.labelAttribute != null) {
			this.labelAttribute = copy.labelAttribute.clone();
		}
	}

	/**
	 * get the attributes used to learn the classifier
	 * 
	 * @return the attributes
	 */
	public SDFAttributeList getAttributes() {
		return attributes;
	}

	/**
	 * determine the positions of the classification attributes in the tuple
	 * 
	 * @return the positions
	 */
	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(1), attributes);
	}

	/**
	 * get the position of the class in the tuple
	 * 
	 * @return the position
	 */
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

	/**
	 * calculate the positions of the a list of attributes in another list of
	 * attributes
	 * 
	 * @param in
	 *            the schema holding all attributes
	 * @param out
	 *            the schema holding the attributes to determine the positions
	 * @return the positions of the attributes
	 */
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

	/**
	 * set the attributes to be used to learn tha classifier
	 * 
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(SDFAttributeList attributes) {
		this.attributes = attributes;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new ClassifyAO(this);
	}

	/**
	 * set the attribute that is holding the class
	 * 
	 * @param labelAttribute
	 *            the attribute to set
	 */
	public void setLabelAttribute(SDFAttribute labelAttribute) {
		this.labelAttribute = labelAttribute;
	}

}
