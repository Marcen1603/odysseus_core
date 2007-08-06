package mg.dynaquest.gui.composer;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.4 $
 Log: $Log: Dragger.java,v $
 Log: Revision 1.4  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.3  2002/02/20 15:51:42  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.2  2002/01/31 16:14:40  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.awt.*;
import java.awt.event.*;

/**
 * @author  Marco Grawunder
 */
class Dragger extends MouseAdapter implements MouseMotionListener {

	/**
	 * @uml.property  name="press"
	 */
	Point press = new Point();

	/**
	 * @uml.property  name="dragging"
	 */
	boolean dragging = false;

	public void mousePressed(MouseEvent event) {
		press.x = event.getX();
		press.y = event.getY();
		dragging = true;
	}

	/**
	 * @return  the dragging
	 * @uml.property  name="dragging"
	 */
	public boolean isDragging() {
		return dragging;
	}

	public void mouseReleased(MouseEvent event) {
		dragging = false;
	}

	public void mouseClicked(MouseEvent event) {
		dragging = false;
	}

	public void mouseMoved(MouseEvent event) {
		// ignore
	}

	public void mouseDragged(MouseEvent event) {
		Component c = (Component) event.getSource();
		if (dragging) {
			Point loc = c.getLocation();
//			Rectangle oldPos = c.getBounds();
			Point pt = new Point();
			pt.x = event.getX() + loc.x - press.x;
			pt.y = event.getY() + loc.y - press.y;
			c.setLocation(pt.x, pt.y);
			Container parent = c.getParent();
			parent.repaint();
		}
	}
}