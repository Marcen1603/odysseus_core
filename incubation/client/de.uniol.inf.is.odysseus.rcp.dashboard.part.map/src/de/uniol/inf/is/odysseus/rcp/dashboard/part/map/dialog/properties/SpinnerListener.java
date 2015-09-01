package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;

/**
 * A listener for a spinner in the TreeListener, so we can change the given LayerConfiguration
 * if the spinner is changed
 * @author Tobias Brandt
 *
 */
public abstract class SpinnerListener extends SelectionAdapter {
	
	LayerConfiguration layerConfig;
	Spinner spinner;
	 HeatmapPropertiesDialog heatmapPropertiesDialog;
	
	public SpinnerListener(LayerConfiguration layerConfiguration, Spinner spinner, HeatmapPropertiesDialog heatmapPropertiesDialog) {
		this.layerConfig = layerConfiguration;
		this.spinner = spinner;
		this.heatmapPropertiesDialog = heatmapPropertiesDialog;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		// To Override
	}
	
}
