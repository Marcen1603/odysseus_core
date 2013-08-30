package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;

@SuppressWarnings("rawtypes")
public class ReceiverPOHelper extends AbstractPhysicalOperatorHelper<ReceiverPO> {
	private final static String PROTOCOL_HANDLER_NAME_TAG = "protocolhandler";
	private final static String DIRECTION_TAG = "direction";

	@Override
	public Class<ReceiverPO> getOperatorClass() {
		return ReceiverPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		ReceiverPO<?,?> rpo = (ReceiverPO<?,?>)o;
		IProtocolHandler protHandler = rpo.getProtocolHandler();
		String direction = protHandler.getDirection().toString();
		result.appendChild(result.createElement(DIRECTION_TAG,direction));
		//TODO: Exchange and Access-Pattern
		result.appendChild(result.createElement(PROTOCOL_HANDLER_NAME_TAG,protHandler.getName()));
		return result;
	}

	@Override
	SimpleImmutableEntry<Integer, ReceiverPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		@SuppressWarnings("unused")
		ITransportDirection direction = null;
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(DIRECTION_TAG)) {
				direction = elem.getTextValue().equals(ITransportDirection.IN) ? ITransportDirection.IN : ITransportDirection.OUT;
			} else if(elem.getName().equals(PROTOCOL_HANDLER_NAME_TAG)) {
				//TODO: Handle the different protocol-types accordingly, get the appropriate instance from the ProtocolHandlerRegistry etc.
			}
		}
		//ReceiverPO result = new ReceiverPO(...);
		//return new SimpleImmutableEntry<Integer, ReceiverPO>(operatorId,result);
		return null;
	}
}
