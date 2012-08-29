package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

public class LayerConfiguration {

	private int layerType = 0;
	private String name = null;
	private int attributePosition = 0;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAttributePosition() {
		return attributePosition;
	}

	public void setAttributePosition(int attributePosition) {
		this.attributePosition = attributePosition;
	}

	public int getLayerType() {
	    return layerType;
    }

	public void setLayerType(int layerType) {
	    this.layerType = layerType;
    }

}
