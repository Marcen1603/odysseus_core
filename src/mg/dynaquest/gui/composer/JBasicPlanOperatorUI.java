package mg.dynaquest.gui.composer;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.9 $
 Log: $Log: JBasicPlanOperatorUI.java,v $
 Log: Revision 1.9  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.8  2004/09/16 08:53:53  grawund
 Log: *** empty log message ***
 Log:
 Log: Revision 1.7  2004/08/25 08:54:31  grawund
 Log: POState ILLEGAL in ERROR umbenannt
 Log:
 Log: Revision 1.6  2003/05/09 12:53:00  grawund
 Log: ?
 Log:
 Log: Revision 1.5  2002/02/20 15:51:44  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.4  2F002/02/06 14:02:13  grawund
 Log: Einbindung beliebiger Araneus-Konverter moeglich
 Log:
 Log: Revision 1.3  2002/02/05 13:37:28  grawund
 Log: [no comments]
 Log:
 Log: Revision 1.2  2002/01/31 16:14:42  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.beans.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.plaf.*;
import mg.dynaquest.monitor.*;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class JBasicPlanOperatorUI extends JPlanOperatorUI implements
		MouseListener, MouseMotionListener, PropertyChangeListener {

	/**
	 * @uml.property  name="popup"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final JPopupMenu popup = new JPopupMenu();

	/**
	 * @uml.property  name="runAction"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final RunPOAction runAction = new RunPOAction("Run");

	/**
	 * @uml.property  name="run2Action"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final RunPOAction run2Action = new RunPOAction("Run (w/o close)");

	/**
	 * @uml.property  name="openAction"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final RunPOAction openAction = new RunPOAction("Open");

	/**
	 * @uml.property  name="nextAction"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final RunPOAction nextAction = new RunPOAction("Next");

	/**
	 * @uml.property  name="closeAction"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final RunPOAction closeAction = new RunPOAction("Close");

	//final InterruptPOAction interruptAction = new InterruptPOAction();

	static final Color blockedColor = new Color(244, 108, 112);

	static final Color blockedTextColor = Color.black;

	static final Color illegalColor = new Color(255, 0, 0);

	static final Color illegalTextColor = Color.white;

	static final Color workingColor = new Color(132, 228, 124);

	static final Color workingTextColor = new Color(0, 0, 0);

	static final Color inactiveColor = new Color(230, 230, 230);

	static final Color inactiveTextColor = new Color(150, 150, 150);

	static final Color initializedColor = new Color(253, 244, 140);

	static final Color initializedTextColor = new Color(0, 0, 0);

	static final Color finishedColor = new Color(213, 234, 255);

	static final Color finishedTextColor = new Color(0, 0, 0);

	static final Color shadowColor = new Color(150, 150, 150);

	static final Color topTextColor = Color.black;

	static final Color topColor = Color.white;

	// Fuer die Auswertung der Maus-Events
	/**
	 * @uml.property  name="press"
	 */
	Point press = new Point();

	/**
	 * @uml.property  name="dragging"
	 */
	boolean dragging = false;

	// Aber was ich auf jeden Fall brauche, ist eine Fabrik-Methode
	public static ComponentUI createUI(JComponent c) {
		// Später kann man sich hier überlegen, ob man nur eine
		// Referenz zurückliefert (wie bei den Motif-Buttons) aber
		// im Moment lasse ich das erst mal so
		return new JBasicPlanOperatorUI();
	}

	public void installUI(JComponent c) {
		JPlanOperator po = (JPlanOperator) c;
		po.setActiveWorkerThread(null);
		po.addMouseListener(this);
		po.addMouseMotionListener(this);
		po.addPropertyChangeListener(this);
		popup.add(new InspectAction());
		popup.add(runAction);
		popup.add(run2Action);
		popup.add(openAction);
		popup.add(nextAction);
		popup.add(closeAction);

		//popup.add(this.interruptAction);
		//interruptAction.setEnabled(false);
		super.installUI(c);
	}

	public void uninstallUI(JComponent c) {
		JPlanOperator po = (JPlanOperator) c;
		po.removePropertyChangeListener(this);
		po.removeMouseListener(this);
		po.removeMouseMotionListener(this);
		super.uninstallUI(c);
	}

	public Dimension getMaximumSize(JComponent c) {
		// ACHTUNG HACK ____>> TODO!!!!!!
		return new Dimension(100 + 30, 100);
	}

	public Dimension getMinimumSize(JComponent c) {
		return getMaximumSize(c);
	}

	public Dimension getPreferredSize(JComponent c) {
		return getMaximumSize(c);
	}

	private void drawConnector(Color drawingColor, int x, int y, int width,
			int height, Graphics2D g) {

		g.setColor(shadowColor);
		g.fillRect(x + 1, y + 1, width, height);
		g.setColor(drawingColor);
		g.fillRect(x, y, width - 2, height - 2);
		g.setColor(Color.black);
		g.drawRect(x, y, width - 2, height - 2);
	}

	public void paint(Graphics g_old, JComponent c) {
		Graphics2D g = (Graphics2D) g_old;
		Color textColor = inactiveTextColor;
		Color poDrawingColor = inactiveColor;

		JPlanOperator po = (JPlanOperator) c;
		int portSize = 10;
		int topBlockSize = 25;
		int shadowSize = 3;

		Insets insets = c.getInsets();
		int width = c.getWidth() - insets.left - insets.right - 1 - shadowSize;
		int height = c.getHeight() - insets.top - insets.bottom - shadowSize;
		g.translate(insets.left, insets.top);

		POState poState = null;
		long stateDurationTime = 0;
		String poStateString = "null";
		int poObjectsWritten = 0;
		int poObjectsRead = 0;

		// Damit auch die zusammengehörigen Werte ausgelesen werden
		// bremst das eventuell?
		POStatePDA statePDA = po.getStatePDA();
		synchronized (statePDA) {
			poState = statePDA.getPOState();
			stateDurationTime = statePDA.getStateDurationTime();
			poStateString = statePDA.getPOStateString();
			poObjectsWritten = statePDA.getObjectsWritten();
			poObjectsRead = statePDA.getObjectsRead();
		}

		//if (poState == POState.INACTIVE || poState == POState.ILLEGAL){
		//  interruptAction.setEnabled(false);
		//}else{
		//  interruptAction.setEnabled(true);
		//}
		// Die Farbe ermitteln, in der der PO gezeichnet werden
		// soll. Um ein Flimmer zu verhindern, sind einige
		// Zustände nur im stabilen Zustand gezeichnet (Zeit>1s)
		if ((poState == POState.READ_BLOCKED || poState == POState.WRITE_BLOCKED)
				&& stateDurationTime > 1000) {
			poDrawingColor = blockedColor;
			textColor = blockedTextColor;
		} else if (poState == POState.INACTIVE) {
			poDrawingColor = inactiveColor;
			textColor = inactiveTextColor;
		} else if (poState == POState.ERROR) {
			poDrawingColor = illegalColor;
			textColor = illegalTextColor;
		} else if (poState == POState.INITIALIZED && stateDurationTime > 1000) {
			poDrawingColor = initializedColor;
			textColor = initializedTextColor;
		} else if (poState == POState.FINISHED) {
			poDrawingColor = finishedColor;
			textColor = finishedTextColor;
		} else {
			poDrawingColor = workingColor;
			textColor = workingTextColor;
		}

		// Zuerst! der Output-Port
		drawConnector(poDrawingColor, 0 + (width / 2) - portSize / 2, 0,
				portSize, portSize + 2, g);
		// und der Komponente mitteilen
		po.setConnectionOutPoint(new Point(width / 2, portSize / 2));

		// Dann die Box

		// Schatten
		g.setColor(shadowColor);
		g.fillRoundRect(0 + shadowSize, 0 + shadowSize + portSize, width,
				height - 2 * portSize, 5, 5);
		g.setColor(poDrawingColor);
		g.fillRoundRect(0, 0 + portSize, width, (height - 2 * portSize), 5, 5);
		// und noch einen schwarzen Kasten rundherum
		g.setColor(Color.black);
		g.drawRoundRect(0, 0 + portSize, width, height - 2 * portSize, 5, 5);
		// Neu, jetzt mit Box oben
		g.setColor(topColor);
		g.fillRoundRect(1, 1 + portSize, width - 1, portSize + topBlockSize
				- 10, 5, 5);
		g.fillRect(1, Math.round(0 + portSize + topBlockSize / 2), width - 1,
				Math.round(portSize + topBlockSize / 2 - 8));
		//g.setColor(Color.black);
		//g.drawRoundRect(0,0+portSize,width,portSize+topBlockSize-10,5,5);

		// und in Felder unterteilen
		//    g.drawLine(0,portSize+topBlockSize,width,portSize+topBlockSize);

		// Sowie "Knubbel" die Input-Ports darstellen
		int noOfInputs = po.getNumberOfInputs();
		int space = 0;
		int aktPos = 0;
		if (noOfInputs > 0) {
			space = (width - (noOfInputs * portSize)) / noOfInputs;
			// erste Position ist auf der linken Seiten, mit
			// halben Abstand
			aktPos = space / 2;
		}

		for (int i = 0; i < noOfInputs; i++) {
			//System.out.println("aktPos "+aktPos+" space "+space+" width
			// "+width);
			// Im Moment weiss ich leider nicht, wie ich das mit
			// den blockierten Inputs realisieren soll ...
			//      if (i==blockedInput){
			//        g.setColor(blockedColor);
			//      }else{
			//        g.setColor(workingColor);
			//      }
			drawConnector(poDrawingColor, aktPos, height - portSize, portSize,
					portSize, g);
			// Die Position des Port der Komponente mitteilen (Mitte des Ports);
			po.setConnectionInPoint(i, new Point(aktPos + portSize / 2, height
					- portSize / 2));

			// nach rechts gehen, mit der Breite des Ports und dem
			// Abstand
			aktPos = aktPos + portSize + space;
		}

		// if (poState == POState.INACTIVE){
		//      g.setColor(inactiveColor);
		//    }else{
		//      g.setColor(blockedColor);
		//    }

		g.setColor(topTextColor);
		// Jetzt fehlt noch der Name
		String name = po.getPOName();
		// mittig im oberen Feld plazieren
		Font font = g.getFont();
		g.setFont(font.deriveFont(Font.BOLD));

		Rectangle2D fontRect = font.getStringBounds(name, g
				.getFontRenderContext());
		// ich geh jetzt erst mal davon aus, dass der Text kleiner ist,
		// als der Rahmen ... spaeter eventuell die Groesse des Rahmens anhand
		// des Textes erzeugen (getPreferredSize())
		long fontY = portSize + (topBlockSize / 2)
				+ java.lang.Math.round(fontRect.getHeight())
				+ java.lang.Math.round(fontRect.getY()) + 2;
		long fontX = (width - java.lang.Math.round(fontRect.getWidth())) / 2;
		//System.out.println("StringPos:"+fontX+" "+fontY);
		g.drawString(name, fontX, fontY);

		g.setColor(textColor);
		g.setFont(font.deriveFont(Font.PLAIN));
		String text = "";

		// Den Text nur bei einem stabilen Zustand ausgeben ... ansonsten
		// ist der Nutzen gleich 0
		if (stateDurationTime > 1000) {
			text = poStateString;
			fontRect = font.getStringBounds(text, g.getFontRenderContext());
			fontX = (width - java.lang.Math.round(fontRect.getWidth())) / 2;
			g.drawString(text, fontX, fontY + topBlockSize - 5);
		}

		text = "w: " + String.valueOf(poObjectsWritten);
		fontRect = font.getStringBounds(text, g.getFontRenderContext());
		fontX = (width - java.lang.Math.round(fontRect.getWidth())) / 2;
		g.drawString(text, fontX, fontY
				+ java.lang.Math.round(1.4 * topBlockSize));

		if (noOfInputs > 0) {
			text = "r: " + String.valueOf(poObjectsRead);
			fontRect = font.getStringBounds(text, g.getFontRenderContext());
			fontX = (width - java.lang.Math.round(fontRect.getWidth())) / 2;
			g.drawString(text, fontX, fontY + 2 * topBlockSize);
		}

		//    poIcon.paintIcon(c,g,width,height);

	}

	/**
	 * @return  the dragging
	 * @uml.property  name="dragging"
	 */
	public boolean isDragging() {
		return dragging;
	}

	public void mouseClicked(MouseEvent event) {
		if ((event.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) {
			dragging = false;
			popup.show(event.getComponent(), event.getX(), event.getY());
		}
	}

	public void mousePressed(MouseEvent event) {
		if ((event.getModifiers() & MouseEvent.BUTTON1_MASK) > 0) {
			press.x = event.getX();
			press.y = event.getY();
			dragging = true;
		}
	}

	public void mouseReleased(MouseEvent event) {
		dragging = false;
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseDragged(MouseEvent event) {
		Component c = (Component) event.getSource();
		if (dragging) {
			Point loc = c.getLocation();
			Point pt = new Point();
			pt.x = event.getX() + loc.x - press.x;
			pt.y = event.getY() + loc.y - press.y;
			c.setLocation(pt.x, pt.y);
			Container parent = c.getParent();
			parent.repaint();
		}
	}

	public void mouseMoved(MouseEvent event) {
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("activeWorkerThread")) {
			runAction.setEnabled(event.getNewValue() == null);
			//interruptAction.setEnabled(event.getNewValue() != null);
		}
	}

}

class InspectAction extends AbstractAction {

	//private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5886606555236850547L;

	public InspectAction() {
		super("Inspect");
	}

	// Erst mal hier, später in eine eigene Klasse auslagern
	public void showInspectScreen1(JPlanOperator po)
			throws IntrospectionException {
		TriggeredPlanOperator tpo = po.getModel().getRealPO();

		//try{
		Class tpoClass = tpo.getClass();
		BeanInfo info = Introspector.getBeanInfo(tpoClass);
		PropertyDescriptor[] propsDesc = info.getPropertyDescriptors();

		JFrame f = new JFrame("Inspektor-Fenster fuer " + po.getPOName());
		f.setBounds(200, 200, 500, propsDesc.length * 40);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		f.getContentPane().setLayout(gridbag);
		c.fill = GridBagConstraints.HORIZONTAL;

		//       f.getContentPane().setLayout(new BorderLayout());
		for (int i = 0; i < propsDesc.length; i++) {
			JLabel label = new JLabel(propsDesc[i].getDisplayName());
			c.weightx = 0.3;
			c.gridx = 0;
			c.gridy = i;
			gridbag.setConstraints(label, c);
			f.getContentPane().add(label);

			// Erst mal als Hack immer ein TextFeld
			JTextField textField = null;
			// Erst mal ;-)
			//Class propClass = propsDesc[i].getPropertyType();
			//          System.out.println("-------------------------------------");
			//          System.out.println("Eigenschaft:
			// "+propsDesc[i].getDisplayName());
			//          System.out.println("-------------------------------------");
			//          System.out.println(propsDesc[i].getName());
			//          System.out.println(propClass);
			if (propsDesc[i].getReadMethod() != null) {
				Method read = propsDesc[i].getReadMethod();
				Object value = null;
				try {
					//              System.out.println(tpoClass);
					//              System.out.println(read);
					value = read.invoke(tpo, (Object[]) null);
					textField = new JTextField(value.toString());
					textField.setEditable(false);
				} catch (Exception alle) {
					alle.printStackTrace();
				}
				c.weightx = 0.7;
				c.gridx = 1;
				c.gridy = i;
				gridbag.setConstraints(textField, c);
				f.getContentPane().add(textField);
			}
			if (propsDesc[i].getWriteMethod() != null) {
				//            System.out.println("Write: "+propsDesc[i].getWriteMethod());
				// Immer wenn es eine Write-Methode gibt, darf hier
				// auch reingeschrieben werden
				//textField.setEditable(true);
			}

			//Class ed = propsDesc[i].getPropertyEditorClass();
			//if (ed == null){
			// PropertyEditor ed2 = PropertyEditorManager.findEditor(propClass);
			//            System.out.println(ed2);
			//         }else{
			//            System.out.println(ed);
		}
		//}
		f.setVisible(true);
		//f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.repaint();

		// }catch(IntrospectionException e){
		//      System.err.println(e.getMessage());
		//      e.printStackTrace();
		//    }

	}

	public void showInspectScreen2(JPlanOperator po) {
//		TriggeredPlanOperator tpo = po.getModel().getRealPO();
//
//		try {
//			Class tpoClass = tpo.getClass();
//			BeanInfo info = Introspector.getBeanInfo(tpoClass);
//			PropertyDescriptor[] propsDesc = info.getPropertyDescriptors();
//
//			JFrame f = new JFrame("Inspektor-Fenster fuer " + po.getPOName());
//			f.setBounds(200, 200, 500, propsDesc.length * 40);
//
//			//PropertyTable proTable = new PropertyTable(po);
//			//       f.getContentPane().setLayout(new GridLayout(0,1));
//			//     f.getContentPane().add(proTable);
//
//			for (int i = 0; i < propsDesc.length; i++) {
////				Object value = null;
//				Class propClass = propsDesc[i].getPropertyType();
//				if (propsDesc[i].getReadMethod() != null) {
//					Method read = propsDesc[i].getReadMethod();
//					try {
//						value = read.invoke(tpo, (Object[]) null);
//					} catch (Exception alle) {
//						alle.printStackTrace();
//					}
//				}
//				Class ed = propsDesc[i].getPropertyEditorClass();
//				if (ed == null) {
//					PropertyEditor ed2 = PropertyEditorManager
//							.findEditor(propClass);
//					//            System.out.println(ed2);
//					if (ed2 != null) {
//						ed = ed2.getClass();
//					}
//				} else {
//					//            System.out.println(ed);
//				}
//
//				// Nur wenn es überhaupt einen Editor gibt, dann auch eine
//				// Zeile eintragen
//				if (ed != null) {
//					// proTable.addProperty(propsDesc[i].getDisplayName(),value,ed);
//					if (propsDesc[i].getWriteMethod() != null) {
//						// proTable.allowUserPropertyEdit(propsDesc[i].getDisplayName(),true);
//					} else {
//						// proTable.allowUserPropertyEdit(propsDesc[i].getDisplayName(),false);
//					}
//				}
//			}
//			f.setVisible(true);
//			//f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//			f.repaint();
//
//		} catch (IntrospectionException e) {
//			System.err.println(e.getMessage());
//			e.printStackTrace();
//		}

	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		JPlanOperator po = null;

		if (o instanceof AbstractButton) {
			AbstractButton butt = (AbstractButton) o;
			Object parent = butt.getParent();
			if (parent instanceof JPopupMenu) {
				JPopupMenu men = (JPopupMenu) parent;
				// Der Invoker ist immer eine Istanz von JPlanOperator
				po = (JPlanOperator) men.getInvoker();
			}
		}

		if (po != null) {
			try {
				showInspectScreen1(po);
			} catch (Exception ex) {

				ex.printStackTrace();
			}
		}
	}
}

/**
 * @author  Marco Grawunder
 */
class MyPOActionExecutor extends Thread {
	/**
	 * @uml.property  name="po"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	JPlanOperator po = null;

	/**
	 * @uml.property  name="executionMode"
	 */
	String executionMode = "";

	public MyPOActionExecutor(JPlanOperator po, String executionMode) {
		this.po = po;
		this.executionMode = executionMode;
	}

	public void run() {
		//System.out.print("Fuehre Query ab PO "+po.getPOName()+" aus ...
		// Thread: "+po.getName());
		try {
			if (executionMode.equals("Run")) {
				po.execute(true);
			} else if (executionMode.equals("Run (w/o close)")) {
				po.execute(false);
			} else if (executionMode.equals("Open")) {
				po.open();
			} else if (executionMode.equals("Next")) {
				po.next();
			} else if (executionMode.equals("Close")) {
				po.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		po.setActiveWorkerThread(null);
		//System.out.println(" fertig.");
	}
}

abstract class POAction extends AbstractAction {

	public POAction(String text) {
		super(text);
	}

	protected JPlanOperator getJPlanOperator(ActionEvent e) {
		Object o = e.getSource();
		JPlanOperator po = null;

		if (o instanceof AbstractButton) {
			AbstractButton butt = (AbstractButton) o;
			//System.out.println("Gefunden "+butt.getParent());
			Object parent = butt.getParent();
			if (parent instanceof JPopupMenu) {
				JPopupMenu men = (JPopupMenu) parent;
				// Der Invoker ist immer eine Istanz von JPlanOperator
				// System.out.println("Gefunden: "+men.getInvoker());
				po = (JPlanOperator) men.getInvoker();
			}
		}
		return po;
	}

}

class RunPOAction extends POAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7063967693049479527L;
	// private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="methode"
	 */
	private String methode = "";

	public RunPOAction(String methode) {
		super(methode);
		this.methode = methode;
	}

	public void actionPerformed(ActionEvent e) {
		JPlanOperator po = super.getJPlanOperator(e);
		if (po != null) {
			try {
				//model.getRealPO().start();
				MyPOActionExecutor exec = new MyPOActionExecutor(po, methode);
				exec.start();
				po.setActiveWorkerThread(exec);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}

