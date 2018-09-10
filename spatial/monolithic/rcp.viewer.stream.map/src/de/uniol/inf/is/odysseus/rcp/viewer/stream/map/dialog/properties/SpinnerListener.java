package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;

/**
 * A listener for a spinner in the TreeListener, so we can change the given LayerConfiguration
 * if the spinner is changed
 * @author Tobias Brandt
 *
 */
public abstract class SpinnerListener extends SelectionAdapter {
	
	LayerConfiguration layerConfig;
	Spinner spinner;
	 MapPropertiesDialog mapPropertiesDialog;
	
	public SpinnerListener(LayerConfiguration layerConfiguration, Spinner spinner, MapPropertiesDialog mapPropertiesDialog) {
		this.layerConfig = layerConfiguration;
		this.spinner = spinner;
		this.mapPropertiesDialog = mapPropertiesDialog;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		// To Override
	}
	
}
