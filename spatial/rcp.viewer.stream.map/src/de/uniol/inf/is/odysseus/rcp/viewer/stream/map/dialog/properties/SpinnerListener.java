package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;

/**
 * A listener for a spinner in the TreeListener, so we can change the given HeatmapLayerConfiguration
 * if the spinner is changed
 * @author Tobias Brandt
 *
 */
public class SpinnerListener extends SelectionAdapter {
	
	HeatmapLayerConfiguration heatmapLayerConfig;
	Spinner spinner;
	TreeListener treeListener;
	
	public SpinnerListener(HeatmapLayerConfiguration heatmapLayerConfiguration, Spinner spinner, TreeListener treeListener) {
		this.heatmapLayerConfig = heatmapLayerConfiguration;
		this.spinner = spinner;
		this.treeListener = treeListener;
	}
	
	public void widgetSelected(SelectionEvent e) {
		// To Override
	}
	
}
