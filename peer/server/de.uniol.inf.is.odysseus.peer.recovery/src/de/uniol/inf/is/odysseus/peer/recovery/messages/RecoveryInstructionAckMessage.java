package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.pipe.PipeID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The acknowledge message for a recovery instruction.
 * 
 * @see RecoveryInstructionMessage
 * @author Michael Brand
 *
 */
public class RecoveryInstructionAckMessage implements IMessage {
	
	public static final int HOLD_ON_ACK = 0;

	public static final int GO_ON_ACK = 1;

	public static final int ADD_QUERY_ACK = 2;

	public static final int UPDATE_SENDER_ACK = 3;

	public static final int UPDATE_RECEIVER_ACK = 4;
	
	private int mMessageType;
	private PipeID mPipeId;

	public RecoveryInstructionAckMessage() {

		// Empty default constructor

	}

	public RecoveryInstructionAckMessage(PipeID pipeId, int messageType) {

		this.mMessageType = messageType;
		this.mPipeId = pipeId;

	}

	@Override
	public byte[] toBytes() {

		ByteBuffer bb = null;
		int bbsize;

		int pipeIdLength = 0;

		pipeIdLength = this.mPipeId.toString().getBytes().length;
		bbsize = 4 + 4 + pipeIdLength;
		bb = ByteBuffer.allocate(bbsize);
		bb.putInt(this.mMessageType);
		bb.putInt(pipeIdLength);
		bb.put(this.mPipeId.toString().getBytes());

		bb.flip();
		return bb.array();

	}

	/**
	 * Parses message from byte array.
	 */
	@Override
	public void fromBytes(byte[] data) {

		ByteBuffer bb = ByteBuffer.wrap(data);
		this.mMessageType = bb.getInt();

		int pipeIdLength = bb.getInt();
		byte[] pipeIdByte = new byte[pipeIdLength];
		String pipeIdString = new String(pipeIdByte);

		try {

			URI uri = new URI(pipeIdString);
			this.mPipeId = PipeID.create(uri);

		} catch (URISyntaxException e) {

			e.printStackTrace();

		}

	}

	public int getMessageType() {

		return this.mMessageType;

	}

	public PipeID getPipeId() {

		return this.mPipeId;

	}

}