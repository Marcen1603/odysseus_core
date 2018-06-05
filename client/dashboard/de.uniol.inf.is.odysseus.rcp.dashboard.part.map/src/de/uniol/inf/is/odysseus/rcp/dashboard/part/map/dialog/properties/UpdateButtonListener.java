package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.AbstractLayer;

public abstract class UpdateButtonListener extends SelectionAdapter {

	AbstractLayer<?> layer;

	public UpdateButtonListener(AbstractLayer<?> layer) {
		this.layer = layer;
	}

	@Override
	abstract public void widgetSelected(SelectionEvent e);
}