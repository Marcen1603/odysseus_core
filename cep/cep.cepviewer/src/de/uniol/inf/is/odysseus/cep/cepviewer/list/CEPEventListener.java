package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.exception.NoStateMachineInstanceException;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPStatus;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.epa.event.CEPEvent;
import de.uniol.inf.is.odysseus.cep.epa.event.ICEPEventListener;

public class CEPEventListener implements ICEPEventListener {

	private CEPListView view;

	public CEPEventListener(CEPListView view) {
		this.view = view;
	}

	public void cepEventOccurred(CEPEvent event) {
		final Object content = event.getContent();
		if (content instanceof StateMachineInstance) {
			switch (event.getType()) {
			case CEPEvent.ADD_MASCHINE:
				if (this.view.getNormalList().isFull()) {
					break;
				}
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
				try{
				this.view.getSite().getShell().getDisplay()
						.asyncExec(new Runnable() {
							public void run() {
								try{
								view.selectionChanged((StateMachineInstance<?>) content);
								view.getStatusList().getTree().refresh();
								}catch(Exception e) {
									e.printStackTrace();
								}
							}
						});
				}catch(Exception e) {
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
			this.view.getSite().getShell().getDisplay()
					.asyncExec(new Runnable() {
						public void run() {
							view.getNormalList().getTree().refresh();
							view.getQueryList().getTree().refresh();
							view.getStatusList().getTree().refresh();
						}
					});
			this.view.setInfoData();
		} else {
			throw new NoStateMachineInstanceException();
		}
	}

}
