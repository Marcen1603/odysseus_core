package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

/**
 * This class defines the query view which should hold the information of the
 * selected query.
 * 
 * @author Christian
 */
public class CEPQueryView extends ViewPart {

	// The id of this view.
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.queryview";

	private Label label;
	
	/**
	 * This is the constructor.
	 */
	public CEPQueryView() {
		super();
	}

	/**
	 * This method creates the query view.
	 * 
	 * @param parent
	 *            is the widget which contains the query view.
	 */
	@Override
	public void createPartControl(Composite parent) {
		// just show its name.
		this.label = new Label(parent, SWT.NONE);
		label.setText("QueryView");
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	@Override
	public void setFocus() {
	}
	
	@SuppressWarnings("unchecked")
	public void setContent(StateMachineInstance instance) {
		this.label.setText("" + instance.hashCode());
	}

}
