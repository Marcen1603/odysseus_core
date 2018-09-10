package de.uniol.inf.is.odysseus.wrapper.urg.protocol;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class UrgProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends SimpleByteBufferHandler<T> {
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
            IAccessPattern access, IStreamObjectDataHandler<T> dataHandler, OptionMap options) {
		super (direction, access, options, dataHandler);
	}
	

	@Override
	public IProtocolHandler<T> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options, IStreamObjectDataHandler<T> dataHandler) {
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
