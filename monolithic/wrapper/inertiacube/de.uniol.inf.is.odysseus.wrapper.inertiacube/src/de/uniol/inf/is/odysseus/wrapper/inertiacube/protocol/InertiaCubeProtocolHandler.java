package de.uniol.inf.is.odysseus.wrapper.inertiacube.protocol;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class InertiaCubeProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends SimpleByteBufferHandler<T> {
	
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
			OptionMap options, IStreamObjectDataHandler<T> dataHandler) {
        super(direction, access, options, dataHandler);
	}


	@Override
	public IProtocolHandler<T> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options, IStreamObjectDataHandler<T> dataHandler) {
		return new InertiaCubeProtocolHandler<>(direction,
                access, options, dataHandler);
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
		} 
		// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
		return true;
	}

}
