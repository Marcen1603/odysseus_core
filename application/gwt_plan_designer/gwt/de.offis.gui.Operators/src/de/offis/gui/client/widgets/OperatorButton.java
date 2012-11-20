package de.offis.gui.client.widgets;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

/**
 * Baseclass for a button.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorButton extends PushButton {
	public OperatorButton(ImageResource image, String title, ClickHandler clickhandler) {
		super(new Image(image), clickhandler);
		setTitle(title);
		setStyleName("button");
	}

}
