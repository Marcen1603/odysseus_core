package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

public class TransformationHelper {
	
	@SuppressWarnings("rawtypes")
	public static boolean isRealSink(IPhysicalOperator op) {
		if(op instanceof JxtaSenderPO)
			return false;
		if(op instanceof ISink) {
			ISink opAsSink = (ISink)op;
			//Op might be only operator (no incoming connections -> no need to further seperate)
			if(opAsSink.getSubscribedToSource().size()<=0) {
				return false;
			}
			else {
				if(op instanceof ISource) {
					ISource opAsSource = (ISource)op;
					if(opAsSource.getSubscriptions().size()==0) {
						return true;
					}
				}
				else {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean hasRealSources(int queryID,IServerExecutor executor) {
		Set<IPhysicalOperator> physicalOps = executor.getExecutionPlan().getQueryById(queryID).getAllOperators();
		for(IPhysicalOperator op : physicalOps) {
			if(isRealSink(op))
				return true;
		}
		return false;
	}
	

	/**
	 * Converts a String to a peer ID.
	 * 
	 * @param peerIDString
	 *            String to convert
	 * @return PeerId (if it exists), null else.
	 */
	public static PeerID toPeerID(String peerIDString) {
		try {
			final URI id = new URI(peerIDString);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}

}
