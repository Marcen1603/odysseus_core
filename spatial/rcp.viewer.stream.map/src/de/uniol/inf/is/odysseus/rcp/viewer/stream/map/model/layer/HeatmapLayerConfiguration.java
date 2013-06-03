package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer;

/**
 * This does nothing more than to show,
 * that this RasterLayer is an heatmap.
 * 
 * Makes it possible to add a heatmap via
 * the "New Layer"-dialog.
 * @author Tobias Brandt
 *
 */
public class HeatmapLayerConfiguration extends RasterLayerConfiguration {

	public HeatmapLayerConfiguration(RasterLayerConfiguration toCopy) {
		super(toCopy);
	}
	
	public HeatmapLayerConfiguration(String name) {
		super(name);
	}

}
