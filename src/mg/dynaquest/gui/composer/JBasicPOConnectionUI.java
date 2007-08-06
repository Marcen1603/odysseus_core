package mg.dynaquest.gui.composer;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.5 $
 Log: $Log: JBasicPOConnectionUI.java,v $
 Log: Revision 1.5  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.4  2004/09/16 08:53:53  grawund
 Log: *** empty log message ***
 Log:
 Log: Revision 1.3  2002/02/20 15:51:46  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.2  2002/01/31 16:14:44  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;

//import javax.swing.border.*;

public class JBasicPOConnectionUI extends JPOConnectionUI {

	/**
	 * @uml.property  name="wideStroke"
	 */
	Stroke wideStroke = new BasicStroke((float) 2.0);

	/**
	 * @uml.property  name="initDimension"
	 */
	Dimension initDimension = new Dimension(1000, 1000);

	// Aber was ich auf jeden Fall brauche, ist eine Fabrik-Methode
	public static ComponentUI createUI(JComponent c) {
		// Später kann man sich hier überlegen, ob man nur eine
		// Referenz zurückliefert (wie bei den Motif-Buttons) aber
		// im Moment lasse ich das erst mal so
		return new JBasicPOConnectionUI();
	}

	public void installUI(JComponent c) {
		super.installUI(c);
	}

	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
	}

	public Dimension getMaximumSize(JComponent c) {
		JPOConnection conn = (JPOConnection) c;
		Point dest = conn.getDestinationConnectionPoint();
		Point source = conn.getSourceConnectionPoint();

		if (dest != null && source != null) {
			return new Dimension((int) Math.abs(Math.round(dest.getX()
					- source.getX())), (int) Math.abs(Math.round(dest.getY()
					- source.getY())));
		} else {
			return initDimension;
		}
	}

	public Dimension getMinimumSize(JComponent c) {
		return getMaximumSize(c);
	}

	public Dimension getPreferredSize(JComponent c) {
		return getMaximumSize(c);
	}

	public void paint(Graphics g_old, JComponent c) {
		// System.out.println("Paint von POConnection");
		Graphics2D g = (Graphics2D) g_old;
		JPOConnection conn = (JPOConnection) c;
		Point dest = conn.getDestinationConnectionPoint();
		Point source = conn.getSourceConnectionPoint();
		if (dest != null && source != null) {
			g.setStroke(wideStroke);
			g.draw(new Line2D.Double(source.getX(), source.getY(), dest.getX(),
					dest.getY()));
		}
	}

}