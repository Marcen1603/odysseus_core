package de.uniol.inf.is.odysseus.peer.loadbalancing.active.wrapper;

import java.util.List;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionSender;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.JxtaBundleSenderPO;

/***
 * Wrapper Class for JxtaSenderPO to allow calling protected Functions from the outside.
 * @author Carsten Cordes
 *
 */
public class JxtaSenderWrapper<T extends IStreamObject<?>>  extends JxtaSenderPO<T> {

	JxtaBundleSenderPO<T> parent;
	
	/**
	 * Constructor from JxtaSenderAO
	 * @param ao Operator
	 * @throws DataTransmissionException
	 */
	public JxtaSenderWrapper(JxtaSenderAO ao) throws DataTransmissionException {
		super(ao);
		this.setOutputSchema(ao.getOutputSchema());
	}
	
	/**
	 * Constructor from JxtaSenderPO
	 * @param po Operator
	 */
	public JxtaSenderWrapper(JxtaSenderPO<T> po) {
		super(po);
		this.setOutputSchema(po.getOutputSchema());
	}
	
	public void setParent(JxtaBundleSenderPO<T> parent) {
		this.parent = parent;
	}

	/**
	 * Wrapper Function for process_next to be able to send streams from the outside.
	 * @see de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	public void process_next(T object, int port) {
		super.process_next(object, port);
	}

	/**
	 * Wrapper Function for process_done to be able to call method from the outside.
	 * @see de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	public void process_done(int port) {
		super.process_done(port);
	}


	/**
	 * Wrapper Function for onReceiveOpen to get right Query ID.
	 * @param sender Transmission.
	 */
	@Override
	public void onReceiveOpen(ITransmissionSender sender) {
		
		super.onReceiveOpen(sender);
		
		Optional<Integer> optQueryID = determineQueryID(parent.getOwner());
		if (optQueryID.isPresent()) {
			int queryID = optQueryID.get();
			ServerExecutorService.getServerExecutor().startQuery(queryID, SessionManagementService.getActiveSession());
		}
	}

	/**
	 * Wrapper Function for onReceiveClose to get right sender
	 * @param sender Transmission
	 */
	@Override
	public void onReceiveClose(ITransmissionSender sender) {
		
		super.onReceiveClose(sender);
		
		Optional<Integer> optQueryID = determineQueryID(parent.getOwner());
		if (optQueryID.isPresent()) {
			int queryID = optQueryID.get();
			ServerExecutorService.getServerExecutor().stopQuery(queryID, SessionManagementService.getActiveSession());
		}
	}
	
	/**
	 * Get Query ID for owner
	 * @param owner Query owner
	 * @return Query ID.
	 */
	private static Optional<Integer> determineQueryID(List<IOperatorOwner> owner) {
		if (owner.isEmpty()) {
			return Optional.absent();
		}
		return Optional.of(owner.get(0).getID());
	}
	
}
