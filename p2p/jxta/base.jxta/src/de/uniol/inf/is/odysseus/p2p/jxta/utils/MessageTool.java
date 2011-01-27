package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.endpoint.TextDocumentMessageElement;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class MessageTool {

	static Logger logger = LoggerFactory.getLogger(MessageTool.class);
	
	public static void sendMessage(PeerGroup netPeerGroup,
			PipeAdvertisement adv, Message message, int maxRetries) {
		new MessageSender(netPeerGroup, adv, message, maxRetries).start();
	}

	@SuppressWarnings("rawtypes")
	public static PipeAdvertisement createResponsePipeFromMessage(String namespace,
			Message msg, int number) {
		MessageElement advElement = msg.getMessageElement(namespace, "pipeAdv"
				+ number);
		XMLDocument theDocument = null;
		try {
			theDocument = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(advElement.getMimeType(),
							advElement.getStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PipeAdvertisement pipeAdv = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(theDocument);

		return pipeAdv;
	}

	@SuppressWarnings("rawtypes")
	public static PeerAdvertisement createPeerAdvertisementFromMessage(String namespace,
			Message msg) {
		MessageElement advElement = msg.getMessageElement(namespace, "peerAdv");
		XMLDocument theDocument = null;
		try {
			theDocument = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(advElement.getMimeType(),
							advElement.getStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PeerAdvertisement peerAdv = (PeerAdvertisement) AdvertisementFactory
				.newAdvertisement(theDocument);

		return peerAdv;
	}

	public static String getMessageElementAsString(String namespace,
			String namespace2, Message msg) {
		MessageElement query = msg.getMessageElement(namespace, namespace2);
		return query.toString();
	}

	@SuppressWarnings("rawtypes")
	public static PipeAdvertisement createPipeAdvertisementFromXml(String xmlAdv) {
		PipeAdvertisement pipeAdv = null;
		StringReader sr = new StringReader(xmlAdv);
		XMLDocument xml = null;
		try {
			xml = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(MimeMediaType.XMLUTF8, sr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pipeAdv = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(xml);

		return pipeAdv;

	}

	public static Message createOdysseusMessage(OdysseusMessageType type,
			Map<String, Object> messageElems) {
		String namespace = type.name();
		return createSimpleMessage(namespace, messageElems);
	}

	@SuppressWarnings("rawtypes")
	public static Message createSimpleMessage(String namespace,
			Map<String, Object> messageElems) {
		Message response = new Message();
		int pipeCounter = 0;
		int aoCounter = 0;

		for (Entry entry : messageElems.entrySet()) {
			Object elem = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				response.addMessageElement(namespace, new StringMessageElement(
						(String) elem, (String) value, null));
			} else if (value instanceof PeerAdvertisement) {
				TextDocumentMessageElement peerAdv = new TextDocumentMessageElement(
						"peerAdv",
						(XMLDocument) ((PeerAdvertisement) value)
								.getDocument(MimeMediaType.XMLUTF8), null);
				response.addMessageElement(namespace, peerAdv);
			}

			else if (value instanceof PipeAdvertisement) {
				TextDocumentMessageElement responsePipeAdv = new TextDocumentMessageElement(
						"pipeAdv" + pipeCounter++,
						(XMLDocument) ((PipeAdvertisement) value)
								.getDocument(MimeMediaType.XMLUTF8), null);
				response.addMessageElement(namespace, responsePipeAdv);
			} else if (value instanceof Subplan) {
				byte[] data = null;
				if (value != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(bos);
						oos.writeObject(value);
						oos.flush();
						oos.close();
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					data = bos.toByteArray();
				}
				MessageElement query = new ByteArrayMessageElement("subplan",
						null, data, null);
				response.addMessageElement(namespace, query);
			} else {
				byte[] data = null;
				if (value != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(bos);
						oos.writeObject(value);
						oos.flush();
						oos.close();
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					data = bos.toByteArray();
				}

				MessageElement query = new ByteArrayMessageElement("ao"
						+ aoCounter++, null, data, null);
				response.addMessageElement(namespace, query);

			}

		}
		// JXTA Workaround um MessageElemente ueber Socket zu versenden.
		response.addMessageElement(null, new StringMessageElement("empty",
				"empty", null));
		return response;
	}

}

class MessageSender extends Thread {

	static Logger logger = LoggerFactory.getLogger(MessageSender.class);
	
	PeerGroup netPeerGroup;
	JxtaSocket socket;
	PipeAdvertisement pipeAdv;
	Message message;
	private int maxRetries;

	public MessageSender(PeerGroup netPeerGroup, PipeAdvertisement pipeAdv,
			Message message, int maxRetries) {
		this.netPeerGroup = netPeerGroup;
		this.pipeAdv = pipeAdv;
		this.message = message;
		this.maxRetries = maxRetries;
	}

	@Override
	public void run() {
		int retries = 0;
		while (socket == null && (retries < maxRetries)) {
			retries++;
			try {
				socket = new JxtaSocket(netPeerGroup, null, pipeAdv, 16000,
						true);
				socket.setSoTimeout(0);
				break;
			} catch (IOException e2) {
				socket = null;
				// e2.printStackTrace();
			}
		}
		
		if (socket == null){
			logger.error("Connection to "+pipeAdv+" was not possible. Aborting");
			return;
		}

		ObjectOutputStream oout = null;
		try {
			oout = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		try {
			if (oout != null) {
				oout.writeObject(message);
				oout.flush();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (oout != null) {
				oout.close();
			}
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
