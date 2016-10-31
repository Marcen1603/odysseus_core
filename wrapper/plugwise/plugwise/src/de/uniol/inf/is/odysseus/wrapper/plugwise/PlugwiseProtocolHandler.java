package de.uniol.inf.is.odysseus.wrapper.plugwise;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class PlugwiseProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {

	public static final String NAME = "Plugwise";
	public static final String CIRCLE_MAC = "circle_mac";

	@SuppressWarnings("unused")
	private String circleMac = null;

	public PlugwiseProtocolHandler() {
	}

	public PlugwiseProtocolHandler(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		super(direction, access, dataHandler, options);
		init_internal();
	}

	private void init_internal() {
		if (optionsMap.containsKey(CIRCLE_MAC)) {
			circleMac = optionsMap.get(CIRCLE_MAC);
		} else {
			throw new IllegalArgumentException("No Circle Mac defined!");
		}
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		}
		return ITransportExchangePattern.OutOnly;
	}



	@SuppressWarnings("unused")
	private byte[] getMessage(byte[] id, byte[] mac, byte[] args){


		byte[] header = { 0x05, 0x05, 0x03, 0x03 };
		byte[] footer = { 0x0d, 0x0a };

		int messageLength = id.length;
		if (mac != null){
			messageLength+=mac.length;
		}
		if(args != null){
			messageLength+=args.length;
		}

		byte[] message = new byte[messageLength];
		System.arraycopy(id, 0, message, 0, id.length);
		int curPos = id.length;
		if (mac!=null){
			System.arraycopy(mac, 0, message, curPos, mac.length);
			curPos += mac.length;
		}

		if (args!=null){
			System.arraycopy(args, 0, message, curPos, args.length);
			curPos += args.length;
		}


		byte[] chksum = Checksum.getCRC16_bytes(message);

		byte[] toSend = new byte[header.length+footer.length+id.length+chksum.length];

		System.arraycopy(header, 0, toSend, 0, header.length);
		System.arraycopy(message, 0, toSend, header.length, message.length);
		System.arraycopy(chksum, 0, toSend, header.length+message.length, chksum.length);
		System.arraycopy(footer, 0, toSend, header.length+message.length+chksum.length, footer.length);
		return toSend;

	}


	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		// init:
//		byte[] id = { 0x00, 0x0a };
//		byte[] toSend = getMessage(id,null,null);
//		getTransportHandler().send(toSend);

		byte[] init = {0x00, 0x0a, (byte) 0xb4, (byte) 0xc3};
		getTransportHandler().send(init);

		//0017000D6F0000B1B9A90180CC
		byte[] on1 = {0x00,0x17,0x00,0x0D,0x6F,0x00,0x00,(byte) 0xB1,(byte) 0xB9,(byte) 0xA9,0x01,(byte) 0x80,(byte) 0xCC};
		getTransportHandler().send(on1);
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		return new PlugwiseProtocolHandler<>(direction, access, options,
				dataHandler);

	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
		int size = message.limit();
		byte[] out = new byte[size];
		message.get(out, 0,	size);
		for (byte b : out) {
			System.out.print(Integer.toHexString(b));
		}
		System.out.println();
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}

}
