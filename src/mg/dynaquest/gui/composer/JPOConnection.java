package mg.dynaquest.gui.composer;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.4 $
 Log: $Log: JPOConnection.java,v $
 Log: Revision 1.4  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.3  2004/09/16 08:53:53  grawund
 Log: *** empty log message ***
 Log:
 Log: Revision 1.2  2002/01/31 16:14:55  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//import javax.swing.border.*;

/**
 * @author  Marco Grawunder
 */
public class JPOConnection extends JComponent implements ChangeListener,
		ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1223886998609494368L;
	//private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="model"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPOConnectionModel model;

	public JPOConnection(JPlanOperator source, JPlanOperator destination,
			int destinationPortNo) {
		init(new JPOConnectionModel(), source, destination, destinationPortNo);
	}

	protected void init(JPOConnectionModel m, JPlanOperator source,
			JPlanOperator destination, int destinationPortNo) {
		model = m;
		model.setSource(source);
		model.setDestination(destination, destinationPortNo);
		destination.addComponentListener(this);
		source.addComponentListener(this);
		model.addChangeListener(this);
		updateUI();
	}

	public void setUI(JPlanOperatorUI ui) {
		super.setUI(ui);
	}

	public void updateUI() {
		setUI((JPOConnectionUI) UIManager.getUI(this));
		invalidate();
	}

	public String getUIClassID() {
		return "mg.dynaquest.composer.JPOConnectionUI";
	}

	/**
	 * @param model  the model to set
	 * @uml.property  name="model"
	 */
	public void setModel(JPOConnectionModel m) {
		JPOConnectionModel old = model;

		if (m == null) {
			model = new JPOConnectionModel();
		} else {
			model = m;
		}

		firePropertyChange("model", old, model);
	}

	/**
	 * @return  the model
	 * @uml.property  name="model"
	 */
	protected JPOConnectionModel getModel() {
		return model;
	}

	public void stateChanged(ChangeEvent e) {
		// Hier noch auswerten
		repaint();
	}

	// Methoden zum Zugriff
	public Point getSourceConnectionPoint() {
		return model.getSource().getConnectionOutPoint();
	}

	public Point getDestinationConnectionPoint() {
		return model.getDestination().getConnectionInPoint(
				model.getDestinationPortNo());
	}

	public void componentResized(ComponentEvent p0) {
		repaint();
	}

	public void componentMoved(ComponentEvent p0) {
		repaint();
	}

	public void componentShown(ComponentEvent p0) {
		repaint();
	}

	public void componentHidden(ComponentEvent p0) {
		//  this.setVisible(true);
	}

}