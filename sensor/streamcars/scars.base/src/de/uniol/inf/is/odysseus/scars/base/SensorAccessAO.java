package de.uniol.inf.is.odysseus.scars.base;

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class SensorAccessAO extends AccessAO {
	
	private static final long serialVersionUID = 1L;
	private String objectListPath = "";
	
	
	public String getObjectListPath() {
		return this.objectListPath;
	}
	
	public void setObjectListPath(String objectListPath) {
		this.objectListPath = objectListPath;
	}
	
	public SensorAccessAO(SDFSource sdfSource) {
		super(sdfSource);
	}

	public SensorAccessAO(SensorAccessAO sensorAccessAO) {
		super(sensorAccessAO);
		this.objectListPath = sensorAccessAO.objectListPath;
	}	

	@Override
	public SensorAccessAO clone() {
		return new SensorAccessAO(this);
	}
}
