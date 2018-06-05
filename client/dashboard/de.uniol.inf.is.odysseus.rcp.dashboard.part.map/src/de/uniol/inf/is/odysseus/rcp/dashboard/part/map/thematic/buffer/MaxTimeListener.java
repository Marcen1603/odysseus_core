package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.buffer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;

public class MaxTimeListener extends SelectionAdapter{
	
	Buffer connection;
	Spinner hours;
	Spinner minutes;
	Spinner seconds;
	
	public MaxTimeListener(Buffer connection, Spinner hours, Spinner minutes, Spinner seconds) {
		this.connection = connection;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		int newSeconds = seconds.getSelection();
		newSeconds += minutes.getSelection() * 60;
		newSeconds += hours.getSelection() * 60 * 60;
		connection.setTimeRange(newSeconds);
	}
}
