package de.uniol.inf.is.odysseus.core.server.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IIteratable;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class StoreTransportHandler extends AbstractPullTransportHandler implements IIteratable<IStreamObject<IMetaAttribute>>{

	private static final String TYPE = "type";

	public static String NAME = "STORE";

	private IStore<String, IStreamObject<IMetaAttribute>> store;

	private List<String> keySet;
	private Iterator<String> iter;

	public StoreTransportHandler() {
	}

	public StoreTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		initOptions(options);
	}

	@SuppressWarnings("unchecked")
	private void initOptions(OptionMap options) {
		options.checkRequiredException(TYPE);
		String storeType = options.get(TYPE);
		store = (IStore<String, IStreamObject<IMetaAttribute>>) StoreRegistry.createStore(storeType, options);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new StoreTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		keySet = new ArrayList<>(store.keySet());
		Collections.sort(keySet);
		keySet.iterator();
	}

	@Override
	public void processOutOpen() throws IOException {
		// nothing to do
	}

	@Override
	public void processInClose() throws IOException {
		keySet.clear();
	}

	@Override
	public void processOutClose() throws IOException {
		// nothing to do
	}

	@Override
	public void send(byte[] message) throws IOException {

	}


	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}


	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public IStreamObject<IMetaAttribute> getNext() {
		return store.get(iter.next());
	}


	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}


}
