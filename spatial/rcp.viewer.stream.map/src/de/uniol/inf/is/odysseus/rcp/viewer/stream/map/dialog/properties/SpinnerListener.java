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
	TreeListener treeListener;
	
	public SpinnerListener(LayerConfiguration layerConfiguration, Spinner spinner, TreeListener treeListener) {
		this.layerConfig = layerConfiguration;
		this.spinner = spinner;
		this.treeListener = treeListener;
	}
	
	public void widgetSelected(SelectionEvent e) {
		// To Override
	}
	
}
