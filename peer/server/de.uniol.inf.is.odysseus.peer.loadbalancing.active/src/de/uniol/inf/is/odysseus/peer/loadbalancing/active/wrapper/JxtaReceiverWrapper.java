package de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
/***
 * Wrapper Class for JxtaReceiverPO to allow calling protected Functions from the outside.
 * @author Carsten Cordes
 *
 */
@SuppressWarnings("rawtypes")
public class JxtaReceiverWrapper<T extends IStreamObject> extends
		JxtaReceiverPO<T> {

	
	
	/**
	 * Constructor from Physical JxtaReceiver
	 * @param po Operator
	 */
	public JxtaReceiverWrapper(JxtaReceiverPO<T> po) {
		super(po);
	}

	
	/**
	 * Constructor from Logical JxtaReceiver
	 * @param ao Operator
	 * @throws DataTransmissionException
	 */
	public JxtaReceiverWrapper(JxtaReceiverAO ao)
			throws DataTransmissionException {
		super(ao);
	}

	/**
	 * Wrapper Function for process_close()
	 */
	@Override
	public void process_close() {
		super.process_close();
	}

	/**
	 * Wrapper Function for process_open()
	 */
	@Override
	public void process_open() {
		super.process_open();
	}

}
