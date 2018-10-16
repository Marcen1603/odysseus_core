package de.uniol.inf.is.odysseus.rcp.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.base.Preconditions;

public final class ClickableImage {

	private final Label label;
	
	private IImageClickHandler handler;
	
	public ClickableImage( Composite parent, Image image, IImageClickHandler hdl) {
		// Preconditions.checkNotNull(parent, "parent must not be null!");
		// Preconditions.checkNotNull(image, "image must not be null!");

		handler = hdl;
		
		label = new Label(parent, SWT.NONE);
		label.setImage(image);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if( e.button == 1 && handler != null) {
					handler.onClick();
				}
			}
		});
	}
	
	public ClickableImage( Composite parent, Image image ) {
		this(parent, image, null);
	}
	
	public Label getLabel() {
		return label;
	}
	
	public void setClickHandler(IImageClickHandler handler) {
		this.handler = handler;
	}
	
	public IImageClickHandler getClickHandler() {
		return handler;
	}
}
