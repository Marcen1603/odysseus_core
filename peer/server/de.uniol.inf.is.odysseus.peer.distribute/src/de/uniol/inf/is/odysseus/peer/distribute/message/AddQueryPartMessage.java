package de.uniol.inf.is.odysseus.peer.distribute.message;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class AddQueryPartMessage implements IMessage {

	private static final Logger LOG = LoggerFactory.getLogger(AddQueryPartMessage.class);

	private String pqlStatement;
	private ID sharedQueryID;
	private String transCfgName;
	private int queryPartID;
	private Collection<String> metadataTypes;

	public AddQueryPartMessage() {

	}

	public AddQueryPartMessage(ID sharedQueryID, String pqlStatement, String transCfgName, int queryPartID, Collection<String> metadataTypes) {
		this.pqlStatement = pqlStatement;
		this.sharedQueryID = sharedQueryID;
		this.transCfgName = transCfgName;
		this.queryPartID = queryPartID;
		this.metadataTypes = metadataTypes;
	}

	@Override
	public byte[] toBytes() {
		byte[] pqlStatementBytes = pqlStatement.getBytes();
		byte[] sharedQueryIDBytes = sharedQueryID.toString().getBytes();
		byte[] transCfgNameBytes = transCfgName.getBytes();

		int bbSize = 4 + 4 + pqlStatementBytes.length + 4 + sharedQueryIDBytes.length + 4 + transCfgNameBytes.length;
		bbSize += (4 + (metadataTypes.size() * 4) + calcByteSize(metadataTypes));
		ByteBuffer bb = ByteBuffer.allocate(bbSize);

		bb.putInt(queryPartID);
		bb.putInt(pqlStatementBytes.length);
		bb.putInt(sharedQueryIDBytes.length);
		bb.putInt(transCfgNameBytes.length);

		bb.put(pqlStatementBytes);
		bb.put(sharedQueryIDBytes);
		bb.put(transCfgNameBytes);

		bb.putInt(metadataTypes.size());
		for (String metadataType : metadataTypes) {
			byte[] metadataTypeBytes = metadataType.getBytes();
			bb.putInt(metadataTypeBytes.length);
			bb.put(metadataTypeBytes);
		}

		bb.flip();

		return bb.array();
	}

	private static int calcByteSize(Collection<String> list) {
		int size = 0;
		for (String txt : list) {
			size += (txt.getBytes().length);
		}
		return size;
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);

		queryPartID = bb.getInt();
		int pqlStatementLength = bb.getInt();
		int sharedQueryIDLength = bb.getInt();
		int transCfgNameLength = bb.getInt();

		byte[] pqlStatementBytes = new byte[pqlStatementLength];
		byte[] sharedQueryIDBytes = new byte[sharedQueryIDLength];
		byte[] transCfgNameBytes = new byte[transCfgNameLength];

		bb.get(pqlStatementBytes);
		bb.get(sharedQueryIDBytes);
		bb.get(transCfgNameBytes);

		pqlStatement = new String(pqlStatementBytes);
		sharedQueryID = toID(new String(sharedQueryIDBytes));
		transCfgName = new String(transCfgNameBytes);
		
		int metadataTypeCount = bb.getInt();
		metadataTypes = Lists.newArrayList();
		for( int i = 0; i < metadataTypeCount; i++ ) {
			int size = bb.getInt();
			byte[] typeBytes = new byte[size];
			bb.get(typeBytes);
			metadataTypes.add( new String(typeBytes));
		}
	}

	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not get id from text {}", text, ex);
			return null;
		}
	}

	public String getPqlStatement() {
		return pqlStatement;
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}

	public String getTransCfgName() {
		return transCfgName;
	}

	public int getQueryPartID() {
		return queryPartID;
	}
	
	public Collection<String> getMetadataTypes() {
		return metadataTypes;
	}
	
}
