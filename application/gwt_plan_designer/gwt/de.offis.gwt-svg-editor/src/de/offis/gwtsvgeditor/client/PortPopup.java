package de.offis.gwtsvgeditor.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class PortPopup extends PopupPanel{
	public PortPopup(String message) {
		super(true, false);
		
		setWidget(new Label(message));
	}
}
