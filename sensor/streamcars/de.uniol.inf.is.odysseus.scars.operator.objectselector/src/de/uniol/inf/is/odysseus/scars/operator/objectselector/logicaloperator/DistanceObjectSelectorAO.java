package de.uniol.inf.is.odysseus.scars.operator.objectselector.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DistanceObjectSelectorAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private Double distanceThresholdYRight;
	private Double distanceThresholdYLeft;
	private Double distanceThresholdXRight;
	private Double distanceThresholdXLeft;
	private String trackedObjectY;
	private String trackedObjectX;
	private String trackedObjectList;

	public DistanceObjectSelectorAO() {
	}

	public DistanceObjectSelectorAO(DistanceObjectSelectorAO distanceObjectSelectorAO) {
		super(distanceObjectSelectorAO);
		this.distanceThresholdYRight = distanceObjectSelectorAO.distanceThresholdYRight;
		this.distanceThresholdYLeft = distanceObjectSelectorAO.distanceThresholdYLeft;
		this.distanceThresholdXRight = distanceObjectSelectorAO.distanceThresholdXRight;
		this.distanceThresholdXLeft = distanceObjectSelectorAO.distanceThresholdXLeft;
		this.trackedObjectY = distanceObjectSelectorAO.trackedObjectY;
		this.trackedObjectX = distanceObjectSelectorAO.trackedObjectX;
		this.trackedObjectList = distanceObjectSelectorAO.trackedObjectList;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public DistanceObjectSelectorAO clone() {
		return new DistanceObjectSelectorAO(this);
	}
	
	public Double getDistanceThresholdYRight() {
		return distanceThresholdYRight;
	}

	public Double getDistanceThresholdYLeft() {
		return distanceThresholdYLeft;
	}

	public Double getDistanceThresholdXRight() {
		return distanceThresholdXRight;
	}

	public Double getDistanceThresholdXLeft() {
		return distanceThresholdXLeft;
	}

	public String getTrackedObjectY() {
		return trackedObjectY;
	}

	public String getTrackedObjectX() {
		return trackedObjectX;
	}

	public String getTrackedObjectList() {
		return trackedObjectList;
	}

	public void setTrackedObjectList(String trackedObjectList) {
		this.trackedObjectList = trackedObjectList;
	}

	public void setTrackedObjectX(String trackedObjectX) {
		this.trackedObjectX = trackedObjectX;
	}

	public void setTrackedObjectY(String trackedObjectY) {
		this.trackedObjectY = trackedObjectY;
	}

	public void setDistanceThresholdXLeft(Double distanceThresholdXLeft) {
		this.distanceThresholdXLeft = distanceThresholdXLeft;
	}

	public void setDistanceThresholdXRight(Double distanceThresholdXRight) {
		this.distanceThresholdXRight = distanceThresholdXRight;
	}

	public void setDistanceThresholdYLeft(Double distanceThresholdYLeft) {
		this.distanceThresholdYLeft = distanceThresholdYLeft;
	}

	public void setDistanceThresholdYRight(Double distanceThresholdYRight) {
		this.distanceThresholdYRight = distanceThresholdYRight;
	}

}
