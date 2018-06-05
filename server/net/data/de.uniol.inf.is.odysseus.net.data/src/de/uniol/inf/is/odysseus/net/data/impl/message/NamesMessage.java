package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;
import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class NamesMessage implements IMessage {

	private Collection<String> names;
	
	public NamesMessage() {
		
	}
	
	public NamesMessage(Collection<String> names) {
		Preconditions.checkNotNull(names, "names must not be null!");

		this.names = names;
	}
	
	@Override
	public byte[] toBytes() {
		int size = 0;
		for( String name : names ) {
			size += name.length();
		}
		
		ByteBuffer bb = ByteBuffer.allocate(size + names.size() * 4);
		for( String name : names ) {
			MessageUtils.putString(bb, name);
		}
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		names = Lists.newArrayList();
		while( bb.remaining() > 0 ) {
			names.add(MessageUtils.getString(bb));
		}
	}
	
	public Collection<String> getNames() {
		return names;
	}

}
