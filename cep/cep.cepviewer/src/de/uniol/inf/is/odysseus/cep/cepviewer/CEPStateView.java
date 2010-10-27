package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

/**
 * This class defines the state view which should hold the information of the
 * selected state.
 * 
 * @author Christian
 */
public class CEPStateView extends ViewPart {

	// The id of this view.
	public static final String ID = "MyView.stateView";

	/**
	 * This is the constructor.
	 */
	public CEPStateView() {
		super();
	}

	/**
	 * This method creates the state view.
	 * 
	 * @param parent
	 *            is the widget which contains the state view.
	 */
	@Override
	public void createPartControl(Composite parent) {
		// just show its name.
		Label label = new Label(parent, SWT.NONE);
		label.setText("StateView");
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	@Override
	public void setFocus() {
	}

}
