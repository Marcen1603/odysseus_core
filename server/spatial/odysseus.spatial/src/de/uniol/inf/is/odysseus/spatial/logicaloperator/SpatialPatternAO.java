package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SPATIALPATTERN", doc = "Detects different spatio(temporal) patterns.", category = {
		LogicalOperatorCategory.PROCESSING })
public class SpatialPatternAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -5328186366559655572L;

	private SpatialPattern spatialPattern;

	// The radius in meters around the given position of the target that is
	// accepted (e.g. the vessel can be 100 m around the target to declare, that
	// the vessel reached the target)
	private double radius;

	// Position of the moving object (not the target but e.g. the vessel)
	private String latMovingName;
	private String lngMovingName;

	// Static target or target via a data stream?
	private boolean movingTarget;
	
	// For a static target
	private double targetLatitude;
	private double targetLongitude;

	// For a moving target
	private String latTargetName;
	private String lngTargetName;

	public SpatialPatternAO() {
		super();
	}

	public SpatialPatternAO(SpatialPatternAO ao) {
		super(ao);
		
		this.spatialPattern = ao.spatialPattern;
		
		this.radius = ao.getRadius();
		
		this.latMovingName = ao.getLatMovingName();
		this.lngMovingName = ao.getLngMovingName();
		
		this.movingTarget = ao.isMovingTarget();
		
		this.targetLatitude = ao.targetLatitude;
		this.targetLongitude = ao.targetLongitude;
		this.latTargetName = ao.getLatTargetName();
		this.lngTargetName = ao.getLngTargetName();
	}

	public SpatialPattern getSpatialPattern() {
		return spatialPattern;
	}
	
	public double getTargetLatitude() {
		return targetLatitude;
	}
	
	@Parameter(type = EnumParameter.class, name = "spatialPattern", optional = false, doc = "The spatial pattern that you want to detect.")
	public void setTrainingMode(SpatialPattern spatialPattern) {
		this.spatialPattern = spatialPattern;
	}
	
	public boolean isMovingTarget() {
		return movingTarget;
	}

	@Parameter(type = BooleanParameter.class, name = "movingTarget", optional = false, doc = "If true, the target is set via a data stream on port 0 with the targetLatitudeAttribute and targetLongitudeAttribute attribute names. If false, a static target is defined with targetLatitude and targetLongitude. (The data streams need to be joined beforehand)")
	public void setMovingTarget(boolean movingTarget) {
		this.movingTarget = movingTarget;
	}

	@Parameter(type = DoubleParameter.class, name = "targetLatitude", optional = true, doc = "The latitude value of the target in case that the target is static.")
	public void setTargetLatitude(double latitude) {
		this.targetLatitude = latitude;
	}

	public double getTargetLongitude() {
		return targetLongitude;
	}
	
	@Parameter(type = DoubleParameter.class, name = "targetLongitude", optional = true, doc = "The longitude value of the target in case that the target is static.")
	public void setTargetLongitude(double longitude) {
		this.targetLongitude = longitude;
	}

	public double getRadius() {
		return radius;
	}

	@Parameter(type = DoubleParameter.class, name = "radius", optional = true, doc = "The radius in meters around the given position of the target that is accepted (e.g. the vessel can be 100 m around the target to declare, that the vessel reached the target)")
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getLatMovingName() {
		return latMovingName;
	}

	@Parameter(type = StringParameter.class, name = "movingLatitudeAttribute", optional = false, doc = "The name of the attribute on the first port which has the latitude for the moving object.")
	public void setLatMovingName(String latMovingName) {
		this.latMovingName = latMovingName;
	}

	public String getLngMovingName() {
		return lngMovingName;
	}

	@Parameter(type = StringParameter.class, name = "movingLongitudeAttribute", optional = false, doc = "The name of the attribute on the first port which has the longitude for the moving object.")
	public void setLngMovingName(String lngMovingName) {
		this.lngMovingName = lngMovingName;
	}

	public String getLatTargetName() {
		return latTargetName;
	}

	@Parameter(type = StringParameter.class, name = "targetLatitudeAttribute", optional = true, doc = "The name of the attribute on the first port which has the latitude for the target object.")
	public void setLatTargetName(String latTargetName) {
		this.latTargetName = latTargetName;
	}

	public String getLngTargetName() {
		return lngTargetName;
	}

	@Parameter(type = StringParameter.class, name = "targetLongitudeAttribute", optional = true, doc = "The name of the attribute on the first port which has the longitude for the moving object.")
	public void setLngTargetName(String lngTargetName) {
		this.lngTargetName = lngTargetName;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SpatialPatternAO(this);
	}

}
