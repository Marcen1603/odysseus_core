package de.uniol.inf.is.odysseus.wrapper.kinect.openNI;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

/**
 * Kinect protocol handler, only passes the ByteBuffer generated by the
 * {@link KinectTransportHandler} to a data handler, usualy TupleDataHandler.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectProtocolHandler<T> extends SimpleByteBufferHandler<T> {
    /**
     * Standard constructor.
     */
    public KinectProtocolHandler() {
        super();
    }

    /**
     * Constructor.
     * @param direction
     * Direction of the stream.
     * @param access
     * Access pattern.
     */
    public KinectProtocolHandler(
            ITransportDirection direction, IAccessPattern access,
            Map<String, String> options, IDataHandler<T> dataHandler) {
        super(direction, access, options, dataHandler);
    }

    @Override
    public IProtocolHandler<T> createInstance(
            ITransportDirection direction, IAccessPattern access,
            Map<String, String> options, IDataHandler<T> dataHandler) {
        return new KinectProtocolHandler<>(direction,
                access, options, dataHandler);
    }

    @Override
    public String getName() {
        return "Kinect";
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }


    @Override
    public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
    	if(!(o instanceof KinectProtocolHandler)) {
    		return false;
    	} 
    	// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
    	return true;
    }
}
