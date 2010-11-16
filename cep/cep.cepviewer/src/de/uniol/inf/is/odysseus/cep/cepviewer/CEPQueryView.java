package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.event.CEPViewAgent;
import de.uniol.inf.is.odysseus.cep.cepviewer.event.CEPViewEvent;
import de.uniol.inf.is.odysseus.cep.cepviewer.event.ICEPViewListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;

/**
 * This class defines the query view which should hold the information of the
 * selected query.
 * 
 * @author Christian
 */
public class CEPQueryView extends ViewPart {

	// The id of this view.
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.queryview";

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
		final Label label = new Label(parent, SWT.NONE);
		label.setText("QueryView");
		CEPViewAgent.getInstance().addCEPEventListener(new ICEPViewListener() {
			public void cepEventOccurred(CEPViewEvent event) {
				if (event.getType() == CEPViewEvent.ITEM_SELECTED) {
					label.setText("" + ((StateMachineInstance) event.getContent()).getInstanceId());
				}
			}
		});
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	@Override
	public void setFocus() {
	}

}
