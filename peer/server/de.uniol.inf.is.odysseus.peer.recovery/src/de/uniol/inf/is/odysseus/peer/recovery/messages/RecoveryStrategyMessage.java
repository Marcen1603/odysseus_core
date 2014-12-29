package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.nio.ByteBuffer;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Class of messages to send recovery strategy information.
 * 
 * @author Michael Brand
 */
public class RecoveryStrategyMessage implements IMessage {

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
	 * The PQL code for which the strategy is chosen.
	 */
	private String mPQL;

	/**
	 * The PQL code for which the strategy is chosen.
	 * 
	 * @return A valid PQL code.
	 */
	public String getPQLCode() {
		return this.mPQL;
	}

	/**
	 * The recovery strategy.
	 */
	private String mStrategy;

	/**
	 * The recovery strategy.
	 * 
	 * @return A string representing a recovery strategy.
	 */
	public String getStrategy() {
		return this.mStrategy;
	}

	/**
	 * Empty default constructor.
	 */
	public RecoveryStrategyMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery strategy message.
	 * 
	 * @param pql
	 *            The PQL code for which the strategy is chosen. <br />
	 *            Must be not null.
	 * @param strategy
	 *            The recovery strategy. <br />
	 *            Must be not null.
	 */
	public RecoveryStrategyMessage(String pql, String strategy) {
		Preconditions.checkNotNull(pql);
		Preconditions.checkNotNull(strategy);

		this.mPQL = pql;
		this.mStrategy = strategy;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] pqlBytes = this.mPQL.getBytes();
		byte[] strategyBytes = this.mStrategy.getBytes();

		int bufferSize = 4 + idBytes.length + 4 + pqlBytes.length + 4
				+ strategyBytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);

		buffer.putInt(pqlBytes.length);
		buffer.put(pqlBytes);

		buffer.putInt(strategyBytes.length);
		buffer.put(strategyBytes);

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

		int strategyBytesLength = buffer.getInt();
		byte[] strategyBytes = new byte[strategyBytesLength];
		buffer.get(strategyBytes);
		this.mStrategy = new String(strategyBytes);
	}

}