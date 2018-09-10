package de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "MovingObjectEstimation", doc = "Enriches the point in time (input 2) with an estimation, which moving objects need to be predicted.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MOEstimationAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -7299499368889774372L;

	private static final int LOCATION_INPUT_PORT = 0;
	// private static final int TIMER_INPUT_PORT = 1;

	private String geometryAttribute;
	private String idAttribute;
	private String pointInTimeFutureAttribute;
	private String pointInTimeNowAttribute;
	private String centerMovingObjectAttribute;
	private double radius;
	private final OptionMap optionsMap = new OptionMap();
	private List<Option> optionsList;

	public MOEstimationAO() {
		super();
	}

	public MOEstimationAO(MOEstimationAO ao) {
		super(ao);
		this.geometryAttribute = ao.getGeometryAttribute();
		this.idAttribute = ao.getIdAttribute();
		this.pointInTimeFutureAttribute = ao.getPointInTimeAttribute();
		this.pointInTimeNowAttribute = ao.getPointInTimeNowAttribute();
		this.centerMovingObjectAttribute = ao.getCenterMovingObjectAttribute();
		this.radius = ao.getRadius();

		optionsMap.addAll(ao.optionsMap);
		if (ao.optionsList != null) {
			this.optionsList = new ArrayList<>(ao.optionsList);
		}
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

	@Parameter(name = "pointInTimeAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the point in time to which the moving objects need to be predicted.")
	public void setPointInTimeAttribute(String pointInTimeAttribute) {
		this.pointInTimeFutureAttribute = pointInTimeAttribute;
	}

	public String getPointInTimeNowAttribute() {
		return pointInTimeNowAttribute;
	}

	@Parameter(name = "pointInTimeNowAttribute", optional = true, type = StringParameter.class, isList = false, doc = "Name of the attribute with the point in time from which the trajectory needs to be predicted.")
	public void setPointInTimeNowAttribute(String pointInTimeAttribute) {
		this.pointInTimeNowAttribute = pointInTimeAttribute;
	}

	public double getRadius() {
		return radius;
	}

	@Parameter(name = "radius", optional = false, type = DoubleParameter.class, isList = false, doc = "The radius for estimation around the center moving object. E.g. the same radius as the following radius select operator.")
	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	public void setOptions(List<Option> value) {
		for (Option option : value) {
			optionsMap.setOption(option.getName().toLowerCase(), option.getValue());
		}
		optionsList = value;
	}

	public List<Option> getOptions() {
		return optionsList;
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
		SDFAttribute attribute1 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "PointInTime",
				SDFDatatype.TIMESTAMP);
		SDFAttribute attribute2 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "movingObjectId",
				SDFDatatype.LONG);
		SDFAttribute attribute3 = new SDFAttribute(inputSchema.getBaseSourceNames().get(0), "movingObjectIds",
				SDFDatatype.LIST);

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
