package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ImagePictogram implements IPictogram {

	private Point position = new Point(0, 0);
	private Image theImage;
	private String filename;
	private Label label;
	private boolean activated = false;

	public ImagePictogram(String filename) {
		this.filename = filename;
	}

	@Override
	public void activate() {
		label.setVisible(true);
		activated = true;
	}

	@Override
	public void deactivate() {
		label.setVisible(false);
		activated = false;
	}

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public void setPosition(Point position) {
		this.position = position;
	}

	@Override
	public void init(Composite parent) {
		theImage = new Image(parent.getDisplay(), new ImageData(filename));
		label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_BOTH));
		label.setImage(theImage);

	}

	@Override
	public boolean isActivated() {
		return activated;
	}
	
	@Override
	public String toString() {	
		return "Image ("+this.filename+")";
	}

}
