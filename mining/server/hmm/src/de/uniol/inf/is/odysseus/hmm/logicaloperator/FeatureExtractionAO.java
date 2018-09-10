package de.uniol.inf.is.odysseus.hmm.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Unary Logical Operator. Feature Extraction is used to extract the most important information from
 * an input stream, e.g. calculating the orientation angle from given coordinates.
 * 
 * @author Michael Moebes, mmo
 * @author Christian Pieper, cpi
 * 
 */
@LogicalOperator(name = "FEATUREEXTRACTION", minInputPorts = 1, maxInputPorts = 1, doc="Feature Extraction is used to extract the most important information from an input stream, e.g. calculating the orientation angle from given coordinates.",category={LogicalOperatorCategory.MINING})
public class FeatureExtractionAO extends UnaryLogicalOp {

	/**
	 * Auto-generated serial by eclipse
	 */
	private static final long serialVersionUID = -3986431121912825299L;

	// The default constructor is required as instances of logical operators are created by newInstance()
	public FeatureExtractionAO() {
		super();
	}

	// constructor called, if clone() was used
	public FeatureExtractionAO(FeatureExtractionAO feAO) {
		super(feAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new FeatureExtractionAO(this);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#getOutputSchemaIntern(int)
	 * 
	 * Changing the output schema from 20 attributes to 2 attributes
	 * String "datatype" - e.g. orientation, velocity, coordinate
	 * Double "value" - e.g. 180.2 degree 
	 */
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {

		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		SDFAttribute attributeType = new SDFAttribute(null, "datatype",	SDFDatatype.STRING, null, null, null);
		attributes.add(attributeType);

		SDFAttribute attributeId = new SDFAttribute(null, "value", SDFDatatype.DOUBLE, null, null, null);
		attributes.add(attributeId);

		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema());
		return outSchema;
	}

}
