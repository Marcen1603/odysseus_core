package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;

public abstract class ButtonListener extends SelectionAdapter{

	HeatmapLayerConfiguration heatmapLayerConfiguration;
	TreeListener treeListener;
	Button correspondingButton;
	
	public ButtonListener(HeatmapLayerConfiguration heatmapLayerConfiguration, TreeListener treeListener, Button correspondingButton) {
		this.heatmapLayerConfiguration = heatmapLayerConfiguration;
		this.treeListener = treeListener;
		this.correspondingButton = correspondingButton;
	}
	
	public abstract void widgetSelected(SelectionEvent e);
}
