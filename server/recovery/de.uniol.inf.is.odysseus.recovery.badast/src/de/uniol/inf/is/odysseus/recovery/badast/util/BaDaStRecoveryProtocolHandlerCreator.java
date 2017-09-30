package de.uniol.inf.is.odysseus.recovery.badast.util;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.util.Constants;

/**
 * Helper class to create a protocol handler for BaDaSt.
 *
 * @author Michael
 *
 */
public class BaDaStRecoveryProtocolHandlerCreator {

	/**
	 * Creates a new protocol handler.
	 *
	 * @param access
	 *            The access to the source, which is recorded by BaDaSt.
	 * @param transferHandler
	 *            The transfer handler for the objects from public subscribe
	 *            system in recovery mode. Not for the objects from the source
	 *            operator.
	 */
	@SuppressWarnings("unchecked")
	public static <StreamObject extends IStreamObject<IMetaAttribute>> IProtocolHandler<StreamObject> createProtocolHandler(
			AbstractAccessAO access, ITransferHandler<StreamObject> transferHandler) {
		IStreamObjectDataHandler<StreamObject> dataHandler = (IStreamObjectDataHandler<StreamObject>) DataHandlerRegistry
				.getStreamObjectDataHandler(access.getDataHandler(), access.getOutputSchema());
		dataHandler.setMetaAttribute(access.getLocalMetaAttribute());
		OptionMap options = new OptionMap(access.getOptions());
		IAccessPattern pattern;
		if (Constants.GENERIC_PULL.equalsIgnoreCase(access.getWrapper())) {
			// XXX AccessPattern: Better way to determine the AccessPattern?
			pattern = IAccessPattern.PULL;
		} else {
			pattern = IAccessPattern.PUSH;
		}
		IProtocolHandler<StreamObject> protocolHandler = (IProtocolHandler<StreamObject>) ProtocolHandlerRegistry
				.getInstance(access.getProtocolHandler(), ITransportDirection.IN, pattern, options, dataHandler);
		protocolHandler.setTransfer(transferHandler);
		return protocolHandler;
	}

}