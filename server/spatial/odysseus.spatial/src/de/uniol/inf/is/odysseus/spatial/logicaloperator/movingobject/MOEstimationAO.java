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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "MovingObjectEstimation", doc = "Enriches the point in time (input 2) with an estimation, which moving objects need to be predicted.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MOEstimationAO extends BinaryLogicalOp {
	
	private static final int LOCATION_INPUT_PORT = 0;
	private static final int TIMER_INPUT_PORT = 1;

	private static final long serialVersionUID = -7299499368889774372L;
	private String geometryAttribute;
	private String idAttribute;
	private String pointInTimeAttribute;
	private String centerMovingObjectAttribute;

	public MOEstimationAO() {
		super();
	}

	public MOEstimationAO(MOEstimationAO ao) {
		super(ao);
		this.geometryAttribute = ao.getGeometryAttribute();
		this.idAttribute = ao.getIdAttribute();
		this.pointInTimeAttribute = ao.getPointInTimeAttribute();
		this.centerMovingObjectAttribute = ao.getCenterMovingObjectAttribute();
	}

	public String getGeometryAttribute() {
		return geometryAttribute;
	}

	@Parameter(name = "geometryAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the geometry of the moving object.")
	public void setGeometryAttribute(String geometryAttribute) {
		this.geometryAttribute = geometryAttribute;
	}

	public String getIdAttribute() {
		return idAttribute;
	}

	@Parameter(name = "idAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the id of the moving object.")
	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}

	public String getPointInTimeAttribute() {
		return pointInTimeAttribute;
	}

	@Parameter(name = "pointInTimeAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the point in time to which the moving objects need to be predicted.")
	public void setPointInTimeAttribute(String pointInTimeAttribute) {
		this.pointInTimeAttribute = pointInTimeAttribute;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MOEstimationAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		// Use old schema from the input
		SDFSchema inputSchema = getInputSchema(LOCATION_INPUT_PORT);

		// Add the attributes
		List<SDFAttribute> attributes = new ArrayList<>();
		SDFAttribute attribute1 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "PointInTime", SDFDatatype.TIMESTAMP);
		SDFAttribute attribute2 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "movingObjectId", SDFDatatype.LONG);
		SDFAttribute attribute3 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "movingObjectIds", SDFDatatype.LIST);

		attributes.add(attribute1);
		attributes.add(attribute2);
		attributes.add(attribute3);
		
		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

	public String getCenterMovingObjectAttribute() {
		return centerMovingObjectAttribute;
	}

	@Parameter(name = "centerMovingObjectAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the center id.")
	public void setCenterMovingObjectAttribute(String centerMovingObjectAttribute) {
		this.centerMovingObjectAttribute = centerMovingObjectAttribute;
	}

}
