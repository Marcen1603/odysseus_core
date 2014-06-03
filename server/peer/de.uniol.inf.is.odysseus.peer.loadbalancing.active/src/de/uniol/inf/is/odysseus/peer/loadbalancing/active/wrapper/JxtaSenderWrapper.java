package de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

public class JxtaSenderWrapper<T extends IStreamObject<?>>  extends JxtaSenderPO<T> {

	
	public JxtaSenderWrapper(JxtaSenderAO ao) throws DataTransmissionException {
		super(ao);
	}
	
	public JxtaSenderWrapper(JxtaSenderPO<T> po) {
		super(po);
	}

	/*
	 * Wrapper Function for process_next to be able to send streams from the outside.
	 * @see de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	public void process_next(T object, int port) {
		super.process_next(object, port);
	}

	/*
	 * Wrapper Function for process_done to be able to call method from the outside.
	 * @see de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	public void process_done(int port) {
		super.process_done(port);
	}

}
