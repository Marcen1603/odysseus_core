package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.nio.ByteBuffer;
import java.util.UUID;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class AddQueryPartMessage implements IMessage {

	private String queryText;
	private String parser;
	private UUID sharedQueryID;
	private String transCfgName;
	private int queryPartID;

	public AddQueryPartMessage() {

	}

	public AddQueryPartMessage(UUID sharedQueryID, String queryText, String parser, String transCfgName, int queryPartID) {
		this.queryText = queryText;
		this.parser = parser;
		this.sharedQueryID = sharedQueryID;
		this.transCfgName = transCfgName;
		this.queryPartID = queryPartID;
	}

	@Override
	public byte[] toBytes() {
		byte[] queryTextBytes = queryText.getBytes();
		byte[] parserBytes = parser.getBytes();
		byte[] sharedQueryIDBytes = sharedQueryID.toString().getBytes();
		byte[] transCfgNameBytes = transCfgName.getBytes();

		int bbSize = 4 + 4 + queryTextBytes.length + 4 + parserBytes.length + 4 + sharedQueryIDBytes.length + 4 + transCfgNameBytes.length;
		ByteBuffer bb = ByteBuffer.allocate(bbSize);

		bb.putInt(queryPartID);
		bb.putInt(queryTextBytes.length);
		bb.putInt(parserBytes.length);
		bb.putInt(sharedQueryIDBytes.length);
		bb.putInt(transCfgNameBytes.length);

		bb.put(queryTextBytes);
		bb.put(parserBytes);
		bb.put(sharedQueryIDBytes);
		bb.put(transCfgNameBytes);

		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);

		queryPartID = bb.getInt();
		int queryTextLength = bb.getInt();
		int parserLength = bb.getInt();
		int sharedQueryIDLength = bb.getInt();
		int transCfgNameLength = bb.getInt();

		byte[] queryTextBytes = new byte[queryTextLength];
		byte[] parserBytes = new byte[parserLength];
		byte[] sharedQueryIDBytes = new byte[sharedQueryIDLength];
		byte[] transCfgNameBytes = new byte[transCfgNameLength];

		bb.get(queryTextBytes);
		bb.get(parserBytes);
		bb.get(sharedQueryIDBytes);
		bb.get(transCfgNameBytes);

		queryText = new String(queryTextBytes);
		parser = new String(parserBytes);
		sharedQueryID = UUID.fromString(new String(sharedQueryIDBytes));
		transCfgName = new String(transCfgNameBytes);
	}

	public String getQueryText() {
		return queryText;
	}
	
	public String getParser() {
		return parser;
	}

	public UUID getSharedQueryID() {
		return sharedQueryID;
	}

	public String getTransCfgName() {
		return transCfgName;
	}

	public int getQueryPartID() {
		return queryPartID;
	}
}
