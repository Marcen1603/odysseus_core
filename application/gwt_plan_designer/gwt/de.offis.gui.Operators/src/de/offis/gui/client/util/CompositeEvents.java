package de.offis.gui.client.util;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Composite;

public abstract class CompositeEvents extends Composite {
	
	protected EventBus events;
	
	public CompositeEvents(EventBus events) {
		this.events = events;
	}
}
