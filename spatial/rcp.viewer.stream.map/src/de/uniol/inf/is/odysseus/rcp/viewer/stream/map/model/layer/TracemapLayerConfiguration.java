package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;

/**
 * Configuration for the tracemap with a bit more than a rasterLayerConfiguration
 * 
 * Makes it possible to add a linemap via the "New Layer"-dialog.
 * 
 * @author Tobias Brandt
 * 
 */
public class TracemapLayerConfiguration extends RasterLayerConfiguration {

	private static final long serialVersionUID = -7982545440178333614L;

	private String query;
	private HashMap<Integer, Color> colors;
	private int numOfLineElements;
	private boolean autoTransparency;
	
	public TracemapLayerConfiguration(String name) {
		super(name);
		setQuery("");
		colors = new HashMap<Integer, Color>();
		numOfLineElements = 10;
		autoTransparency = true;
	}
	
	public TracemapLayerConfiguration(TracemapLayerConfiguration toCopy) {
		// TODO: Maybe better a full copy, but what is the envelope (coverageGeographic)
		super(toCopy.getName());
		setQuery(toCopy.getQuery());
		setColors(toCopy.getColors());
		setNumOfLineElements(toCopy.getNumOfLineElements());
		setAutoTransparency(toCopy.isAutoTransparency());
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}

	public HashMap<Integer, Color> getColors() {
		return colors;
	}

	/**
	 * Set all colors for all lines
	 * @param colors
	 */
	public void setColors(HashMap<Integer, Color> colors) {
		this.colors = colors;
	}
	
	/**
	 * Get the color for the line with the id "id"
	 * @param id
	 * @return
	 */
	public Color getColorForId(int id) {
		return colors.get(id);
	}
	
	/**
	 * Set the color for the line with the id "id"
	 * @param id
	 * @param color
	 */
	public void setColorForId(int id, Color color) {
		colors.put(new Integer(id), color);
	}

	public int getNumOfLineElements() {
		return numOfLineElements;
	}

	public void setNumOfLineElements(int numOfLineElements) {
		this.numOfLineElements = numOfLineElements;
	}

	/**
	 * If autoTransparency is on, the number of elements to show will
	 * be ignored and all line-elements will be visible, with
	 * auto transparency (smooth transparency to the older elements)
	 * @return
	 */
	public boolean isAutoTransparency() {
		return autoTransparency;
	}

	public void setAutoTransparency(boolean autoTransparency) {
		this.autoTransparency = autoTransparency;
	}
}
