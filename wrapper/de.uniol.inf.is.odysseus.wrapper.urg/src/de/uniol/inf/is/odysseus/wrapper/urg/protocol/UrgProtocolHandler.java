package de.uniol.inf.is.odysseus.wrapper.urg.protocol;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class UrgProtocolHandler<T> extends SimpleByteBufferHandler<T> {
	/**
	 * Default constructor.
	 */
	public UrgProtocolHandler() {
		super();
	}
	
	/**
     * Constructor.
     * @param direction
     * Direction of the stream.
     * @param access
     * Access pattern.
	 * @param transfer 
	 * @param dataHandler 
     */
	public UrgProtocolHandler(ITransportDirection direction,
            IAccessPattern access, IDataHandler<T> dataHandler, Map<String, String> options) {
		super (direction, access, options, dataHandler);
	}
	

	@Override
	public IProtocolHandler<T> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options, IDataHandler<T> dataHandler) {
		return new UrgProtocolHandler<>(direction, access, dataHandler, options);
	}

	@Override
	public String getName() {
		return "URG";
	}


    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof UrgProtocolHandler)) {
			return false;
		} else {
			// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
			return true;
		}
	}
}
