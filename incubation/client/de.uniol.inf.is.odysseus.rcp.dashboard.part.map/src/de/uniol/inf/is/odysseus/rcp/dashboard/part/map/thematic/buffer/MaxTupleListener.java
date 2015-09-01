package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.buffer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.LayerUpdater;

public class MaxTupleListener extends SelectionAdapter{
	
	LayerUpdater connection;
	Spinner spinner;
	
	public MaxTupleListener(LayerUpdater connection, Spinner spinner) {
		this.connection = connection;
		this.spinner = spinner;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		connection.setMaxPufferSize(spinner.getSelection());
		connection.checkForPufferSize();
	}
}