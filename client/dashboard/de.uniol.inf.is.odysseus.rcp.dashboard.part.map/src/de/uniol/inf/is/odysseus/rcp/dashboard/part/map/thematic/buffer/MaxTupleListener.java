package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.buffer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;

public class MaxTupleListener extends SelectionAdapter{
	
	Buffer connection;
	Spinner spinner;
	
	public MaxTupleListener(Buffer connection, Spinner spinner) {
		this.connection = connection;
		this.spinner = spinner;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		connection.setMaxPufferSize(spinner.getSelection());
		connection.checkForBufferSize();
	}
}
