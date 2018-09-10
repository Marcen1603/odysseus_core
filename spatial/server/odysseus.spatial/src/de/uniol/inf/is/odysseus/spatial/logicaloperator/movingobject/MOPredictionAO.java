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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "MovingObjectPrediction", doc = "Enriches the point in time (input 2) with a prediction, where the moving objects will be.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MOPredictionAO extends BinaryLogicalOp {

	private static final int LOCATION_INPUT_PORT = 0;
	// private static final int ESTIMATION_INPUT_PORT = 1;

	private static final long serialVersionUID = -5203604651964484664L;

	private String pointInTimeFutureAttribute;
	private String pointInTimeNowAttribute;
	private String movingObjectListAttribute;
	private String centermovingObjectIdAttribute;

	private String geometryAttribute;
	private String idAttribute;

	private long timeStepSizeMs;

	public MOPredictionAO() {
		super();
	}

	public MOPredictionAO(MOPredictionAO ao) {
		super(ao);
		this.geometryAttribute = ao.getGeometryAttribute();
		this.idAttribute = ao.getIdAttribute();
		this.pointInTimeFutureAttribute = ao.getPointInTimeAttribute();
		this.pointInTimeNowAttribute = ao.getPointInTimeNowAttribute();
		this.movingObjectListAttribute = ao.getMovingObjectListAttribute();
		this.centermovingObjectIdAttribute = ao.getCentermovingObjectIdAttribute();
		this.timeStepSizeMs = ao.getTimeStepSizeMs();
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
		return pointInTimeFutureAttribute;
	}

	@Parameter(name = "pointInTimeFutureAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the point in time to which the moving objects need to be predicted.")
	public void setPointInTimeAttribute(String pointInTimeAttribute) {
		this.pointInTimeFutureAttribute = pointInTimeAttribute;
	}

	public String getPointInTimeNowAttribute() {
		return pointInTimeNowAttribute;
	}

	@Parameter(name = "pointInTimeNowAttribute", optional = true, type = StringParameter.class, isList = false, doc = "Name of the attribute with the point in time to which the moving objects need to be predicted.")
	public void setPointInTimeNowAttribute(String pointInTimeAttribute) {
		this.pointInTimeNowAttribute = pointInTimeAttribute;
	}

	public String getMovingObjectListAttribute() {
		return this.movingObjectListAttribute;
	}

	@Parameter(name = "movingObjectListAttribute", optional = true, type = StringParameter.class, isList = false, doc = "Name of the attribute with the list of the moving objects that need to be predicted.")
	public void setMovingObjectListAttribute(String movingObjectListAttribute) {
		this.movingObjectListAttribute = movingObjectListAttribute;
	}

	public String getCentermovingObjectIdAttribute() {
		return centermovingObjectIdAttribute;
	}

	@Parameter(name = "centerMovingObjectIdAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the id in the time input port..")
	public void setCenterMovingObjectIdAttribute(String centermovingObjectIdAttribute) {
		this.centermovingObjectIdAttribute = centermovingObjectIdAttribute;
	}

	public long getTimeStepSizeMs() {
		return timeStepSizeMs;
	}

	@Parameter(name = "timeStepSizeMs", optional = true, type = LongParameter.class, isList = false, doc = "If you predict a trajectory, this is the step size used between the predicted points.")
	public void setTimeStepSizeMs(long timeStepSizeMs) {
		this.timeStepSizeMs = timeStepSizeMs;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MOPredictionAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		// Use old schema from the input
		SDFSchema inputSchema = getInputSchema(LOCATION_INPUT_PORT);

		// Add the attributes
		List<SDFAttribute> attributes = new ArrayList<>();
		SDFAttribute attribute1 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "movingObjectId",
				SDFDatatype.STRING);
		SDFAttribute attribute2 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "location",
				SDFSpatialDatatype.SPATIAL_POINT);
		SDFAttribute attribute3 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "speedMetersPerSecond",
				SDFDatatype.DOUBLE);
		SDFAttribute attribute4 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "direction",
				SDFDatatype.DOUBLE);
		SDFAttribute attribute5 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "centerMovingObjectId",
				SDFDatatype.STRING);
		SDFAttribute attribute6 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "centerLocation",
				SDFSpatialDatatype.SPATIAL_POINT);
		SDFAttribute attribute7 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "predictionTime",
				SDFDatatype.POINT_IN_TIME);

		attributes.add(attribute1);
		attributes.add(attribute2);
		attributes.add(attribute3);
		attributes.add(attribute4);
		attributes.add(attribute5);
		attributes.add(attribute6);
		attributes.add(attribute7);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
