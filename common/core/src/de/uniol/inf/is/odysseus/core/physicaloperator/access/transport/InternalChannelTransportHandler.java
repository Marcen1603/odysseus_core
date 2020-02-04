package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class InternalChannelTransportHandler extends AbstractPushTransportHandler {

	public static final String NAME = "InternalChannel";
	public static final String CHANNEL_NAME = "internalchannel.name";
	private String channelName;
	private TransferQueue<IStreamObject<IMetaAttribute>> queue;
	private Receiver receiver;

	private static final Map<String, TransferQueue<IStreamObject<IMetaAttribute>>> channelQueues = new HashMap<>();

	public InternalChannelTransportHandler() {
		super();
	}

	public InternalChannelTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		options.checkRequiredException(CHANNEL_NAME);
		this.channelName = options.get(CHANNEL_NAME);
		synchronized (InternalChannelTransportHandler.class) {
			if (!channelQueues.containsKey(channelName)) {
				LinkedTransferQueue<IStreamObject<IMetaAttribute>> queue = new LinkedTransferQueue<>();
				channelQueues.put(channelName, queue);
			}
		}
		queue = channelQueues.get(channelName);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new InternalChannelTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		receiver = new Receiver(this, queue);
		receiver.start();
	}

	@Override
	public void processOutOpen() throws IOException {
		// Nothing to do
	}

	@Override
	public void processInClose() throws IOException {
		if (receiver != null) {
			receiver.stopReceiver();
		}
	}

	@Override
	public void processOutClose() throws IOException {
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new UnsupportedOperationException("Send with bytes not supported.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(Object message) throws IOException {
		if (message instanceof IStreamObject) {
			try {
				queue.put((IStreamObject<IMetaAttribute>) message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}

class Receiver extends Thread {
	final InternalChannelTransportHandler handler;
	final TransferQueue<IStreamObject<IMetaAttribute>> queue;
	boolean interrupted = false;

	public Receiver(InternalChannelTransportHandler handler, TransferQueue<IStreamObject<IMetaAttribute>> queue) {
		this.handler = handler;
		this.queue = queue;
	}

	@Override
	public void run() {
		this.interrupted = false;
		IStreamObject<IMetaAttribute> streamable;

		while (!interrupted) {
			try {
				while ((streamable = queue.poll(1000, TimeUnit.MILLISECONDS)) != null) {
					handler.fireProcess(streamable);
				}
			} catch (
			InterruptedException e) {
				interrupted = true;
				e.printStackTrace();
			}
		}
	}

	public void stopReceiver() {
		this.interrupted = true;
	}

}
