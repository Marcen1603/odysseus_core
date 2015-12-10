package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.style.PersistentStyle;

public class VectorLayerConfiguration extends LayerConfiguration{

	
	private static final long serialVersionUID = -8907504034146280649L;
	
	private String query = null;
	private String attribute = null;
	private int maxTupleCount = -1;
	
	private PersistentStyle style = null;

	public VectorLayerConfiguration(String layerName) {
		super(layerName);
		// TODO Auto-generated constructor stub
	}

	public VectorLayerConfiguration(VectorLayerConfiguration toCopy) {
		super(toCopy);
		this.query = toCopy.query;
		this.attribute = toCopy.attribute;
		this.maxTupleCount = toCopy.maxTupleCount;
		this.style = toCopy.style;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the maxTupleCount
	 */
	public int getMaxTupleCount() {
		return maxTupleCount;
	}

	/**
	 * @param maxTupleCount the maxTupleCount to set
	 */
	public void setMaxTupleCount(int maxTupleCount) {
		this.maxTupleCount = maxTupleCount;
	}

	/**
	 * @return the style
	 */
	public PersistentStyle getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(PersistentStyle style) {
		this.style = style;
	}
	
}
