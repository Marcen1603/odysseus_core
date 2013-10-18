package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Enumeration;
import java.util.Map;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;

public class TransportHandlerHelper {
	private final static String TRANSPORT_HANDLER_NAME_TAG = "transporthandlername";
	private final static String OPTIONS_TAG = "transporthandleroptions";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument generateTransportHandlerStatement(ITransportHandler transportHandler, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		if(transportHandler instanceof AbstractTransportHandler) {
			AbstractTransportHandler th = (AbstractTransportHandler)transportHandler;
			toAppendTo.appendChild(rootDoc.createElement(TRANSPORT_HANDLER_NAME_TAG,th.getName()));
			toAppendTo.appendChild(rootDoc.createElement(OPTIONS_TAG,th.getOptionsMap().toString()));
		}
		return rootDoc;
	}
	
	@SuppressWarnings("rawtypes")
	public static ITransportHandler createTransportHandlerFromStatement(TextElement<?> statement, IProtocolHandler protHandler) {
		Enumeration<? extends TextElement<?>> elements = statement.getChildren();
		String transportHandlerName = "";
		Map<String,String> options = null;
		
		while(elements.hasMoreElements()) {
			TextElement elem = elements.nextElement();
			if(elem.getName().equals(TRANSPORT_HANDLER_NAME_TAG)) {
				transportHandlerName = elem.getTextValue();
			} else if(elem.getName().equals(OPTIONS_TAG)) {
				options = Tools.fromStringToMap(elem.getTextValue());
			}
		}
		// this assumes, that there are no overlaps between the supported datatypes of different handlers
		ITransportHandler result = TransportHandlerRegistry.getInstance(transportHandlerName,protHandler,options);
		return result;
	}
}
