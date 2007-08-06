package mg.dynaquest.gui.composer;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.5 $
 Log: $Log: JPlanOperator.java,v $
 Log: Revision 1.5  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.4  2004/09/16 08:53:53  grawund
 Log: *** empty log message ***
 Log:
 Log: Revision 1.3  2002/02/06 14:02:15  grawund
 Log: Einbindung beliebiger Araneus-Konverter moeglich
 Log:
 Log: Revision 1.2  2002/01/31 16:14:46  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.TimeoutException;
import javax.swing.*;
import javax.swing.event.*;
import mg.dynaquest.monitor.*;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class JPlanOperator extends JComponent implements ChangeListener,
		PropertyChangeListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3536137201267687708L;

	//private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="model"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPlanOperatorModel model;

	/**
	 * @uml.property  name="connectionOutPoint"
	 */
	private Point connectionOutPoint;

	/**
	 * @uml.property  name="connectionInPoints"
	 */
	private Point[] connectionInPoints;

	// Kann man vielleicht noch um eine Liste erweitern
	/**
	 * @uml.property  name="activeWorkerThread"
	 */
	private Thread activeWorkerThread;

	/**
	 * @param activeWorkerThread  the activeWorkerThread to set
	 * @uml.property  name="activeWorkerThread"
	 */
	public void setActiveWorkerThread(Thread thread) {
		this.activeWorkerThread = thread;
		this.firePropertyChange("activeWorkerThread", null, thread);
	}

	/**
	 * @return  the activeWorkerThread
	 * @uml.property  name="activeWorkerThread"
	 */
	public Thread getActiveWorkerThread() {
		return this.activeWorkerThread;
	}

	/**
	 * @return  the model
	 * @uml.property  name="model"
	 */
	public JPlanOperatorModel getModel() {
		return model;
	}


	public JPlanOperator(String name, TriggeredPlanOperator po) {
		init(new JPlanOperatorModel(), name, po);
	}

	public JPlanOperator(TriggeredPlanOperator po) {
		init(new JPlanOperatorModel(), po.getPOName(), po);
	}

	protected void init(JPlanOperatorModel m, String name,
			TriggeredPlanOperator po) {
		model = m;
		model.setRealPO(po);
		model.setPOName(name);
		model.getStatePDA().reInit();
		if (model.getNumberOfInputs() > 0) {
			connectionInPoints = new Point[model.getNumberOfInputs()];
		}
		model.addChangeListener(this);
		// setMinimumSize(new Dimension(80,80));
		// setPreferedSize(new Dimension(80,80));
		// Ein Timer zum Zeichnen (alle Sekunde einmal zeichenen,
		// da Zustandsänderungen fuer Blockiert nur dann gezeichnet
		// werden sollen, wenn sie laenger als eine Sekunde (z.B.) dauern
		// da es sonst zu oft blinkt. Da ohne timer aber gar nicht mitbekommen
		// werden wuerde, dass die Blockierung andauert ...
		Timer oneSecondTimer = new Timer(1000, this);
		oneSecondTimer.start();

		updateUI();
	}

	public void setUI(JPlanOperatorUI ui) {
		super.setUI(ui);
	}

	public void updateUI() {
		setUI((JPlanOperatorUI) UIManager.getUI(this));
		invalidate();
	}

	public String getUIClassID() {
		return "mg.dynaquest.composer.JPlanOperatorUI";
	}

	/**
	 * @param model  the model to set
	 * @uml.property  name="model"
	 */
	public void setModel(JPlanOperatorModel m) {
		JPlanOperatorModel old = model;

		if (m == null) {
			model = new JPlanOperatorModel();
		} else {
			model = m;
		}

		firePropertyChange("model", old, model);
	}

	public void stateChanged(ChangeEvent e) {
		// Hier noch auswerten
		repaint();
	}

	// Methoden zum Zugriff

	//  public void setState(int state)
	//  public int getState(){
	//    return model.getState();
	//  }

	//  public int getBlockedInput(){
	//    return model.getBlockedInput();
	//  }

	public POStatePDA getStatePDA() {
		return model.getStatePDA();
	}

	public String getPOName() {
		return model.getPOName();
	}

	public void setPOName(String poName) {
		model.setPOName(poName);
	}

	public int getNumberOfInputs() {
		return model.getNumberOfInputs();
	}

	/**
	 * @param connectionOutPoint  the connectionOutPoint to set
	 * @uml.property  name="connectionOutPoint"
	 */
	public void setConnectionOutPoint(Point connectionOutPoint) {
		//System.out.print("setConnectionOutPoint "+connectionOutPoint+" -->
		// ");
		// Den Punkt noch in das aktuelle Koordinaten-System transferieren
		connectionOutPoint.translate(this.getX(), this.getY());
		this.connectionOutPoint = connectionOutPoint;
		// Event feier ... aber nicht das firePropertyChange Event! .. ToDo
		//    System.out.println(this.connectionOutPoint);
	}

	/**
	 * @return  the connectionOutPoint
	 * @uml.property  name="connectionOutPoint"
	 */
	public Point getConnectionOutPoint() {
		return connectionOutPoint;
	}

	public void setConnectionInPoint(int pos, Point point) {
		// Den Punkt noch in das aktuelle Koordinaten-System transferieren
		point.translate(this.getX(), this.getY());
		this.connectionInPoints[pos] = point;
	}

	public Point getConnectionInPoint(int pos) {
		return connectionInPoints[pos];
	}

	/**
	 * @return  the connectionInPoints
	 * @uml.property  name="connectionInPoints"
	 */
	public Point[] getConnectionInPoints() {
		return connectionInPoints;
	}

	// Methoden zum Ausführen von Planoperator-Aktionen
	public void open() throws POException {
		this.getModel().open();
	}

	public void close() throws POException {
		this.getModel().close();
	}

	public void next() throws POException, TimeoutException {
		this.getModel().next();
	}

	public void execute(boolean withClose) throws POException, TimeoutException {
		this.getModel().execute(withClose);
	}

	public void propertyChange(PropertyChangeEvent event) {
		// Hier noch mal was tun ;-)
		if (event.getPropertyName().equals("POName")) {
			this.setPOName((String) event.getNewValue());
		}
	}

	public void actionPerformed(ActionEvent event) {
		//System.out.println(event.getSource());
		if (event.getSource() instanceof Timer) {
			JPlanOperatorModel model = this.getModel();
			if (model != null) {
				// Nur dann neu zeichnen, wenn sich der
				// Status verändert hat und dieser Zustand schon mindestens e
				// eine Sekunde andauert
				POStatePDA statePDA = model.getStatePDA();
				if (statePDA.getStateDurationTime() > 1000) {
					repaint();
				}
			}
		}
	}
}