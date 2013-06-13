package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

import org.eclipse.swt.graphics.Color;

/**
 * Shows, that this RasterLayer is an heatmap. (Makes it possible to add a heatmap via the "New Layer"-dialog)
 * Has some additional configurations for the heatmap.
 * @author Tobias Brandt
 * 
 */
public class HeatmapLayerConfiguration extends RasterLayerConfiguration {

	private static final long serialVersionUID = -7225910571344993841L;

	private String query;
	private int geometricAttributePosition;
	private Color minColor;
	private Color maxColor;
	
	public HeatmapLayerConfiguration(RasterLayerConfiguration toCopy) {
		super(toCopy);
		setQuery("");
		setGeometricAttributePosition(0);
	}
	
	public HeatmapLayerConfiguration(String name) {
		super(name);
	}	

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getGeometricAttributePosition() {
		return geometricAttributePosition;
	}

	public void setGeometricAttributePosition(int geometricAttributePosition) {
		this.geometricAttributePosition = geometricAttributePosition;
	}

	public Color getMinColor() {
		return minColor;
	}

	public void setMinColor(Color minColor) {
		this.minColor = minColor;
	}

	public Color getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(Color maxColor) {
		this.maxColor = maxColor;
	}

}
