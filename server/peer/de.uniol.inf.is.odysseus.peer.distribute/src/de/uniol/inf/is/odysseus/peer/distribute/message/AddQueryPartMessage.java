package de.uniol.inf.is.odysseus.peer.distribute.message;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class AddQueryPartMessage implements IMessage {

	private static final Logger LOG = LoggerFactory.getLogger(AddQueryPartMessage.class);
	
	private String pqlStatement;
	private ID sharedQueryID;
	private String transCfgName;
	private int queryPartID;
	
	public AddQueryPartMessage() {
		
	}
	
	public AddQueryPartMessage( ID sharedQueryID, String pqlStatement, String transCfgName, int queryPartID ) {
		this.pqlStatement = pqlStatement;
		this.sharedQueryID = sharedQueryID;
		this.transCfgName = transCfgName;
		this.queryPartID = queryPartID;
	}
	
	@Override
	public byte[] toBytes() {
		byte[] pqlStatementBytes = pqlStatement.getBytes();
		byte[] sharedQueryIDBytes = sharedQueryID.toString().getBytes();
		byte[] transCfgNameBytes = transCfgName.getBytes();
		
		ByteBuffer bb = ByteBuffer.allocate( 4 + 4 + pqlStatementBytes.length + 4 + sharedQueryIDBytes.length + 4 + transCfgNameBytes.length );
		
		bb.putInt(queryPartID);
		bb.putInt(pqlStatementBytes.length);
		bb.putInt(sharedQueryIDBytes.length);
		bb.putInt(transCfgNameBytes.length);
		
		bb.put(pqlStatementBytes);
		bb.put(sharedQueryIDBytes);
		bb.put(transCfgNameBytes);
		
		bb.flip();
		
		return bb.array();
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
}
