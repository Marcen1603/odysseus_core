package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
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
	private int valueAttributePosition;
	private RGB minColor;	// Save RGB and not color, cause Color is not serializable
	private RGB maxColor;
	private int alpha;
	private boolean interpolation;
	private boolean autoPosition;
	private boolean hideWithoutInformation;
	private int numTilesWidth;
	private int numTilesHeight;
	private double latSW; // Latitude Southwest
	private double lngSW;
	private double latNE;
	private double lngNE;
	
	public HeatmapLayerConfiguration(HeatmapLayerConfiguration toCopy) {
		// TODO: Maybe better a full copy, but what is the envelope (coverageGeographic)
		super(toCopy.getName());
		super.setSrid(toCopy.getSrid()); 
		setQuery(toCopy.getQuery());
		setGeometricAttributePosition(toCopy.getGeometricAttributePosition()); //Here should be a point
		setValueAttributePosition(toCopy.getValueAttributePosition());
		setMinColor(toCopy.getMinColor());
		setMaxColor(toCopy.getMaxColor());
		setAlpha(toCopy.getAlpha());
		setInterpolation(toCopy.isInterpolation());
		setHideWithoutInformation(toCopy.isHideWithoutInformation());
		setNumTilesHeight(toCopy.getNumTilesHeight());
		setNumTilesWidth(toCopy.getNumTilesWidth());
		setAutoPosition(toCopy.isAutoPosition());
		setLatNE(toCopy.getLatNE());
		setLngNE(toCopy.getLngNE());
		setLatSW(toCopy.getLatSW());
		setLngSW(toCopy.getLngSW());
		super.setCoverageGeographic(getLngSW(), getLngNE(), getLatSW(), getLatNE());
	}
	
	public HeatmapLayerConfiguration(RasterLayerConfiguration toCopy) {
		// TODO: Maybe better a full copy, but what is the envelope (coverageGeographic)
		super(toCopy.getName());
		super.setSrid(toCopy.getSrid());
		setQuery("");
		setGeometricAttributePosition(0); //Here should be a point
		setValueAttributePosition(1); // Here should be the value
		setMinColor(new Color(Display.getDefault(),0,255,0));
		setMaxColor(new Color(Display.getDefault(),255,0,0));
		setAlpha(50);
		setInterpolation(true);
		setHideWithoutInformation(true);
		setNumTilesHeight(10);
		setNumTilesWidth(10);
		setAutoPosition(true);
		// Round about ... Germany
		setLatNE(54);
		setLngNE(14);
		setLatSW(47);
		setLngSW(7);
		super.setCoverageGeographic(getLngSW(), getLngNE(), getLatSW(), getLatNE());
	}
	
	public HeatmapLayerConfiguration(String name) {
		super(name);
		super.setSrid(4326); //???
		setQuery("");
		setGeometricAttributePosition(0); //Here should be a point
		setValueAttributePosition(1); // Here should be the value
		setMinColor(new Color(Display.getDefault(),0,255,0));
		setMaxColor(new Color(Display.getDefault(),255,0,0));
		setAlpha(150);
		setInterpolation(true);
		setHideWithoutInformation(true);
		setNumTilesHeight(10);
		setNumTilesWidth(10);
		setAutoPosition(true);
		// Round about ... Germany
		setLatNE(54);
		setLngNE(14);
		setLatSW(47);
		setLngSW(7);
		super.setCoverageGeographic(getLngSW(), getLngNE(), getLatSW(), getLatNE());
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
		return new Color(Display.getDefault(), minColor);
	}

	public void setMinColor(Color minColor) {
		this.minColor = new RGB(minColor.getRed(), minColor.getGreen(), minColor.getBlue());
	}

	public Color getMaxColor() {
		return new Color(Display.getDefault(), maxColor);
	}

	public void setMaxColor(Color maxColor) {
		this.maxColor = new RGB(maxColor.getRed(), maxColor.getGreen(), maxColor.getBlue());
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
	
	/**
	 * Returns the Latitude value for the southWest-Point
	 * @return
	 */
	public double getLatSW() {
		return latSW;
	}

	/**
	 * Set the Latitude value for the southWest-Point
	 * @param latSW
	 */
	public void setLatSW(double latSW) {
		this.latSW = latSW;
		super.setCoverageGeographic(getLngSW(), getLngNE(), getLatSW(), getLatNE());
	}

	/**
	 * Returns the Longitude value for the southWest-Point
	 * @return
	 */
	public double getLngSW() {
		return lngSW;
	}

	/**
	 * Set the Longitude value for the southWest-Point
	 * @param lngSW
	 */
	public void setLngSW(double lngSW) {
		this.lngSW = lngSW;
		super.setCoverageGeographic(getLngSW(), getLngNE(), getLatSW(), getLatNE());
	}

	/**
	 * Return the Latitude value for the northEast-Point
	 * @return
	 */
	public double getLatNE() {
		return latNE;
	}

	/**
	 * Set the Latitude value for the northEast-Point
	 * @return
	 */
	public void setLatNE(double latNE) {
		this.latNE = latNE;
		super.setCoverageGeographic(getLngSW(), getLngNE(), getLatSW(), getLatNE());
	}

	/**
	 * Return the Longitude value for the northEast-Point
	 * @return
	 */
	public double getLngNE() {
		return lngNE;
	}

	/**
	 * Set the Latitude value for the northEast-Point
	 * @param lngNE
	 */
	public void setLngNE(double lngNE) {
		this.lngNE = lngNE;
		super.setCoverageGeographic(getLngSW(), getLngNE(), getLatSW(), getLatNE());
	}

	public int getValueAttributePosition() {
		return valueAttributePosition;
	}

	public void setValueAttributePosition(int valueAttributePosition) {
		this.valueAttributePosition = valueAttributePosition;
	}

	public boolean isHideWithoutInformation() {
		return hideWithoutInformation;
	}

	public void setHideWithoutInformation(boolean hideWithoutInformation) {
		this.hideWithoutInformation = hideWithoutInformation;
	}

}
