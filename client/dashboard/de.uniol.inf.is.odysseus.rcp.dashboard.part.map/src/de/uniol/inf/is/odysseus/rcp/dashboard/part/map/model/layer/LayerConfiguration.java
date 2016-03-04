package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer;

import java.io.Serializable;

public abstract class LayerConfiguration implements Serializable {

	private static final long serialVersionUID = -3897708132891607334L;

	private String name = null;

	public LayerConfiguration() {
	}

	public LayerConfiguration(String layerName) {
		this.name = layerName;
	}

	public LayerConfiguration(LayerConfiguration toCopy) {
		this.name = toCopy.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
