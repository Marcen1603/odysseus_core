package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;

public abstract class ButtonListener extends SelectionAdapter{

	LayerConfiguration layerConfiguration;
	TreeListener treeListener;
	Button correspondingButton;
	
	public ButtonListener(LayerConfiguration layerConfiguration, TreeListener treeListener, Button correspondingButton) {
		this.layerConfiguration = layerConfiguration;
		this.treeListener = treeListener;
		this.correspondingButton = correspondingButton;
	}
	
	public abstract void widgetSelected(SelectionEvent e);
}
