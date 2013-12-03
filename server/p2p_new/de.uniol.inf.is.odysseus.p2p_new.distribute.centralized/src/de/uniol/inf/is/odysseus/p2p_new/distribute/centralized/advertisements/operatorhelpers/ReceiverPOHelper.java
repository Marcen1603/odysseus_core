package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;

@SuppressWarnings("rawtypes")
public class ReceiverPOHelper extends AbstractPhysicalOperatorHelper<ReceiverPO> {
	private static String PROTOCOLHANDLER_TAG = "protocolhandler";
	private static String TRANSPORTHANDLER_TAG = "transporthandler";
	
	@Override
	public Class<ReceiverPO> getOperatorClass() {
		return ReceiverPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		ReceiverPO<?,?> rpo = (ReceiverPO<?,?>)o;
		IProtocolHandler protHandler = rpo.getProtocolHandler();
		
		Element protocolHandlerElement = rootDoc.createElement(PROTOCOLHANDLER_TAG);
		toAppendTo.appendChild(protocolHandlerElement);
		ProtocolHandlerHelper.generateProtocolHandlerStatement(protHandler, mimeType, rootDoc, protocolHandlerElement);
		
		Element transportHandlerElement = rootDoc.createElement(TRANSPORTHANDLER_TAG);
		toAppendTo.appendChild(transportHandlerElement);
		TransportHandlerHelper.generateTransportHandlerStatement(((AbstractProtocolHandler)protHandler).getTransportHandler(), mimeType, rootDoc, transportHandlerElement);
		//System.out.println(rootDoc.toString());
		return rootDoc;
	}

	@SuppressWarnings("unchecked")
	@Override
	SimpleImmutableEntry<Integer, ReceiverPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		IProtocolHandler protocolHandler = null;
		TextElement<?> transportHandlerElement = null;
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(PROTOCOLHANDLER_TAG)) {
				protocolHandler = ProtocolHandlerHelper.createProtocolHandlerFromStatement(elem);
			} else if (elem.getName().equals(TRANSPORTHANDLER_TAG)) {
				// can't use it right away, since we have to find the IProtocolHandler first
				transportHandlerElement = elem;
			}
		}
		
		// fetch the transporthandler as well, so that the protocolhandler is registered as a listener there
		// don't need a reference on it, though
		TransportHandlerHelper.createTransportHandlerFromStatement(transportHandlerElement, protocolHandler);
		ReceiverPO result = new ReceiverPO(protocolHandler);
		return new SimpleImmutableEntry<Integer, ReceiverPO>(operatorId,result);
	}
}
