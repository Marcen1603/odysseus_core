package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;


public class ChannelHandlerReceiverPO<R extends MessageLite, W> extends AbstractSource<W>
		implements ITransferHandler<W>, ISource<W> {

	Logger logger = LoggerFactory.getLogger(ChannelHandlerReceiverPO.class);
	private SocketAddress address;
	private R message;
	private ITransformer<R, W> transformer;
	private ChannelReceiverDelegate<R> delegate;

	public ChannelHandlerReceiverPO(SocketAddress address,
			R message, ITransformer<R,W> inputTransformer) {
		this.address = address;
		this.message = message;
		this.transformer = inputTransformer;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		delegate = new ChannelReceiverDelegate<R>(ChannelHandlerReceiverPO.this);
		delegate.open(address, message);
	}
	
	public void newMessage(R message){
		transfer(this.transformer.transform(message));
	}

	@Override
	public AbstractSource<W> clone() {
		throw new RuntimeException("Clone currently not supported Exception");
	}
	
	@Override
	protected void process_close() {
		delegate.close();
	}
	
}

