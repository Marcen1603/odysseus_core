package de.uniol.inf.is.odysseus.wrapper.plugwise.protocol;

import java.util.List;

// https://bitbucket.org/hadara/python-plugwise/src/635b13b0b20f763b831c11e814149413cda69214/plugwise/protocol.py?at=default
@SuppressWarnings("all")

public class PlugwiseMessage {
	static final byte[] PACKET_HEADER = {0x05,0x05,0x03,0x03};
	static final byte[] PACKET_FOOTER = {0x0d,0x0a};
	
	final int ID;
	final List<IPlugwiseTypes> args;
	final String mac;
	
	public PlugwiseMessage(int ID, final List<IPlugwiseTypes> args, String mac) {
		this.ID = ID;
		this.args = args;
		this.mac = mac;
	}
	
	byte[] serialize(){
		
		StringBuffer msg = new StringBuffer();
		msg.append(ID);
		msg.append(mac);
		for (IPlugwiseTypes a:args){
			msg.append(a.serialize());
		}
		byte[] checksum = {0};//calcChecksum(msg);
		byte[] part1 = msg.toString().getBytes();
		byte[] macP = mac.getBytes();
		byte[] ret = new byte[2+macP.length+checksum.length];
		
		
		return ret;
//		args = b''.join(a.serialize() for a in self.args)
//		        msg = self.ID+self.mac+sc(args)
//		        checksum = self.calculate_checksum(msg)
//		        return self.PACKET_HEADER+msg+checksum+self.PACKET_FOOTER
	}
}
