package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;

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
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class MessageTool {

	public static void sendMessage(PeerGroup netPeerGroup,
			PipeAdvertisement adv, Message message) {
		new MessageSender(netPeerGroup, adv, message).start();
	}

	@SuppressWarnings("rawtypes")
	public static PipeAdvertisement getResponsePipe(String namespace,
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
	public static PeerAdvertisement getPeerAdvertisement(String namespace,
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

	// Methode auskommentiert da aktuell nicht be�tigt
	// public synchronized static Object getObjectFromMessage(Message msg,
	// String elem){
	// MessageElement advElement = msg.getMessageElement(elem);
	//
	// Object obj = null;
	// try {
	// ByteArrayInputStream bis = new ByteArrayInputStream
	// (advElement.getBytes(true));
	// ObjectInputStream ois = new ObjectInputStream (bis);
	// obj = ois.readObject();
	// }
	// catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// catch (ClassNotFoundException ex) {
	// ex.printStackTrace();
	// }
	// return obj;
	// }

	// Methode auskommentiert da aktuell nicht be�tigt

	// public synchronized static Object getObjectFromMessage(Message msg, int
	// number){
	//
	// MessageElement advElement = msg.getMessageElement("ao"+number);
	//
	// Object obj = null;
	// try {
	// ByteArrayInputStream bis = new ByteArrayInputStream
	// (advElement.getBytes(true));
	// ObjectInputStream ois = new ObjectInputStream (bis);
	// obj = ois.readObject();
	// }
	// catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// catch (ClassNotFoundException ex) {
	// ex.printStackTrace();
	// }
	// return obj;
	// }

	// Auskommentiert da akuell nicht ben�tigt

	// public static ArrayList<POEventType> getEvents(String events){
	//
	// StringTokenizer st = new StringTokenizer(events, ",");
	// ArrayList<POEventType> list = new ArrayList<POEventType>();
	// while (st.hasMoreTokens()) {
	// list.add(POEventType.valueOf(st.nextToken()));
	// }
	// return list;
	//
	// }

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
				System.out.println("Baue subplan bytearray zusammen");
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

	PeerGroup netPeerGroup;

	JxtaSocket socket;

	PipeAdvertisement pipeAdv;

	Message message;

	public MessageSender(PeerGroup netPeerGroup, PipeAdvertisement pipeAdv,
			Message message) {
		this.netPeerGroup = netPeerGroup;
		this.pipeAdv = pipeAdv;
		this.message = message;
	}

	@Override
	public void run() {
		while (socket == null) {
			try {
				socket = new JxtaSocket(netPeerGroup, null, pipeAdv, 16000,
						true);
				socket.setSoTimeout(0);
				break;
			} catch (IOException e2) {
				socket = null;
				e2.printStackTrace();

			}
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
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
