package de.uniol.inf.is.odysseus.hmm.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="FEATUREEXTRACTION", minInputPorts=1, maxInputPorts=1)
public class FeatureExtractionAO extends UnaryLogicalOp implements ILogicalOperator {

	/**
	 * Auto-generated serial by eclipse
	 */
	private static final long serialVersionUID = -3986431121912825299L;


	// The default constructor is required as instances of logical operators are created by newInstance()
	public FeatureExtractionAO() {
		super();
	}

	//constructor called, if clone() was used
	public FeatureExtractionAO(FeatureExtractionAO feAO) {
		super(feAO);
	}
		
		
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new FeatureExtractionAO(this);
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		
		
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();		
		SDFAttribute attributeType = new SDFAttribute(null, "datatype", SDFDatatype.STRING);
		attributes.add(attributeType);
		
		SDFAttribute attributeId = new SDFAttribute(null, "value", SDFDatatype.DOUBLE);
		attributes.add(attributeId);
		
		SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), attributes);
		return outSchema;
	}

}
