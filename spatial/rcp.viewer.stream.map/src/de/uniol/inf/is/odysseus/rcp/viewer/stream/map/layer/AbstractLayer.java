package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public abstract class AbstractLayer implements ILayer {

	//protected BoundingBox boundingBox;
	protected String name;
	protected String srid;
	
//	@Override
//	public BoundingBox getBoundingBox() {
//		return boundingBox;
//	}
//
//	@Override
//	public void setBoundingBox(BoundingBox boundingBox) {
//		this.boundingBox = boundingBox;;
//	}
	
	@Override
	public String getName() {
	 return name;
	}

	public String getSrid() {
		return srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}
	
	public Style getStyle() {
		return null;
	}
	
}
