package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.exception.NoStateMachineInstanceException;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPStatus;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.epa.event.CEPEvent;
import de.uniol.inf.is.odysseus.cep.epa.event.ICEPEventListener;

/**
 * This class implements the CEPEventListener interface. It recieves a
 * notification if the state of one instance within a CepOperator has changed.
 * 
 * @author Christian
 */
public class CEPEventListener implements ICEPEventListener {

	// the view which holds the lists for the instances
	private CEPListView view;

	// the constructor
	public CEPEventListener(CEPListView view) {
		this.view = view;
	}

	/**
	 * This method is called by the CEPEventAgent of an CepOperator if the state
	 * of one of its instances has changed.
	 * 
	 * @param event
	 *            is the CEPEvent fired by the CEPEventAgent
	 */
	public void cepEventOccurred(CEPEvent event) {
		// get the content object of the event
		final Object content = event.getContent();
		if (content instanceof StateMachineInstance) {
			// if the object is an object of the class StateMachnieInstance
			switch (event.getType()) {
			case CEPEvent.ADD_MASCHINE:
				// if the maximal number of entries within the list is reached
				if (this.view.getNormalList().isFull())
					break;
				// if a new Instance should be added
				CEPInstance newInstance = new CEPInstance(
						(StateMachineInstance<?>) content);
				this.view.getNormalList().addToTree(newInstance);
				this.view.getQueryList().addToTree(newInstance);
				this.view.getStatusList().addToTree(newInstance);
				break;
			case CEPEvent.CHANGE_STATE:
				// if the state of an instance changed
				this.view.getStatusList().stateChanged(
						(StateMachineInstance<?>) content);
				try {
					this.view.getSite().getShell().getDisplay()
							.asyncExec(new Runnable() {
								public void run() {
									view.selectionChanged((StateMachineInstance<?>) content);
									view.getStatusList().getTree().refresh();
								}
							});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case CEPEvent.MACHINE_ABORTED:
				// if an instance was aborted
				this.view.getStatusList().statusChanged(
						(StateMachineInstance<?>) content, CEPStatus.ABORTED);
				break;
			default:
				// do nothing
				break;
			}
			// refresh all lists within the gui
			this.view.getSite().getShell().getDisplay()
					.asyncExec(new Runnable() {
						public void run() {
							view.getNormalList().getTree().refresh();
							view.getQueryList().getTree().refresh();
							view.getStatusList().getTree().refresh();
						}
					});
			// set the info label
			this.view.setInfoData();
		} else {
			// if the object was no instance of the class StateMachineInstance
			throw new NoStateMachineInstanceException();
		}
	}

}
