package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

import java.io.Serializable;

public abstract class LayerConfiguration implements Serializable{

	private static final long serialVersionUID = -3897708132891607334L;
    
	private String group = null;
	private String name = null;

	public LayerConfiguration() {
	}
	
	public LayerConfiguration(String layerName) {
		this.name = layerName;
    }
	
	public LayerConfiguration(LayerConfiguration toCopy) {
		this.name = toCopy.name;
		this.group = toCopy.group;		
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
