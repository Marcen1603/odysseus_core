package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.nio.ByteBuffer;

import net.jxta.impl.id.UUID.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;

/**
 * Class of messages to response to a {@link RequestQueryStateMessage}.
 * 
 * @author Michael Brand
 */
public class RequestQueryStateResponseMessage extends AbstractResponseMessage {

	/**
	 * The requested query state.
	 */
	private Optional<QueryState> mState = Optional.absent();

	/**
	 * The requested query state.
	 * 
	 * @return The requested query state if {@link #isPositive()} or
	 *         {@link Optional#absent()} if not {@link #isPositive()}.
	 */
	public Optional<QueryState> getState() {
		return this.mState;
	}

	/**
	 * Empty default constructor.
	 */
	public RequestQueryStateResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery update pipe acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryUpdatePipeMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param state
	 *            The requested query state. <br />
	 *            Must be not null.
	 */
	public RequestQueryStateResponseMessage(UUID uuid, QueryState state) {
		super(uuid);

		Preconditions.checkNotNull(state);
		this.mState = Optional.of(state);
	}

	/**
	 * Creates a new recovery update pipe failure message.
	 * 
	 * @param uuid
	 *            The id of the original {@link RecoveryUpdatePipeMessage}
	 *            message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public RequestQueryStateResponseMessage(UUID uuid, String errorMessage) {
		super(uuid, errorMessage);
	}
	
	@Override
	public byte[] toBytes() {
		byte[] superBytes = super.toBytes();
		int bufferSize = superBytes.length;
		
		if(this.mState.isPresent()) {
			bufferSize += 4;		
		}
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.put(superBytes);
		if (this.mState.isPresent()) {
			buffer.putInt(this.mState.get().ordinal());
		}

		buffer.flip();
		return buffer.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		super.fromBytes(data);
		ByteBuffer buffer = ByteBuffer.wrap(data);

		int stateOrdinal = buffer.getInt();
		this.mState = Optional.of(QueryState.values()[stateOrdinal]);
	}

}