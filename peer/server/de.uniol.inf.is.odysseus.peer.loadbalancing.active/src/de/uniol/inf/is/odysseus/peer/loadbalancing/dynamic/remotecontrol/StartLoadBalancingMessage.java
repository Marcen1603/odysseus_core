package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.remotecontrol;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class StartLoadBalancingMessage implements IMessage {

	private String strategyName="";

	private boolean startLogLoad=false;
	
	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}


	public boolean isStartLogLoad() {
		return startLogLoad;
	}

	public void setStartLogLoad(boolean startLogLoad) {
		this.startLogLoad = startLogLoad;
	}
	
	
	public StartLoadBalancingMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public StartLoadBalancingMessage(String strategy,boolean startLogLoad)  {
		this.strategyName = strategy;
		this.startLogLoad = startLogLoad;
	}
	
	@Override
	public byte[] toBytes() {
		
		byte[] strategyNameAsBytes = strategyName.getBytes();
		
		ByteBuffer bb = ByteBuffer.allocate(4+4+strategyNameAsBytes.length);
		
		if(startLogLoad) {
			bb.putInt(1);
		}
		else {
			bb.putInt(0);
		}
		
		bb.putInt(strategyNameAsBytes.length);
		bb.put(strategyNameAsBytes);
		return bb.array();
	}


	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		int startLogLoadInt = bb.getInt();
		int strategyNameSize = bb.getInt();
		byte[] strategyNameAsBytes = new byte[strategyNameSize];
		bb.get(strategyNameAsBytes);
		strategyName = strategyNameAsBytes.toString();
		if(startLogLoadInt==1) {
			startLogLoad=true;
		}
		else {
			startLogLoad = false;
		}
		
	}

}
