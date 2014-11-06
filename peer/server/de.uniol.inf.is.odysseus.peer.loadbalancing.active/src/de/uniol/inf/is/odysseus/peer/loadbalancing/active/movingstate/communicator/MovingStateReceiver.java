package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionManager;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiverListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol.IStateReceivedListener;

public class MovingStateReceiver implements ITransmissionReceiverListener {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateReceiver.class);
	
	private final ITransmissionReceiver transmission;
	
	private ArrayList<IStateReceivedListener> listener = new ArrayList<IStateReceivedListener>();
	private String pipe;
	private Serializable receivedState;
	private boolean receivingFinished = false;
	
	public void resetFinished() {
		this.receivingFinished = false;
	}
	
	public MovingStateReceiver(String peerID, String pipeID) throws DataTransmissionException {
		
		transmission = DataTransmissionManager.getInstance().registerTransmissionReceiver(peerID, pipeID);
		transmission.addListener(this);
		transmission.open();
		transmission.sendOpen();
		this.pipe = pipeID;
	}
	
	public void notifyListeners() {
		for(IStateReceivedListener l : listener) {
			if(l!=null) {
				l.stateReceived(pipe);
			}
		}
	}
	
	public void addListener(IStateReceivedListener listener) {
		if(!this.listener.contains(listener))
			this.listener.add(listener);
	}
	
	public boolean hasStatus() {
		return this.receivedState!=null;
	}
	
	@Override
	public void onReceiveData(ITransmissionReceiver receiver, byte[] data) {
		if(!receivingFinished) {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInput in = null;
			
			try {
			  in = new ObjectInputStream(bis);
			  receivedState = (Serializable)in.readObject();
			  receivingFinished = true;
			  LOG.debug("Received: ");
			  LOG.debug(receivedState.toString());
			  notifyListeners();
			} catch (IOException e) {
				LOG.error("Error while deserializing bytes.");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				LOG.error("Class not found.");
				e.printStackTrace();
			} finally {
			  try {
			    bis.close();
			  } catch (IOException ex) {
			    // ignore close exception
			  }
			  try {
			    if (in != null) {
			      in.close();
			    }
			  } catch (IOException ex) {
			    // ignore close exception
			  }
			} 
		}
	}

	@Override
	public void onReceivePunctuation(ITransmissionReceiver receiver,
			IPunctuation punc) {
		//ignore as we are not an operator
		
	}
	
	@SuppressWarnings("rawtypes")
	public void injectState(IStatefulPO operator) throws LoadBalancingException {
		if(receivedState==null) {
			throw new LoadBalancingException("Tried injecting State without having state received.");
		}
		if(operator instanceof IPipe) {
			IPipe physicalOp = (IPipe)operator;
			for(IOperatorOwner owner : physicalOp.getOwner()) {
				physicalOp.suspend(owner);
			}
			operator.setState(receivedState);
			for(IOperatorOwner owner : physicalOp.getOwner()) {
				physicalOp.resume(owner);
			}
		}
		else if (operator instanceof ISink) {
			ISink physicalOp = (ISink)operator;
			for(IOperatorOwner owner : physicalOp.getOwner()) {
				physicalOp.suspend(owner);
			}
			operator.setState(receivedState);
			for(IOperatorOwner owner : physicalOp.getOwner()) {
				physicalOp.resume(owner);
			}
		}
		//TODO Source has no suspend method.
	}

	@Override
	public void onReceiveDone(ITransmissionReceiver receiver) {
		
		
	}
}
