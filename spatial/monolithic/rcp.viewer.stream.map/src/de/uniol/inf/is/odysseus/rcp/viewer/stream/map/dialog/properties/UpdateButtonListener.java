package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.AbstractLayer;

public abstract class UpdateButtonListener extends SelectionAdapter{

	
	AbstractLayer<?> layer;
	
	public UpdateButtonListener(AbstractLayer<?> layer) {
		this.layer = layer;
	}	
	
	@Override
	abstract public void widgetSelected(SelectionEvent e);
}