package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Class of messages to send an add query instruction.
 * 
 * @author Michael Brand, Tobias Brandt
 */
public class RecoveryAddQueryMessage implements IMessage {

	/**
	 * The id of the message.
	 */
	private UUID mID = UUIDFactory.newUUID();

	/**
	 * The id of the message.
	 * 
	 * @return A unique identifier.
	 */
	public UUID getUUID() {
		return this.mID;
	}

	/**
	 * The PQL code to execute.
	 */
	private String mPQL;

	/**
	 * The PQL code to execute.
	 * 
	 * @return A valid PQL code.
	 */
	public String getPQLCode() {
		return this.mPQL;
	}

	/**
	 * The affected local query.
	 */
	private int mLocalQuery;

	/**
	 * The affected shared query.
	 * 
	 * @return The id of a shared query.
	 */
	public int getLocalQueryId() {
		return this.mLocalQuery;
	}

	/**
	 * The state of the query.
	 */
	private QueryState mQueryState;

	/**
	 * The state of the query.
	 */
	public QueryState getQueryState() {
		return this.mQueryState;
	}

	/**
	 * The id of the recovery process.
	 */
	private java.util.UUID mProcessId;

	private java.util.UUID mSubprocessId;

	/**
	 * The id of the recovery process.
	 * 
	 * @return A unique identifier.
	 */
	public java.util.UUID getRecoveryProcessId() {
		return this.mProcessId;
	}

	private ID mSharedQuery;

	public ID getSharedQueryId() {
		return this.mSharedQuery;
	}

	private boolean mMaster;

	public boolean isMaster() {
		return this.mMaster;
	}

	/**
	 * Empty default constructor.
	 */
	public RecoveryAddQueryMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery add query message.
	 * 
	 * @param pqls
	 *            The PQL code to execute. <br />
	 *            Must be not null.
	 * @param localQueryId
	 *            The affected local query id.
	 * @param queryState
	 *            The state of the query. <br />
	 *            Must be not null.
	 * @param processId
	 *            The id of the recovery process. <br />
	 *            Must be not null.
	 * @param subprocessID
	 *            The id of the recovery subprocess. <br />
	 *            Must be not null.
	 */
	public RecoveryAddQueryMessage(String pql, int localQueryId,
			QueryState queryState, ID sharedQuery, boolean master,
			java.util.UUID processId, java.util.UUID subprocessID) {
		Preconditions.checkNotNull(pql);
		Preconditions.checkNotNull(queryState);
		Preconditions.checkNotNull(processId);
		Preconditions.checkNotNull(subprocessID);

		this.mPQL = pql;
		this.mLocalQuery = localQueryId;
		this.mQueryState = queryState;
		this.mSharedQuery = sharedQuery;
		this.mMaster = master;
		this.mProcessId = processId;
		this.mSubprocessId = subprocessID;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] pqlBytes = this.mPQL.getBytes();
		byte[] processIdBytes = this.mProcessId.toString().getBytes();
		byte[] subprocessIdBytes = this.mSubprocessId.toString().getBytes();
		byte[] sharedQueryBytes = null;

		int bufferSize = 4 + idBytes.length + 4 + pqlBytes.length + 4 + 4 + 4
				+ processIdBytes.length + 4 + subprocessIdBytes.length + 4;
		if (this.mSharedQuery != null) {
			sharedQueryBytes = this.mSharedQuery.toString().getBytes();
			bufferSize += sharedQueryBytes.length;
		}
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);

		buffer.putInt(pqlBytes.length);
		buffer.put(pqlBytes);

		buffer.putInt(this.mQueryState.ordinal());
		buffer.putInt(mLocalQuery);

		buffer.putInt(processIdBytes.length);
		buffer.put(processIdBytes);

		buffer.putInt(subprocessIdBytes.length);
		buffer.put(subprocessIdBytes);

		if (this.mSharedQuery != null) {
			buffer.putInt(sharedQueryBytes.length);
			buffer.put(sharedQueryBytes);
		} else {
			buffer.putInt(0);
		}

		buffer.flip();
		return buffer.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);

		int idBytesLength = buffer.getInt();
		byte[] idBytes = new byte[idBytesLength];
		buffer.get(idBytes);
		this.mID = new UUID(new String(idBytes));

		int pqlBytesLength = buffer.getInt();
		byte[] pqlBytes = new byte[pqlBytesLength];
		buffer.get(pqlBytes);
		this.mPQL = new String(pqlBytes);

		this.mQueryState = QueryState.values()[buffer.getInt()];
		mLocalQuery = buffer.getInt();

		int processIdBytesLength = buffer.getInt();
		byte[] processIdBytes = new byte[processIdBytesLength];
		buffer.get(processIdBytes);
		this.mProcessId = java.util.UUID.fromString(new String(processIdBytes));

		int subprocessIdBytesLength = buffer.getInt();
		byte[] subprocessIdBytes = new byte[subprocessIdBytesLength];
		buffer.get(subprocessIdBytes);
		this.mSubprocessId = java.util.UUID.fromString(new String(
				subprocessIdBytes));

		int sharedQueryIdBytesLength = buffer.getInt();
		if (sharedQueryIdBytesLength > 0) {
			byte[] sharedQueryIdBytes = new byte[sharedQueryIdBytesLength];
			buffer.get(sharedQueryIdBytes);
			try {
				final URI id = new URI(new String(sharedQueryIdBytes));
				this.mSharedQuery = IDFactory.fromURI(id);
			} catch (URISyntaxException | ClassCastException ex) {
				this.mSharedQuery = null;
			}
		}

	}

	public java.util.UUID getmSubprocessId() {
		return mSubprocessId;
	}

	public void setmSubprocessId(java.util.UUID mSubprocessId) {
		this.mSubprocessId = mSubprocessId;
	}

}