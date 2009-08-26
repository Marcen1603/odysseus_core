package de.uniol.inf.is.odysseus.p2p.utils.jxta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
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
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

public class MessageTool {
	
	@SuppressWarnings("unchecked")
	public static Message createSimpleMessage(String namespace,
			String namespace2, String message, PipeAdvertisement responsePipe, PeerAdvertisement peerAdvertisement) {
		Message response = new Message();
		MessageElement respElement = new StringMessageElement(namespace2,
				message, null);
		response.addMessageElement(namespace, respElement);
		
		TextDocumentMessageElement responsePipeAdv = new TextDocumentMessageElement(
	            "pipeAdv0", 
	            (XMLDocument) responsePipe.getDocument(MimeMediaType.XMLUTF8),
	            null);
		response.addMessageElement(namespace,responsePipeAdv);
		
		
		TextDocumentMessageElement peerAdv = new TextDocumentMessageElement(
	            "peerAdv", 
	            (XMLDocument) peerAdvertisement.getDocument(MimeMediaType.XMLUTF8),
	            null);
		response.addMessageElement(namespace,peerAdv);
		
		// JXTA Workaround um MessageElemente ueber Socket zu versenden.
		response.addMessageElement(null, respElement);
		return response;
	}


	public static void sendMessage(PeerGroup netPeerGroup, PipeAdvertisement adv, Message message){
		new MessageSender(netPeerGroup, adv, message).start();
	}
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
	public static Message createSimpleMessage(String namespace, Object ... msgElements){
		
		Message response = new Message();
		
		ArrayList<String> stringElements = new ArrayList<String>();
		ArrayList<PipeAdvertisement> pipeElements = new ArrayList<PipeAdvertisement>();
		ArrayList<Object> objectElements = new ArrayList<Object>();
		
		for (int i=0;i<msgElements.length;i++){
			if (msgElements[i] instanceof String){
				stringElements.add((String) msgElements[i]); 
			}
			else if(msgElements[i] instanceof PipeAdvertisement){
				pipeElements.add((PipeAdvertisement) msgElements[i]);
			}
			else{
				objectElements.add(msgElements[i]);
			}
		}
		
		int help = stringElements.size() / 2;
		
		for (int i=0;i<help;i++){
			MessageElement respElement = new StringMessageElement(stringElements.get(i),
					stringElements.get(i+help), null);
			response.addMessageElement(namespace, respElement);
		}
		
		for (int i=0;i<pipeElements.size();i++){
			TextDocumentMessageElement responsePipeAdv = new TextDocumentMessageElement(
		            "pipeAdv"+i, 
		            (XMLDocument) pipeElements.get(i).getDocument(MimeMediaType.XMLUTF8),
		            null);
			response.addMessageElement(namespace, responsePipeAdv);
		}
		
		for (int i = 0; i < objectElements.size(); i++) {
			
			byte [] data = null;
			if (objectElements.get(i) != null){
				 ByteArrayOutputStream bos = new ByteArrayOutputStream();
			      ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(bos);
					oos.writeObject(objectElements.get(i));
				    oos.flush();
				    oos.close();
				    bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      data = bos.toByteArray();
			}
			
			MessageElement query = new ByteArrayMessageElement("ao"+i, null, data, null);
			response.addMessageElement(namespace, query);
		}
		
		// JXTA Workaround um MessageElemente ueber Socket zu versenden.
		response.addMessageElement(null, new StringMessageElement(stringElements.get(0), stringElements.get(1),null));
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
	
	public void run(){
		while(socket == null){
			try {
				socket = new JxtaSocket(netPeerGroup, null, pipeAdv, 8000, true);
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

