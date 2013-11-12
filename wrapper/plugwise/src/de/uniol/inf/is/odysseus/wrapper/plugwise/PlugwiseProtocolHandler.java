package de.uniol.inf.is.odysseus.wrapper.plugwise;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class PlugwiseProtocolHandler<T> extends AbstractProtocolHandler<T> {

	public static final String NAME = "Plugwise";
	public static final String CIRCLE_MAC = "circle_mac";
	
	@SuppressWarnings("unused")
	private String circleMac = null;
	
	public PlugwiseProtocolHandler() {
	}
	
	public PlugwiseProtocolHandler(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler) {
		super(direction, access, dataHandler);
		init(options);
	}
	
	@Override
	public ITransportExchangePattern getExchangePattern() {
		// TODO: Is this always correct?
		return ITransportExchangePattern.InOptionalOut;
	}
	
	
	private void init(Map<String, String> options) {
		if (options.containsKey(CIRCLE_MAC)){
			circleMac = options.get(CIRCLE_MAC);
		}else{
			throw new IllegalArgumentException("No Circle Mac defined!");
		}
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		byte[] init_0 = {0x00, 0x0a};
		
		byte[] chksum = Checksum.getCRC16_bytes(init_0);

		getTransportHandler().send(init_0);
		getTransportHandler().send(chksum);
		

//		
//		byte[] init = {0x00, 0x0a, (byte) 0xb4, 0x3c};
//		byte[] endline = {0x0d, 0x0a};
//		
//		
//		
//		String startMessage = "";
//		byte[] header = {0x05,0x05,0x03,0x03};
//		int powerchangecode = 0017;
//		
//		int on = 01;
//		ByteBuffer command = ByteBuffer.allocate(800);
//		command.putInt(powerchangecode);
//		command.put(this.circleMac.getBytes());
//		command.putInt(on);
//		command.flip();
//		
//		ByteBuffer buffer = ByteBuffer.allocate(1024);
//		buffer.put(header);
//		buffer.put(command);
//		buffer.put(Checksum.getCRC16_bytes(command.array()));
//		buffer.put(endline);
//		
//		getTransportHandler().send(buffer.array());
	}
	
 
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler) {
		return new PlugwiseProtocolHandler<>(direction, access, options, dataHandler);

	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(ByteBuffer message) {
		// TODO Auto-generated method stub
		message.limit();
		byte[] out = message.array();
		for (byte b:out){
			System.out.print(b);
		}
		System.out.println();
	}

	@Override
	public void process(String[] message) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}

}
