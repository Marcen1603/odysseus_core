package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;
import net.jxta.peer.PeerID;

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
	 * The PQL code to execute.
	 */
	private String mPQL;

	/**
	 * The affected local query.
	 */
	private int mLocalQuery;

	/**
	 * The state of the query.
	 */
	private QueryState mQueryState;

	/**
	 * The id of the recovery process.
	 */
	private java.util.UUID mProcessId;

	private java.util.UUID mSubprocessId;
	private ID mSharedQuery;
	private boolean mMaster;
	private PeerID mMasterId;
	private PeerID mFailedPeerId;

	// Connection information
	private String clientIP; // The tablet
	private String hostIP; // The peer
	private int hostPort;

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
	 * 
	 * @return A valid PQL code.
	 */
	public String getPQLCode() {
		return this.mPQL;
	}

	/**
	 * The affected local query.
	 * 
	 * @return The id of a shared query.
	 */
	public int getLocalQueryId() {
		return this.mLocalQuery;
	}

	/**
	 * The state of the query.
	 */
	public QueryState getQueryState() {
		return this.mQueryState;
	}

	/**
	 * The id of the recovery process.
	 * 
	 * @return A unique identifier.
	 */
	public java.util.UUID getRecoveryProcessId() {
		return this.mProcessId;
	}

	public ID getSharedQueryId() {
		return this.mSharedQuery;
	}

	public boolean isMaster() {
		return this.mMaster;
	}

	public PeerID getMasterId() {
		return this.mMasterId;
	}

	public PeerID getFailedPeerId() {
		return this.mFailedPeerId;
	}

	public String getClientIP() {
		return this.clientIP;
	}

	public String getHostIP() {
		return hostIP;
	}

	public int getHostPort() {
		return hostPort;
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
			PeerID masterId, PeerID failedPeerId, java.util.UUID processId,
			java.util.UUID subprocessID, String clientIp, String hostIP, int hostPort) {
		Preconditions.checkNotNull(pql);
		Preconditions.checkNotNull(queryState);
		Preconditions.checkNotNull(processId);
		Preconditions.checkNotNull(subprocessID);

		this.mPQL = pql;
		this.mLocalQuery = localQueryId;
		this.mQueryState = queryState;
		this.mSharedQuery = sharedQuery;
		this.mMaster = master;
		this.mMasterId = masterId;
		this.mFailedPeerId = failedPeerId;
		this.mProcessId = processId;
		this.mSubprocessId = subprocessID;
		this.clientIP = clientIp;
		this.hostIP = hostIP;
		this.hostPort = hostPort;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] pqlBytes = this.mPQL.getBytes();
		byte[] processIdBytes = this.mProcessId.toString().getBytes();
		byte[] subprocessIdBytes = this.mSubprocessId.toString().getBytes();
		byte[] sharedQueryBytes = null;
		byte[] masterIdBytes = null;
		byte[] failedPeerIdBytes = null;
		byte[] clientIpBytes = null;
		byte[] hostIPBytes = null;

		int bufferSize = 4 + idBytes.length + 4 + pqlBytes.length + 4 + 4
				+ processIdBytes.length + 4 + subprocessIdBytes.length + 4;

		bufferSize += 4;
		if (this.mSharedQuery != null) {
			sharedQueryBytes = this.mSharedQuery.toString().getBytes();
			bufferSize += sharedQueryBytes.length;
		}

		// boolean master
		bufferSize += 4;

		bufferSize += 4;
		if (this.mMasterId != null) {
			masterIdBytes = this.mMasterId.toString().getBytes();
			bufferSize += masterIdBytes.length;
		}

		bufferSize += 4;
		if (this.mFailedPeerId != null) {
			failedPeerIdBytes = this.mFailedPeerId.toString().getBytes();
			bufferSize += failedPeerIdBytes.length;
		}

		// client IP (tablet)
		bufferSize += 4;
		if (this.clientIP != null) {
			clientIpBytes = this.clientIP.toString().getBytes();
			bufferSize += clientIpBytes.length;
		}
		
		// host IP (IP of failed peer)
		bufferSize += 4;
		if (this.hostIP != null) {
			hostIPBytes = this.hostIP.toString().getBytes();
			bufferSize += hostIPBytes.length;
		}
		
		// host port (port of socket connection of failed peer)
		bufferSize += 4;

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

		// boolean master
		if (this.mMaster) {
			buffer.putInt(1);
		} else {
			buffer.putInt(0);
		}

		if (this.mMasterId != null) {
			buffer.putInt(masterIdBytes.length);
			buffer.put(masterIdBytes);
		} else {
			buffer.putInt(0);
		}

		if (this.mFailedPeerId != null) {
			buffer.putInt(failedPeerIdBytes.length);
			buffer.put(failedPeerIdBytes);
		} else {
			buffer.putInt(0);
		}

		if (this.clientIP != null) {
			buffer.putInt(this.clientIP.length());
			buffer.put(clientIpBytes);
		} else {
			buffer.putInt(0);
		}
		
		if (this.hostIP != null) {
			buffer.putInt(this.hostIP.length());
			buffer.put(hostIPBytes);
		} else {
			buffer.putInt(0);
		}
		
		buffer.putInt(hostPort);

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
		this.mLocalQuery = buffer.getInt();

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

		// boolean master
		int booleanMaster = buffer.getInt();
		if (booleanMaster == 1) {
			this.mMaster = true;
		} else {
			this.mMaster = false;
		}

		int masterIdBytesLength = buffer.getInt();
		if (masterIdBytesLength > 0) {
			byte[] masterIdBytes = new byte[masterIdBytesLength];
			buffer.get(masterIdBytes);
			try {
				final URI id = new URI(new String(masterIdBytes));
				this.mMasterId = PeerID.create(id);
			} catch (URISyntaxException | ClassCastException ex) {
				this.mMasterId = null;
			}
		}

		int failedPeerIdBytesLength = buffer.getInt();
		if (failedPeerIdBytesLength > 0) {
			byte[] failedPeerIdBytes = new byte[failedPeerIdBytesLength];
			buffer.get(failedPeerIdBytes);
			try {
				final URI id = new URI(new String(failedPeerIdBytes));
				this.mFailedPeerId = PeerID.create(id);
			} catch (URISyntaxException | ClassCastException ex) {
				this.mFailedPeerId = null;
			}
		}

		int clientIpBytesLength = buffer.getInt();
		if (clientIpBytesLength > 0) {
			byte[] clientIpBytes = new byte[clientIpBytesLength];
			buffer.get(clientIpBytes);
			String clientIpString = new String(clientIpBytes);
			this.clientIP = clientIpString;
		}
		
		int hostIpBytesLength = buffer.getInt();
		if (hostIpBytesLength > 0) {
			byte[] hostIpBytes = new byte[hostIpBytesLength];
			buffer.get(hostIpBytes);
			String hostIpString = new String(hostIpBytes);
			this.hostIP = hostIpString;
		}
		
		this.hostPort = buffer.getInt();

	}

	public java.util.UUID getmSubprocessId() {
		return mSubprocessId;
	}

	public void setmSubprocessId(java.util.UUID mSubprocessId) {
		this.mSubprocessId = mSubprocessId;
	}

}