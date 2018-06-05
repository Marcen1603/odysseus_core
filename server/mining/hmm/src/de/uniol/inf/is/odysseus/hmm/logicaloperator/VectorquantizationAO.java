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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * Unary Logical Operator. Process the incoming feature vector, from the Feature Extraction operator 
 * to determine the cluster id.
 * Distinguish autonomous the incoming data, e.g. orientation, velocity, coordinates, to determine
 * the correct method to work with. 
 * 
 * @author Michael Moebes, mmo
 * @author Christian Pieper, cpi
 *
 */
@LogicalOperator(name = "VECTORQUANTIZATION", minInputPorts = 1, maxInputPorts = 1, doc="Process the incoming feature vector, from the Feature Extraction operator  to determine the cluster id. Distinguish autonomous the incoming data, e.g. orientation, velocity, coordinates, to determine the correct method to work with", category={LogicalOperatorCategory.MINING})
public class VectorquantizationAO extends UnaryLogicalOp {

	// Attributes
	private int numCluster;

	/**
	 * Auto-generated serial by eclipse
	 */
	private static final long serialVersionUID = 1666674260694172414L;

	// The default constructor is required as instances of logical operators are created by newInstance().
	public VectorquantizationAO() {
		super();
	}

	// Dieser Konstruktor wird aufgerufen, wenn clone() benutzt wird
	public VectorquantizationAO(VectorquantizationAO vqAO) {
		// super(vqAO);
		this.numCluster = vqAO.numCluster;
	}

	// Clone must call the copy constructor and the copy constructor must call the super copy constructor!
	@Override
	public AbstractLogicalOperator clone() {
		return new VectorquantizationAO(this);
	}

	@Parameter(name = "numcluster", type = IntegerParameter.class)
	public void setAlgorithmus(int numCluster) {
		this.numCluster = numCluster;
		System.out.println("debug VQAO: " + numCluster);
	}

	// Finally, the class needs setters and getters for the parameter it should keep
	public int getNumCluster() {
		return this.numCluster;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {

		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		// SDFAttribute attributeType = new SDFAttribute(null, "datatype",
		// SDFDatatype.STRING);
		// attributes.add(attributeType);

		SDFAttribute attributeId = new SDFAttribute(null, "clusterID", SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeId);

		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		return outSchema;
	}

}
