package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.pipe.PipeID;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The failure message for a recovery instruction.
 * 
 * @see RecoveryInstructionMessage
 * @author Michael Brand
 *
 */
public class RecoveryInstructionFailMessage implements IMessage {

	public static final int HOLD_ON_FAIL = 0;

	public static final int GO_ON_FAIL = 1;

	public static final int ADD_QUERY_FAIL = 2;

	public static final int UPDATE_SENDER_FAIL = 3;

	public static final int UPDATE_RECEIVER_FAIL = 4;
	
	private int mMessageType;
	private PipeID mPipeId;
	private String mMessage = "FAILED";

	public RecoveryInstructionFailMessage() {

		// Empty default constructor

	}

	public RecoveryInstructionFailMessage(PipeID pipeId, int messageType) {

		this.mMessageType = messageType;
		this.mPipeId = pipeId;

	}

	public RecoveryInstructionFailMessage(PipeID pipeId, int messageType,
			String errorMessage) {

		this(pipeId, messageType);
		if (!Strings.isNullOrEmpty(errorMessage)) {

			this.mMessage = errorMessage;

		}

	}

	@Override
	public byte[] toBytes() {

		ByteBuffer bb = null;
		int bbsize;

		int pipeIdLength = this.mPipeId.toString().getBytes().length;
		int errorLength = this.mMessage.toString().getBytes().length;
		bbsize = 4 + 4 + pipeIdLength + 4 + errorLength;
		bb = ByteBuffer.allocate(bbsize);
		bb.putInt(this.mMessageType);
		bb.putInt(pipeIdLength);
		bb.put(this.mPipeId.toString().getBytes());
		bb.putInt(errorLength);
		bb.put(this.mMessage.getBytes());

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

		int messageLength = bb.getInt();
		byte[] messageByte = new byte[messageLength];
		bb.get(messageByte, 0, messageLength);
		this.mMessage = new String(messageByte);

	}

	public int getMessageType() {

		return this.mMessageType;

	}

	public PipeID getPipeId() {

		return this.mPipeId;

	}

	public String getErrorMessage() {

		return this.mMessage;

	}

}