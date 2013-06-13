package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

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
	private int alpha;
	private boolean interpolation;
	private boolean autoPosition;
	private int numTilesWidth;
	private int numTilesHeight;
	
	public HeatmapLayerConfiguration(HeatmapLayerConfiguration toCopy) {
		// TODO: Maybe better a full copy, but what is the envelope (coverageGeographic)
		super(toCopy.getName());
		super.setSrid(4326); //???
		setQuery(toCopy.getQuery());
		setGeometricAttributePosition(toCopy.getGeometricAttributePosition()); //Here should be a point
		setMinColor(toCopy.getMinColor());
		setMaxColor(toCopy.getMaxColor());
		setAlpha(toCopy.getAlpha());
		setInterpolation(toCopy.isInterpolation());
		setNumTilesHeight(toCopy.getNumTilesHeight());
		setNumTilesWidth(toCopy.getNumTilesWidth());
		setAutoPosition(toCopy.isAutoPosition());
	}
	
	public HeatmapLayerConfiguration(RasterLayerConfiguration toCopy) {
		// TODO: Maybe better a full copy, but what is the envelope (coverageGeographic)
		super(toCopy.getName());
		super.setSrid(4326); //???
		setQuery("");
		setGeometricAttributePosition(0); //Here should be a point
		setMinColor(new Color(Display.getDefault(),0,255,0));
		setMaxColor(new Color(Display.getDefault(),255,0,0));
		setAlpha(50);
		setInterpolation(false);
		setNumTilesHeight(10);
		setNumTilesWidth(10);
		setAutoPosition(true);
	}
	
	public HeatmapLayerConfiguration(String name) {
		super(name);
		super.setSrid(4326); //???
		setQuery("");
		setGeometricAttributePosition(0); //Here should be a point
		setMinColor(new Color(Display.getDefault(),0,255,0));
		setMaxColor(new Color(Display.getDefault(),255,0,0));
		setAlpha(50);
		setInterpolation(false);
		setNumTilesHeight(10);
		setNumTilesWidth(10);
		setAutoPosition(true);
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

	public int getAlpha() {
		return alpha;
	}

	/**
	 * Sets the value which controls, how transparent the layer is
	 * 0 = transparent; 255 = opaque
	 * @param alpha
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public boolean isInterpolation() {
		return interpolation;
	}

	/**
	 * Defines, whether the tiles should be strict divided (clear lines, false)
	 * or if they are drawn with interpolation (true)
	 * @param interpolation
	 */
	public void setInterpolation(boolean interpolation) {
		this.interpolation = interpolation;
	}

	public int getNumTilesWidth() {
		return numTilesWidth;
	}

	public void setNumTilesWidth(int numTilesWidth) {
		this.numTilesWidth = numTilesWidth;
	}

	public int getNumTilesHeight() {
		return numTilesHeight;
	}

	public void setNumTilesHeight(int numTilesHeight) {
		this.numTilesHeight = numTilesHeight;
	}

	public boolean isAutoPosition() {
		return autoPosition;
	}

	public void setAutoPosition(boolean autoPosition) {
		this.autoPosition = autoPosition;
	}

}
