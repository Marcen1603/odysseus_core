package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

/**
 * This does nothing more than to show, that this RasterLayer is an heatmap.
 * 
 * Makes it possible to add a heatmap via the "New Layer"-dialog.
 * 
 * @author Tobias Brandt
 * 
 */
public class HeatmapLayerConfiguration extends RasterLayerConfiguration {

	private static final long serialVersionUID = -7225910571344993841L;

	private String query;
	
	public HeatmapLayerConfiguration(RasterLayerConfiguration toCopy) {
		super(toCopy);
		setQuery("");
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

}
