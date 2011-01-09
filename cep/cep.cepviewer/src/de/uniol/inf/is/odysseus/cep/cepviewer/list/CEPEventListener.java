package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
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
		Object content = event.getContent();
		switch (event.getType()) {
		case CEPEvent.ADD_MASCHINE:
			// if a new Instance should be added
			if (content instanceof StateMachineInstance) {
				CEPInstance instance = new CEPInstance((StateMachineInstance<?>) content);
				this.view.getNormalList().addToTree(instance);
				this.view.getQueryList().addToTree(instance);
				this.view.getStatusList().addToTree(instance);
			}
			break;
		case CEPEvent.CHANGE_STATE:
			// if a new Instance should be added
			if (content instanceof StateMachineInstance) {
				this.view.getNormalList().stateChanged(
						(StateMachineInstance<?>) content);
				this.view.getQueryList().stateChanged(
						(StateMachineInstance<?>) content);
				this.view.getStatusList().stateChanged(
						(StateMachineInstance<?>) content);
			}
			break;
		case CEPEvent.MACHINE_ABORTED:
			if (content instanceof StateMachineInstance) {
				this.view.getStatusList().statusChanged(
						(StateMachineInstance<?>) content, CEPStatus.ABORTED);
				this.view.getNormalList().statusChanged(
						(StateMachineInstance<?>) content, CEPStatus.ABORTED);
				this.view.getQueryList().statusChanged(
						(StateMachineInstance<?>) content, CEPStatus.ABORTED);
			}
			break;
		default:
			break;
		}
		this.view.setInfoData();
	}

}
