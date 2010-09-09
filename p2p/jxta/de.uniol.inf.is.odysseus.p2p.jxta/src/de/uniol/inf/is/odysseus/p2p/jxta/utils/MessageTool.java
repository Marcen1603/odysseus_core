package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

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
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

@SuppressWarnings("unchecked")
public class MessageTool {
	
	


	public static void sendMessage(PeerGroup netPeerGroup, PipeAdvertisement adv, Message message){
		new MessageSender(netPeerGroup, adv, message).start();
	}
	
	public static PipeAdvertisement getResponsePipe(String namespace, Message msg, int number){
		MessageElement advElement = msg.getMessageElement(
				namespace, "pipeAdv"+number);
		XMLDocument theDocument = null;
		try {
			theDocument = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(advElement
							.getMimeType(), advElement
							.getStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PipeAdvertisement pipeAdv = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(theDocument);
		
		return pipeAdv;
	}
	
	public static PeerAdvertisement getPeerAdvertisement(String namespace, Message msg){
		MessageElement advElement = msg.getMessageElement(
				namespace, "peerAdv");
		XMLDocument theDocument = null;
		try {
			theDocument = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(advElement
							.getMimeType(), advElement
							.getStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PeerAdvertisement peerAdv = (PeerAdvertisement) AdvertisementFactory
				.newAdvertisement(theDocument);
		
		return peerAdv;
	}
	
	public static String getMessageElementAsString(String namespace, String namespace2, Message msg){
		MessageElement query = msg
		.getMessageElement(namespace, namespace2);
		return query.toString();
	}
	

	public static PipeAdvertisement createPipeAdvertisementFromXml(String xmlAdv){
		PipeAdvertisement pipeAdv = null;
		StringReader sr = new StringReader(xmlAdv);
		XMLDocument xml = null;
		try {
			xml = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(MimeMediaType.XMLUTF8,
							sr);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pipeAdv = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(xml);
		
		return pipeAdv;
		
	}
	
	
	public synchronized static Object getObjectFromMessage(Message msg, String elem){
		MessageElement advElement = msg.getMessageElement(elem);
		
		Object obj = null;
		  try {
		    ByteArrayInputStream bis = new ByteArrayInputStream (advElement.getBytes(true));
		    ObjectInputStream ois = new ObjectInputStream (bis);
		    obj = ois.readObject();
		  }
		  catch (IOException ex) {
		    ex.printStackTrace();
		  }
		  catch (ClassNotFoundException ex) {
		    ex.printStackTrace();
		  }
		return obj;
	}
	
	public synchronized static Object getObjectFromMessage(Message msg, int number){
		
		MessageElement advElement = msg.getMessageElement("ao"+number);
		
		Object obj = null;
		  try {
		    ByteArrayInputStream bis = new ByteArrayInputStream (advElement.getBytes(true));
		    ObjectInputStream ois = new ObjectInputStream (bis);
		    obj = ois.readObject();
		  }
		  catch (IOException ex) {
		    ex.printStackTrace();
		  }
		  catch (ClassNotFoundException ex) {
		    ex.printStackTrace();
		  }
		return obj;
	}
	
	public static ArrayList<POEventType> getEvents(String events){

		StringTokenizer st = new StringTokenizer(events, ",");
		ArrayList<POEventType> list = new ArrayList<POEventType>();
		while (st.hasMoreTokens()) { 	
			list.add(POEventType.valueOf(st.nextToken()));
		}
		return list;

	}
	
	
	public static Message createSimpleMessage(String namespace, Map<String,Object> messageElements) {
		Message response = new Message();
		int pipeCounter = 0;
		int aoCounter = 0;
		
		for(String elem : messageElements.keySet()) {
			if(messageElements.get(elem) instanceof String) {
				response.addMessageElement(namespace, new StringMessageElement(elem, (CharSequence) messageElements.get(elem), null));
			}
			else if(messageElements.get(elem) instanceof PeerAdvertisement) {
				TextDocumentMessageElement peerAdv = new TextDocumentMessageElement(
			            "peerAdv", 
			            (XMLDocument) ((PeerAdvertisement)messageElements.get(elem)).getDocument(MimeMediaType.XMLUTF8),
			            null);
				response.addMessageElement(namespace,peerAdv);
			}
			
			
			
			else if(messageElements.get(elem) instanceof PipeAdvertisement) {
				TextDocumentMessageElement responsePipeAdv = new TextDocumentMessageElement(
			            "pipeAdv"+pipeCounter++, 
			            (XMLDocument) ((PipeAdvertisement)messageElements.get(elem)).getDocument(MimeMediaType.XMLUTF8),
			            null);
				response.addMessageElement(namespace, responsePipeAdv);
			}
			else if(messageElements.get(elem) instanceof Subplan) {
				byte [] data = null;
				if (messageElements.get(elem) != null){
					 ByteArrayOutputStream bos = new ByteArrayOutputStream();
				      ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(bos);
						oos.writeObject(messageElements.get(elem));
					    oos.flush();
					    oos.close();
					    bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				      data = bos.toByteArray();
				}
				System.out.println("Baue subplan bytearray zusammen");
				MessageElement query = new ByteArrayMessageElement("subplan", null, data, null);
				response.addMessageElement(namespace, query);
			}
			else {
				byte [] data = null;
				if (messageElements.get(elem) != null){
					 ByteArrayOutputStream bos = new ByteArrayOutputStream();
				      ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(bos);
						oos.writeObject(messageElements.get(elem));
					    oos.flush();
					    oos.close();
					    bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				      data = bos.toByteArray();
				}
				
				MessageElement query = new ByteArrayMessageElement("ao"+aoCounter++, null, data, null);
				response.addMessageElement(namespace, query);
				
			}
			
		}
		// JXTA Workaround um MessageElemente ueber Socket zu versenden.
		response.addMessageElement(null, new StringMessageElement("empty", "empty",null));
		return response;
	}
	
}

class MessageSender extends Thread{
	
	PeerGroup netPeerGroup;
	
	JxtaSocket socket;
	
	PipeAdvertisement pipeAdv;
	
	Message message;
	
	public MessageSender(PeerGroup netPeerGroup, PipeAdvertisement pipeAdv, Message message){
		this.netPeerGroup = netPeerGroup;
		this.pipeAdv = pipeAdv;
		this.message = message;
	}
	
	@Override
	public void run(){
		while(socket == null){
			try {
				socket = new JxtaSocket(netPeerGroup, null, pipeAdv, 16000, true);
				break;
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				socket = null;
				e2.printStackTrace();
				
			}
		}
	
		ObjectOutputStream oout = null;
		try {
			oout = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			oout.writeObject(message);
			oout.flush();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oout.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

