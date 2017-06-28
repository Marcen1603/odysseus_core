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
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "MovingObjectPrediction", doc = "Enriches the point in time (input 2) with a prediction, where the moving objects will be.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MOPredictionAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -5203604651964484664L;

	private String pointInTimeAttribute;
	private String movingObjectListAttribute;

	private String geometryAttribute;
	private String idAttribute;
	private String courseOverGroundAttribute;
	private String speedOverGroundAttribute;

	public MOPredictionAO() {
		super();
	}

	public MOPredictionAO(MOPredictionAO ao) {
		super(ao);
		this.geometryAttribute = ao.getGeometryAttribute();
		this.idAttribute = ao.getIdAttribute();
		this.pointInTimeAttribute = ao.getPointInTimeAttribute();
		this.movingObjectListAttribute = ao.getMovingObjectListAttribute();
		this.courseOverGroundAttribute = ao.getCourseOverGroundAttribute();
		this.speedOverGroundAttribute = ao.getSpeedOverGroundAttribute();
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

	public String getMovingObjectListAttribute() {
		return this.movingObjectListAttribute;
	}

	@Parameter(name = "movingObjectListAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the list of the moving objects that need to be predicted.")
	public void setMovingObjectListAttribute(String movingObjectListAttribute) {
		this.movingObjectListAttribute = movingObjectListAttribute;
	}

	public String getCourseOverGroundAttribute() {
		return this.courseOverGroundAttribute;
	}

	@Parameter(name = "courseOverGroundAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the course over ground.")
	public void setCourseOverGroundAttribute(String courseOverGroundAttribute) {
		this.courseOverGroundAttribute = courseOverGroundAttribute;
	}

	public String getSpeedOverGroundAttribute() {
		return this.speedOverGroundAttribute;
	}

	@Parameter(name = "speedOverGroundAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the speed over ground.")
	public void setSpeedOverGroundAttribute(String speedOverGroundAttribute) {
		this.speedOverGroundAttribute = speedOverGroundAttribute;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MOPredictionAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		
		// Use old schema from the input
		SDFSchema inputSchema = getInputSchema(pos);

		// Add the attributes
		List<SDFAttribute> attributes = new ArrayList<>();
		SDFAttribute attribute1 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "movingObjectId", SDFDatatype.STRING);
		SDFAttribute attribute2 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "location", SDFSpatialDatatype.SPATIAL_POINT);
		SDFAttribute attribute3 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "speedMetersPerSecond", SDFDatatype.STRING);
		SDFAttribute attribute4 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "direction", SDFDatatype.STRING);
		
		attributes.add(attribute1);
		attributes.add(attribute2);
		attributes.add(attribute3);
		attributes.add(attribute4);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
