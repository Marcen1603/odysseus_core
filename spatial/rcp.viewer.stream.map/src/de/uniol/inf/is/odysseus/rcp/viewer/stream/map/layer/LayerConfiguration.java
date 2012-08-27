package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

public class LayerConfiguration {

	private boolean raster = true;
	private String name = null;

	public boolean isRaster() {
		return raster;
	}

	public void setRaster(boolean raster) {
		this.raster = raster;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
