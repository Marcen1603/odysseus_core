package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Configuration for the tracemap with a bit more than a
 * rasterLayerConfiguration
 * 
 * Makes it possible to add a linemap via the "New Layer"-dialog.
 * 
 * @author Tobias Brandt
 * 
 */
public class TracemapLayerConfiguration extends RasterLayerConfiguration {

	private static final long serialVersionUID = -7982545440178333614L;

	private String query;
	private HashMap<Integer, RGB> colors; // Save RGB and not color, cause Color
											// is not serializable
	private int numOfLineElements;
	private boolean autoTransparency;
	private boolean markEndpoint;
	private int lineWidth;
	private int geometricAttributePosition;
	private int valueAttributePosition;

	public TracemapLayerConfiguration(String name) {
		super(name);
		super.setSrid(4326);
		setQuery("");
		colors = new HashMap<Integer, RGB>();
		numOfLineElements = 10;
		autoTransparency = true;
		lineWidth = 3;
		geometricAttributePosition = 0;
		valueAttributePosition = 1;
		markEndpoint = false;
		super.setCoverageGeographic(-180, 180, -85, 85);
	}

	public TracemapLayerConfiguration(TracemapLayerConfiguration toCopy) {
		// (coverageGeographic)
		super(toCopy.getName());
		super.setSrid(toCopy.getSrid());
		setQuery(toCopy.getQuery());
		setColors(toCopy.getColors());
		setNumOfLineElements(toCopy.getNumOfLineElements());
		setAutoTransparency(toCopy.isAutoTransparency());
		setLineWidth(toCopy.getLineWidth());
		setGeometricAttributePosition(toCopy.getGeometricAttributePosition());
		setValueAttributePosition(toCopy.getValueAttributePosition());
		setMarkEndpoint(toCopy.isMarkEndpoint());
		super.setCoverageGeographic(toCopy.getCoverage().getMinX(), toCopy
				.getCoverage().getMaxX(), toCopy.getCoverage().getMinY(),
				toCopy.getCoverage().getMaxY());
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public HashMap<Integer, Color> getColors() {
		// RGB to Color, cause we need Color, but Color it not serializable
		HashMap<Integer, Color> returnColors = new HashMap<Integer, Color>();
		for (Integer key : colors.keySet()) {
			RGB tempRGB = colors.get(key);
			returnColors.put(key, new Color(Display.getDefault(), tempRGB));
		}
		return returnColors;
	}

	/**
	 * Set all colors for all lines
	 * 
	 * @param colors
	 */
	public void setColors(HashMap<Integer, Color> newColors) {

		HashMap<Integer, RGB> newColorMap = new HashMap<Integer, RGB>();
		for (Integer key : newColors.keySet()) {
			Color tempColor = newColors.get(key);
			RGB tempRGB = new RGB(tempColor.getRed(), tempColor.getGreen(),
					tempColor.getBlue());
			newColorMap.put(key, tempRGB);
		}

		this.colors = newColorMap;
	}

	/**
	 * Get the color for the line with the id "id"
	 * 
	 * @param id
	 * @return
	 */
	public Color getColorForId(int id) {
		RGB tempRGB = colors.get(id);
		if (tempRGB != null)
			return new Color(Display.getDefault(), tempRGB);
		return null;
	}

	/**
	 * Set the color for the line with the id "id"
	 * 
	 * @param id
	 * @param color
	 */
	public void setColorForId(int id, Color color) {
		colors.put(id,
				new RGB(color.getRed(), color.getGreen(), color.getBlue()));
	}

	public int getNumOfLineElements() {
		return numOfLineElements;
	}

	public void setNumOfLineElements(int numOfLineElements) {
		this.numOfLineElements = numOfLineElements;
	}

	/**
	 * If autoTransparency is on, the number of elements to show will be ignored
	 * and all line-elements will be visible, with auto transparency (smooth
	 * transparency to the older elements)
	 * 
	 * @return
	 */
	public boolean isAutoTransparency() {
		return autoTransparency;
	}

	public void setAutoTransparency(boolean autoTransparency) {
		this.autoTransparency = autoTransparency;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getGeometricAttributePosition() {
		return geometricAttributePosition;
	}

	public void setGeometricAttributePosition(int geometricAttributePosition) {
		this.geometricAttributePosition = geometricAttributePosition;
	}

	/**
	 * Decides, if a marker (circle) is drawn at the newest position of the line
	 * 
	 * @return
	 */
	public boolean isMarkEndpoint() {
		return markEndpoint;
	}

	/**
	 * Decides, if a marker (circle) is drawn at the newest position of the line
	 * 
	 * @param markEndpoint
	 */
	public void setMarkEndpoint(boolean markEndpoint) {
		this.markEndpoint = markEndpoint;
	}

	public int getValueAttributePosition() {
		return valueAttributePosition;
	}

	public void setValueAttributePosition(int valueAttributePosition) {
		this.valueAttributePosition = valueAttributePosition;
	}
}
