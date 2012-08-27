package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public abstract class AbstractLayer implements ILayer {

	protected String name;
	protected String srid;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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

	@Override
	public String getComplexName() {
		return name;
	}
	
	@Override
	public String getQualifiedName() {
		return name;
	}
	
}
