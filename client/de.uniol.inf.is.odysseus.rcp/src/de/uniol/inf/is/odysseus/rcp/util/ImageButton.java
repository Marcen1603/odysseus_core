package de.uniol.inf.is.odysseus.rcp.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import java.util.Objects;
import com.google.common.base.Strings;

public final class ImageButton {

	private final Button button;
	
	public ImageButton( Composite parent, Image image, String tooltipText ) {
		Objects.requireNonNull(parent, "parent must not be null!");
		Objects.requireNonNull(image, "image must not be null!");
		
		button = new Button(parent, SWT.PUSH);
		button.setImage(image);
		if( !Strings.isNullOrEmpty(tooltipText) ) {
			button.setToolTipText(tooltipText);
		}
	}
	
	public ImageButton( Composite parent, Image image ) {
		this(parent, image, null);
	}
	
	public Button getButton() {
		return button;
	}
}
