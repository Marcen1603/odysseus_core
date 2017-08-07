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

/**
 * Predicts a trajectory of a moving object. Trajectory element are predicted
 * with a certain granularity. A list of waypoints is calcualted for every
 * moving object.
 * 
 * @author Tobias Brandt
 *
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "MovingObjectTrajectoryPrediction", doc = "Enriches the point in time (input 2) with a prediction of a trajectory, where the moving objects will move along.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MOTrajectoryPredictionAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 8390952478718119148L;

	private String pointInTimeAttribute;
	private String movingObjectListAttribute;

	private String trajectoryAttribute;
	private String idAttribute;
	private String courseOverGroundAttribute;
	private String speedOverGroundAttribute;

	public MOTrajectoryPredictionAO() {
		super();
	}

	public MOTrajectoryPredictionAO(MOTrajectoryPredictionAO ao) {
		super(ao);
		this.trajectoryAttribute = ao.getTrajectoryAttribute();
		this.idAttribute = ao.getIdAttribute();
		this.pointInTimeAttribute = ao.getPointInTimeAttribute();
		this.movingObjectListAttribute = ao.getMovingObjectListAttribute();
		this.courseOverGroundAttribute = ao.getCourseOverGroundAttribute();
		this.speedOverGroundAttribute = ao.getSpeedOverGroundAttribute();
	}

	public String getTrajectoryAttribute() {
		return trajectoryAttribute;
	}

	@Parameter(name = "geometryAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the geometry of the moving object.")
	public void setTrajectoryAttribute(String trajectoryAttribute) {
		this.trajectoryAttribute = trajectoryAttribute;
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
		return new MOTrajectoryPredictionAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		// Use old schema from the input
		SDFSchema inputSchema = getInputSchema(pos);

		// Add the attributes
		List<SDFAttribute> attributes = new ArrayList<>();
		SDFAttribute attribute1 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "movingObjectId",
				SDFDatatype.STRING);
		SDFAttribute attribute2 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "location",
				SDFSpatialDatatype.LIST_TUPLE);

		attributes.add(attribute1);
		attributes.add(attribute2);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
