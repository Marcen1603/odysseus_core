package de.uniol.inf.is.odysseus.priority.postpriorisation.event;

import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.priority.PostPriorisationPO;
import de.uniol.inf.is.odysseus.priority.PriorityPO;

public class PostPriorisationEventListener implements IPOEventListener {

	private PostPriorisationPO<?> postPriorisationPO;
	
	public PostPriorisationEventListener(PostPriorisationPO<?> postPO,
			PriorityPO<?> prioPO) {
		this.postPriorisationPO = postPO;
	}

	@Override
	public void poEventOccured(POEvent poEvent) {
		postPriorisationPO.setActive(false);
	}

}
