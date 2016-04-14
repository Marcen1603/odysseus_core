package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class MemoryTransportHandler extends AbstractPullTransportHandler {

	private static final String NAME = "Memory";
	private static final String STORE = "store";
	private static final String CLEARSTORE = "clearstore";
	
	private static final Map<String, List<Object>> stores = new HashMap<>(); 
	
	private String store;
	private List<Object> myStore;
	
	ObjectInputStream stream;
	
	public MemoryTransportHandler(){	
	}

	public MemoryTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		init(options);
	}

	private void init(OptionMap options) {
		options.checkRequiredException(STORE);
		
		store = options.get(STORE);
		// add store only if not present or clear store is set
		if (options.getBoolean(CLEARSTORE, true) || !stores.containsKey(store)){
			stores.put(store, new LinkedList<>());
		}
		myStore = stores.get(store);
		
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new MemoryTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		stream = null;
		try {
			stream = new MyObjectInputStream(myStore);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void processOutOpen() throws IOException {
		stream.close();
	}

	@Override
	public void processInClose() throws IOException {
		// Close InputStream
		
	}

	@Override
	public void processOutClose() throws IOException {
		// Nothing to do
		
	}

	@Override
	public void send(byte[] message) throws IOException {
		
	}

	@Override
	public void send(Object message) throws IOException {
		myStore.add(message);
	}
	
	@Override
	public InputStream getInputStream() {
		return stream;
	}

	@Override
	public OutputStream getOutputStream() {
		// Not needed
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}

class MyObjectInputStream extends ObjectInputStream{

	private List<Object> store;
	private int pos = 0;

	protected MyObjectInputStream(List<Object> myStore) throws SecurityException, IOException {
		super();
		this.store = myStore;
	}

	@Override
	protected Object readObjectOverride() throws IOException, ClassNotFoundException {
		return store.get(pos++);
	}
	
	@Override
	public void close() throws IOException {
		pos = 0;
		super.close();
	}
	
}
