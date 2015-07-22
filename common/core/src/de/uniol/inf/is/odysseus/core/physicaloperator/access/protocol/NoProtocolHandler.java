package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class NoProtocolHandler<T> extends AbstractProtocolHandler<T> {

	public static final String NAME = "None";

	public NoProtocolHandler(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IDataHandler<T> dataHandler) {
		super(direction, access, dataHandler, options);
	}
	
	public NoProtocolHandler() {
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IDataHandler<T> dataHandler) {
		return new NoProtocolHandler<>(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasNext() throws IOException {
		return ((IIteratable<T>) getTransportHandler()).hasNext();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getNext() throws IOException {
		T next = ((IIteratable<T>) getTransportHandler())
				.getNext();
		if (next != null) {
			return getDataHandler().readData(next);
		} else {
			return null;
		}
	}

	@Override
	public void write(T object) throws IOException {
		getTransportHandler().send(object);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOut;
    }
}
