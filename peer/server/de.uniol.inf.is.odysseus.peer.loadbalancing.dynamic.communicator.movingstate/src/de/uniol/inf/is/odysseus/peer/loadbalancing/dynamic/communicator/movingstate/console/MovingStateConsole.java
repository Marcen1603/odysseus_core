package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.movingstate.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.movingstate.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.movingstate.communicator.MovingStateHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.movingstate.communicator.MovingStateManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.movingstate.communicator.MovingStateReceiver;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.movingstate.communicator.MovingStateSender;

/**
 * Console with debug commands for active LoadBalancing.
 * 
 * @author Carsten Cordes
 * 
 */
public class MovingStateConsole implements CommandProvider {
	

	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateConsole.class);


	private static IPeerDictionary peerDictionary;
	
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}
	
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv) {
			peerDictionary=null;
		}
	}

	
	
	@Override
	/**
	 * Outputs a simple Help.
	 */
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Load Balancing: Moving State Communicator Debug Commands---\n");
		sb.append("\n");
		sb.append("    listStatefolOperators <queryID>             			- Lists all statefol Operators in a query (useful to determine order)\n");
		sb.append("    installStateSender <peerID>                          - Installs new MovingStateSender to PeerID\n");
		sb.append("    installStateReceiver <peerID> <pipeID>               - Installs new MovingStateReceiver with peer and pipeID\n");
		sb.append("    sendData <pipeID>                                    - Sends Data to MovingStateSender with pipeID\n");
		sb.append("    testSendState <pipeID> <LocalQueryId>                - Sends state of first StatefulPO in Query to pipe\n");
		sb.append("    testInjectState <pipeID> <LocalQueryId>              - Injects received state from pipe to first statefol Operator in query.\n");
		sb.append("    sendLongString <pipeID>                              - Sends a 100MB big String to test Buffers.\n");
		sb.append("    extractState <LocalQueryId>							- Extracts first StatefulPO State from Query with Query ID\n");
		return sb.toString();
	}
	
	/****
	/**
	 * Lists all Stateful Operators in a particular query.
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _listStatefulOperators(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: listStatefulOperators <queryID>";
		ci.println("Looking for stateful OPs");

		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		List<IStatefulPO> statefulList = MovingStateHelper
				.getStatefulOperatorList(localQueryId);
		int i = 0;
		for (IStatefulPO statefulOp : statefulList) {
			IPhysicalOperator operator = (IPhysicalOperator) statefulOp;
			ci.println("[" + i + "] " + operator.getName());
		}
	}
	

	/**
	 * Manually install State sender (returns a PipeID which can be used to
	 * install State Receiver)
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _installStateSender(CommandInterpreter ci) {

		final String ERROR_USAGE = "Usage: installStateSender <peerName>";
		final String ERROR_NOTFOUND = "Error: No peer found with name ";
		

		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {

			ci.println(ERROR_USAGE);
			return;
		}

		Collection<PeerID> peerIDs = peerDictionary.getRemotePeerIDs();
		for (PeerID peer : peerIDs) {
			if (peerDictionary.getRemotePeerName(peer).equals(peerName)) {
				String newPipe = MovingStateManager.getInstance().addSender(
						peer.toString());
				ci.println("Pipe installed:");
				ci.println(newPipe);
				return;
			}
		}
		ci.println(ERROR_NOTFOUND + peerName);
	}
	

	public void _testSendState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: testSendStatus <pipeID> <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NOTFOUND = "ERROR: No Sender found for pipe:";
		final String ERROR_NO_STATE ="Error: Could not get state.";
		
		String pipeId = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeId)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		IStatefulPO statefulOp = null;

		IPhysicalQuery localQuery = OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(
				localQueryId);
		for (IPhysicalOperator operator : localQuery.getAllOperators()) {
			if (operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO) operator;
				break;
			}
		}

		if (statefulOp == null) {
			ci.println(ERROR_LOCALOP);
			return;
		}
		IPhysicalOperator physicalOp = (IPhysicalOperator)statefulOp;
		Serializable state = null;
		
		try {
			MovingStateHelper.startBuffering(physicalOp);
			IOperatorState stateObject = statefulOp.getState();
			state = stateObject.getSerializedState();
			MovingStateHelper.stopBuffering(physicalOp);
		} catch (LoadBalancingException e1) {
			ci.print("Error while Stopping or Starting Buffering.");
		}
		MovingStateSender sender = MovingStateManager.getInstance().getSender(
				pipeId);
		if (sender == null) {
			ci.println(ERROR_NOTFOUND + pipeId);
			return;
		}
		
		if(state==null) {
			ci.println(ERROR_NO_STATE);
		}
		
		try {
			sender.sendData(state);
		} catch (LoadBalancingException e) {
			ci.println("ERROR:" + e.getMessage());
			return;
		}
		ci.println("Data sent.");

	}
	

	public void _extractState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: extractState <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NO_STATE ="Error: Could not get state.";
		
		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		IStatefulPO statefulOp = null;

		IPhysicalQuery localQuery = OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(
				localQueryId);
		for (IPhysicalOperator operator : localQuery.getAllOperators()) {
			if (operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO) operator;
				break;
			}
		}

		if (statefulOp == null) {
			ci.println(ERROR_LOCALOP);
			return;
		}
		IPhysicalOperator physicalOp = (IPhysicalOperator)statefulOp;
		Serializable state = null;
		
		try {
			MovingStateHelper.startBuffering(physicalOp);
			IOperatorState stateObject = statefulOp.getState();
			long startTime = System.currentTimeMillis();
			ci.println("Estimated size:"+stateObject.estimateSizeInBytes());
			long curTime = System.currentTimeMillis();
			ci.println("Estimation took "+(curTime-startTime)+" ms.");
			startTime = System.currentTimeMillis();
			state = stateObject.getSerializedState();
			int realSize = getSizeInBytesOfSerializable(state);
			curTime = System.currentTimeMillis();
			ci.println("Real State Size (after serialization:)" + realSize);
			ci.println("Serialization took "+(curTime-startTime)+" ms.");
			
			MovingStateHelper.stopBuffering(physicalOp);
		} catch (LoadBalancingException e1) {
			ci.print("Error while Stopping or Starting Buffering.");
		}
		
		if(state==null) {
			ci.println(ERROR_NO_STATE);
		} else {
			ci.println(state.toString());
		}		
		ci.println("Data sent.");

	}
	
	
	

	public void _installStateReceiver(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: installStateReceiver <peerName> <pipeID>";
		final String ERROR_NOTFOUND = "Error: No peer found with name ";

		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String pipeID = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeID)) {
			ci.println(ERROR_USAGE);
			return;
		}

		Collection<PeerID> peerIDs = peerDictionary.getRemotePeerIDs();
		for (PeerID peer : peerIDs) {
			if (peerDictionary.getRemotePeerName(peer).equals(peerName)) {
				MovingStateManager.getInstance().addReceiver(peer.toString(),
						pipeID);
				ci.println("Receiver installed");
				return;
			}
		}
		ci.println(ERROR_NOTFOUND + peerName);
	}

	public void _sendData(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: sendData <pipeID> <message>";
		final String ERROR_NOTFOUND = "Error: No sender found with pipe ";
		String pipeID = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeID)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String nextWord = ci.nextArgument();
		if (Strings.isNullOrEmpty(nextWord)) {

			ci.println(ERROR_USAGE);
			return;
		}
		StringBuilder sb = new StringBuilder();
		while (nextWord != null) {
			sb.append(nextWord);
			sb.append(' ');
			nextWord = ci.nextArgument();
		}
		String message = sb.toString();

		MovingStateSender sender = MovingStateManager.getInstance().getSender(
				pipeID);
		if (sender == null) {
			ci.println(ERROR_NOTFOUND + pipeID);
			return;
		}
		try {
			sender.sendData(message);
		} catch (LoadBalancingException e) {
			ci.println("ERROR:" + e.getMessage());
			return;
		}
		ci.println("Sent: " + message);
	}
	
	
	public void _sendLongString(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: sendLongString <pipeID>";
		final String ERROR_NOTFOUND = "Error: No sender found with pipe ";
		String pipeID = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeID)) {

			ci.println(ERROR_USAGE);
			return;
		}
		
		//Create 100MB of Data.
		
		StringBuilder sb = new StringBuilder();
		StringBuilder kbString = new StringBuilder();
		//Create 1kB
		for (int i=0;i<1024;i++) {
			kbString.append('A');
		}
		StringBuilder mbString = new StringBuilder();
		//Creat 1Mb
		for (int i=0;i<1024;i++) {
			mbString.append(kbString);
		}
		//Create 100Mb
		for (int i=0;i<100;i++) {
			sb.append(mbString);
		}
		String message = sb.toString();

		MovingStateSender sender = MovingStateManager.getInstance().getSender(
				pipeID);
		if (sender == null) {
			ci.println(ERROR_NOTFOUND + pipeID);
			return;
		}
		try {
			sender.sendData(message);
		} catch (LoadBalancingException e) {
			ci.println("ERROR:" + e.getMessage());
			return;
		}
		ci.println("Sent.");
	}

	/***
	 * Injects a previously received state in the first stateful Operator in
	 * List. Can be used to test serialisation.
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _testInjectState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: testInjectState <pipeID> <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NOTFOUND = "ERROR: No Sender found for pipe:";
		final String ERROR_NOTHING_RECEIVED = "ERROR: No status received. Try sending a status first.";

		String pipeId = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeId)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		IStatefulPO statefulOp = null;

		IPhysicalQuery localQuery = OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(
				localQueryId);
		for (IPhysicalOperator operator : localQuery.getAllOperators()) {
			if (operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO) operator;
				break;
			}
		}

		if (statefulOp == null) {
			ci.println(ERROR_LOCALOP);
			return;
		}
		MovingStateReceiver receiver = MovingStateManager.getInstance()
				.getReceiver(pipeId);
		if (receiver == null) {
			ci.println(ERROR_NOTFOUND + pipeId);
			return;
		}

		try {
			receiver.injectState(statefulOp);
		} catch (LoadBalancingException e) {
			ci.println(ERROR_NOTHING_RECEIVED);
			return;
		}
		ci.println("Injected State.");

	}



	
	

	protected int getSizeInBytesOfSerializable(Serializable obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			out.flush();
			out.close();
		} catch (IOException e) {
			LOG.error("Could not serialize Streamable. Returning 0");
			LOG.error(e.getMessage());
			e.printStackTrace();
			return 0;
		}
		//Sub 4 Bytes for Serialization magic Numbers
		int objectSize = baos.toByteArray().length - 4;
		return objectSize;
	}
	

}
