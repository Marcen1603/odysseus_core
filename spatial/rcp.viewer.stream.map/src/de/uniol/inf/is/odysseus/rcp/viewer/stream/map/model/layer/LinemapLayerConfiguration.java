package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

/**
 * This does nothing more than to show, that this VectorLayer is a Linemap.
 * 
 * Makes it possible to add a linemap via the "New Layer"-dialog.
 * 
 * @author Tobias Brandt
 * 
 */
public class LinemapLayerConfiguration extends RasterLayerConfiguration {

	private static final long serialVersionUID = -7982545440178333614L;

	private String query;
	
	public LinemapLayerConfiguration(String name) {
		super(name);
		setQuery("");
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
}
