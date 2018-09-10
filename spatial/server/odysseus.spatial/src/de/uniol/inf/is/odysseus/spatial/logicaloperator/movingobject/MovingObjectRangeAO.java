package de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MOVINGOBJECTRADIUS", doc = "Puts out all objects in the given range around the given moving object in the given data structure.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MovingObjectRangeAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -1956793493229036126L;
	
	private String dataStructureName;
	private String idAttribute;
	private double radius;

	public MovingObjectRangeAO() {
		super();
	}

	public MovingObjectRangeAO(MovingObjectRangeAO ao) {
		super(ao);
		this.dataStructureName = ao.getDataStructureName();
		this.radius = ao.getRange();
		this.idAttribute = ao.getIdAttribute();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MovingObjectRangeAO(this);
	}
	
	public String getDataStructureName() {
		return dataStructureName;
	}

	@Parameter(name = "dataStructureName", optional = false, type = StringParameter.class, isList = false, doc = "The name of the data structure to search in.")
	public void setDataStructureName(String dataStructureName) {
		this.dataStructureName = dataStructureName;
	}

	public double getRange() {
		return radius;
	}

	@Parameter(name = "radius", optional = false, type = IntegerParameter.class, isList = false, doc = "Radius around the given spatial object in meters.")
	public void setRange(double radius) {
		this.radius = radius;
	}

	public String getIdAttribute() {
		return idAttribute;
	}

	@Parameter(name = "idAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the id of the center object.")
	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		// TODO What is the best way to put the result out?
		
		// Put out the original tuple with an extra field that contains a list
		// with the elements in the range

		// Use old schema from the data structure
		SDFSchema inputSchema = getInputSchema(0);
		List<SDFAttribute> attributes = new ArrayList<>(inputSchema.getAttributes());

		// Add the list of spatial points
		SDFAttribute attr = new SDFAttribute("neighbors", "neighbors", SDFDatatype.STRING);
		attributes.add(attr);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
