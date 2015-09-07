package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class FastAuctionMessage implements IMessage {


	private int auctionId;
	private String pqlStatement;
	private String transCfgName;
	private String bidProviderName;
	
	public FastAuctionMessage() {
		
	}
	
	public FastAuctionMessage(int auctionID, String pqlStatement, String transCfgName, String bidProviderName) {
		this.auctionId = auctionID;
		this.pqlStatement = pqlStatement;
		this. transCfgName = transCfgName;
		this.bidProviderName = bidProviderName;
	}
	
	
	@Override
	public byte[] toBytes() {
		
		byte[] pqlStatementAsBytes = pqlStatement.getBytes();
		byte[] transCfgNameAsBytes = transCfgName.getBytes();
		byte[] bidProviderNameAsBytes = bidProviderName.getBytes();
		
		ByteBuffer bb = ByteBuffer.allocate(16 + pqlStatementAsBytes.length + transCfgNameAsBytes.length + bidProviderNameAsBytes.length);
		
		bb.putInt(auctionId);

		bb.putInt(pqlStatementAsBytes.length);
		bb.put(pqlStatementAsBytes);
		
		
		bb.putInt(transCfgNameAsBytes.length);
		bb.put(transCfgNameAsBytes);
		

		bb.putInt(bidProviderNameAsBytes.length);
		bb.put(bidProviderNameAsBytes);
	
		return bb.array();
	}
	
	
	@Override
	public void fromBytes(byte[] data) {
		
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		this.auctionId = bb.getInt();
		
		int sizeOfPql = bb.getInt();
		byte[] pqlStatementBytes = new byte[sizeOfPql];
		bb.get(pqlStatementBytes);
		this.pqlStatement = new String(pqlStatementBytes);
		

		int sizeOfTransCfg= bb.getInt();
		byte[] transCfgNameAsBytes = new byte[sizeOfTransCfg];
		bb.get(transCfgNameAsBytes);
		this.transCfgName = new String(transCfgNameAsBytes);
		


		int sizeOfBifProvider= bb.getInt();
		byte[] bidProviderNameAsBytes = new byte[sizeOfBifProvider];
		bb.get(bidProviderNameAsBytes);
		this.bidProviderName = new String(bidProviderNameAsBytes);
		
	}

	public int getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}

	public String getPqlStatement() {
		return pqlStatement;
	}

	public void setPqlStatement(String pqlStatement) {
		this.pqlStatement = pqlStatement;
	}

	public String getTransCfgName() {
		return transCfgName;
	}

	public void setTransCfgName(String transCfgName) {
		this.transCfgName = transCfgName;
	}

	public String getBidProviderName() {
		return bidProviderName;
	}

	public void setBidProviderName(String bidProviderName) {
		this.bidProviderName = bidProviderName;
	}
	
	
}
