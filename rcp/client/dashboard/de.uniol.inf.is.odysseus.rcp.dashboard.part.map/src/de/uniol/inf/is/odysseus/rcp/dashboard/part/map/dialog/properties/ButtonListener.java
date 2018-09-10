package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;

public abstract class ButtonListener extends SelectionAdapter {

	LayerConfiguration layerConfiguration;
	AbstractMapPropertiesDialog propertiesDialog;
	Button correspondingButton;

	public ButtonListener(LayerConfiguration layerConfiguration, AbstractMapPropertiesDialog propertiesDialog,
			Button correspondingButton) {
		this.layerConfiguration = layerConfiguration;
		this.propertiesDialog = propertiesDialog;
		this.correspondingButton = correspondingButton;
	}

	@Override
	public abstract void widgetSelected(SelectionEvent e);
}
