package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public interface IPictogram {
	
	public void activate();
	public void deactivate();
	public boolean isActivated();
	public Point getPosition();
	public void setPosition(Point position);
	public void init(Composite parent);

}
