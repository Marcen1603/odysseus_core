package de.uniol.inf.is.odysseus.p2p.jxta.peer.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IServerSocketConnectionHandler;

public class ServerSocketConnectionHandler implements IServerSocketConnectionHandler{

		static Logger logger = LoggerFactory.getLogger(ServerSocketConnectionHandler.class); 
	
		private Map<String, IMessageHandler> messageHandler;
	
		private Socket socket;

		public Socket getSocket() {
			return socket;
		}


		public void setSocket(Socket socket) {
			this.socket = socket;
		}


		public ServerSocketConnectionHandler(Socket socket, Map<String, IMessageHandler> messageHandler) {
			this.socket = socket;
			this.messageHandler = messageHandler;
		}

		@Override
		public void run() {
			sendAndReceiveData(socket);
		}
		
		private void sendAndReceiveData(Socket socket) {
			try {
				OutputStream out = socket.getOutputStream();
				InputStream in = socket.getInputStream();
				ObjectInputStream oin = new ObjectInputStream(in);
				Message msg = null;
				msg = (Message) oin.readObject();
				Iterator<String> it = msg.getMessageNamespaces();
				String namespace="";
				while (it.hasNext()) {
					namespace = it.next();
					logger.debug("ERHALTE NACHRICHT: "+namespace);
					//Existiert ein registrierter Handler, dann sende die Nachricht an diesen weiter
					if(getMessageHandlerList().containsKey(namespace)) {
						getMessageHandlerList().get(namespace).handleMessage(msg, namespace);
					}
				}
				
				oin.close();
				out.close();
				in.close();

				socket.close();
				

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}


		@Override
		public Map<String, IMessageHandler> getMessageHandlerList() {
			return this.messageHandler;
		}


		@Override
		public void setMessageHandlerList(
				Map<String, IMessageHandler> messageHandler) {
			this.messageHandler = messageHandler;
		}
}
