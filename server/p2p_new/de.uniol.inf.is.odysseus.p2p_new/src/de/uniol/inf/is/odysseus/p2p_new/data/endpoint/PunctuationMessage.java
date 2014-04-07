package de.uniol.inf.is.odysseus.p2p_new.data.endpoint;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

public class PunctuationMessage implements IMessage {

	private IPunctuation punctuation;
	private int idHash;
	
	public PunctuationMessage() {
		
	}
	
	public PunctuationMessage( IPunctuation punc, int idHash) {
		this.punctuation = punc;
	}
	
	@Override
	public byte[] toBytes() {
		byte[] puncBytes = ObjectByteConverter.objectToBytes(punctuation);
		ByteBuffer bb = ByteBuffer.allocate(puncBytes.length + 4);
		bb.putInt(idHash);
		bb.put(puncBytes);
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		idHash = bb.getInt();
		
		byte[] puncBytes = new byte[data.length - 4];
		bb.get(puncBytes);
		
		punctuation = (IPunctuation) ObjectByteConverter.bytesToObject(puncBytes);
	}

	public IPunctuation getPunctuation() {
		return punctuation;
	}
	
	public int getIdHash() {
		return idHash;
	}
}
