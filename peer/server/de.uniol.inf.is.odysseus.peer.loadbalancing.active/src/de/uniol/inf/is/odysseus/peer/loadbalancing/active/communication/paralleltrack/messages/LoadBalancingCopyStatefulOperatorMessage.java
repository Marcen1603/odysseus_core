package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/***
 * Message for copying Operator states.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingCopyStatefulOperatorMessage implements IMessage {
	
	public static final int INSTALL_BUFFER = 0;
	public static final int INSTALL_BUFFERS_FINISHED = 1;
	
	public static final int INITALIZE_STATE_COPY = 2;
	public static final int ACK_INITIALIZE_STATE_COPY = 3;
	
	public static final int STATE_COPY_FINISHED = 4;
	public static final int ACK_STATE_COPY_FINISHED = 5;
	
	
	private int loadBalancingProcessId;
	private int msgType;
	
	private int port;
	private String ipAddress;
	private String operatorName;
	private String pipeID;
	
	
	public static LoadBalancingCopyStatefulOperatorMessage createInitializeStateCopyMessage(int lbProcessId, String ipAddress, int port, String operatorName) {
		LoadBalancingCopyStatefulOperatorMessage message = new LoadBalancingCopyStatefulOperatorMessage();
		message.msgType = INITALIZE_STATE_COPY;
		message.loadBalancingProcessId = lbProcessId;
		message.ipAddress = ipAddress;
		message.port = port;
		message.operatorName = operatorName;
		return message;
	}
	
	public static LoadBalancingCopyStatefulOperatorMessage createAckInitializeMessage(int lbProcessId) {
		LoadBalancingCopyStatefulOperatorMessage message = new LoadBalancingCopyStatefulOperatorMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = ACK_INITIALIZE_STATE_COPY;
		return message;
	}
	
	public static LoadBalancingCopyStatefulOperatorMessage createStateCopyFinishedMessage(int lbProcessId) {
		LoadBalancingCopyStatefulOperatorMessage message = new LoadBalancingCopyStatefulOperatorMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = STATE_COPY_FINISHED;
		return message;
	}
	
	public static LoadBalancingCopyStatefulOperatorMessage createAckCopyFinishedMessage(int lbProcessId) {
		LoadBalancingCopyStatefulOperatorMessage message = new LoadBalancingCopyStatefulOperatorMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = ACK_STATE_COPY_FINISHED;
		return message;
	}
	
	public static LoadBalancingCopyStatefulOperatorMessage createInstallBuffersMessage(int lbProcessId, String pipeID) {
		LoadBalancingCopyStatefulOperatorMessage message = new LoadBalancingCopyStatefulOperatorMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = INSTALL_BUFFER;
		message.pipeID = pipeID;
		return message;
	}
	
	public LoadBalancingCopyStatefulOperatorMessage() {
		
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = null;
		int bbsize;
		
		
		
		switch(msgType) {
			
			case INSTALL_BUFFER:
			case INSTALL_BUFFERS_FINISHED:
				byte[] pipeIDBytes = pipeID.getBytes();
				/***
				 * 4 Bytes msgType
				 * 4 Bytes lbProcessId
				 * 4 Bytes pipeID Length
				 * {pipeID}
				 */
				
				bbsize = 4+4+4+pipeIDBytes.length;
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(pipeIDBytes.length);
				bb.put(pipeIDBytes);
				break;
		
			case ACK_INITIALIZE_STATE_COPY:
				byte[] ipAddressBytes = ipAddress.getBytes();
				byte[] operatorNameBytes = operatorName.getBytes();
				
				/***
				 * 4 Bytes msgType
				 * 4 Bytes lbProcessId
				 * 4 Bytes ipAddr. Length
				 * {ipAddress}
				 * 4 Bytes port
				 * 4 Bytes operatorNameLength
				 * {operatorNameLength}
				 */
				bbsize = 4+4+4+ipAddressBytes.length+4+4+operatorNameBytes.length;
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(ipAddressBytes.length);
				bb.put(ipAddressBytes);
				bb.putInt(port);
				bb.putInt(operatorNameBytes.length);
				bb.put(operatorNameBytes);
				break;
			default:
				/**
				 * 4 Bytes msgType
				 * 4 Bytes lbProcessId
				 */
				bbsize = 8;
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				break;
				
		}
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		msgType = bb.getInt();
		loadBalancingProcessId = bb.getInt();
		switch(msgType) {
		
		case INSTALL_BUFFER:
		case INSTALL_BUFFERS_FINISHED:
			int pipeIDLength = bb.getInt();
			byte[] pipeIDBytes = new byte[pipeIDLength];
			bb.get(pipeIDBytes);
			this.pipeID = new String(pipeIDBytes);
			break;
		
		case ACK_INITIALIZE_STATE_COPY:
			int ipAddressLength = bb.getInt();
			byte[] ipAddressBytes = new byte[ipAddressLength];
			bb.get(ipAddressBytes);
			this.ipAddress = new String(ipAddressBytes);
			
			this.port = bb.getInt();
			
			int operatorNameLength = bb.getInt();
			byte[] operatorNameBytes = new byte[operatorNameLength];
			bb.get(operatorNameBytes);
			this.operatorName = new String(operatorNameBytes);
			break;
			
		}
		
	}

}
