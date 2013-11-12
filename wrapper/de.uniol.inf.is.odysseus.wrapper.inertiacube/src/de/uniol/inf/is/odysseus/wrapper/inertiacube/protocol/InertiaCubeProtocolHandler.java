package de.uniol.inf.is.odysseus.wrapper.inertiacube.protocol;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class InertiaCubeProtocolHandler<T> extends SimpleByteBufferHandler<T> {
	
	/**
	 * Default constructor.
	 */
	public InertiaCubeProtocolHandler() {
		super();
	}
	
	/**
     * Constructor.
     * @param direction
     * Direction of the stream.
     * @param access
     * Access pattern.
     */
	public InertiaCubeProtocolHandler(ITransportDirection direction, IAccessPattern access,
            Map<String, String> options, IDataHandler<T> dataHandler,
            ITransferHandler<T> transfer) {
        super(direction, access, options, dataHandler, transfer);
	}


	@Override
	public IProtocolHandler<T> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		return new InertiaCubeProtocolHandler<>(direction,
                access, options, dataHandler, transfer);
	}

	@Override
	public String getName() {
		return "InertiaCube";
	}

    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof InertiaCubeProtocolHandler)) {
			return false;
		} else {
			// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
			return true;
		}
	}

}
