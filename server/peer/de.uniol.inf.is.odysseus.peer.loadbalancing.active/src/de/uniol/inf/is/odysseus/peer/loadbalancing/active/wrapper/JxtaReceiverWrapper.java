package de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;

@SuppressWarnings("rawtypes")
public class JxtaReceiverWrapper<T extends IStreamObject> extends
		JxtaReceiverPO<T> {

	public JxtaReceiverWrapper(JxtaReceiverPO<T> po) {
		super(po);
	}

	public JxtaReceiverWrapper(JxtaReceiverAO ao)
			throws DataTransmissionException {
		super(ao);
	}

	@Override
	public void process_close() {
		super.process_close();
	}

	@Override
	public void process_open() {
		super.process_open();
	}

}
