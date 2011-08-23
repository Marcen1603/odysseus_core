/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.jxta.peer.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;

/**
 * This class receives incoming messages and delegates them to registered
 * message handler
 * 
 * @author Christian Zillmann, Mart Koehler, Marco Grawunder
 * 
 */

public class SocketServerListener implements ISocketServerListener {

	static Logger logger = LoggerFactory.getLogger(SocketServerListener.class);

	private Map<String, IMessageHandler> messageHandlers = new HashMap<String, IMessageHandler>();
	private IOdysseusPeer peer;

	public SocketServerListener() {
	}

	public SocketServerListener(AbstractOdysseusPeer peer) {
		setPeer(peer);
	}

	@Override
	public synchronized void setPeer(IOdysseusPeer peer) {
		this.peer = peer;
		notifyAll();
	}

	@Override
	public void run() {
		synchronized (this) {
			if (getServerPipeAdvertisement() == null) {
				try {
					logger.debug("No ServerPipeAdvertisement set ... waiting");
					wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		JxtaServerSocket serverSocket = null;
		try {

			serverSocket = new JxtaServerSocket(PeerGroupTool.getPeerGroup(),
					getServerPipeAdvertisement(), 10);
			serverSocket.setSoTimeout(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				final Socket socket = serverSocket.accept();
				socket.setSoTimeout(0);
				if (socket != null) {
					new Thread() {
						public void run() {
							try {
								InputStream in = socket.getInputStream();
								ObjectInputStream oin = new ObjectInputStream(
										in);
								Message msg = null;
								msg = (Message) oin.readObject();
								Iterator<String> it = msg
										.getMessageNamespaces();
								while (it.hasNext()) {
									String namespace = it.next();
									if (namespace != null
											&& namespace.trim().length() > 0) {
										// Existiert ein registrierter Handler,
										// dann
										// sende die Nachricht an diesen weiter
										IMessageHandler h = messageHandlers
												.get(namespace);
										if (h != null) {
											logger.debug("Handle meassage in namespace "
													+ namespace + " with " + h);
											h.handleMessage(msg, namespace);
										} else {
											logger.warn("No handler found for "
													+ namespace);
										}
									}
								}

								oin.close();
								in.close();
								try {
									socket.close();
								} catch (SocketTimeoutException e) {
									// ignore
								}

							} catch (IOException e1) {
								e1.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						};
					}.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public PipeAdvertisement getServerPipeAdvertisement() {
		return (PipeAdvertisement) peer.getServerResponseAddress();
	}

	public synchronized IOdysseusPeer getPeer() {
		return peer;
	}

	public synchronized Collection<IMessageHandler> getMessageHandler() {
		return Collections.unmodifiableCollection(messageHandlers.values());
	}

	@Override
	public synchronized void registerMessageHandler(
			List<IMessageHandler> messageHandler) {
		for (IMessageHandler iMessageHandler : messageHandler) {
			registerMessageHandler(iMessageHandler);
		}
	}

	@Override
	public synchronized void registerMessageHandler(
			IMessageHandler messageHandler) {
		messageHandlers.put(messageHandler.getInterestedNamespace(),
				messageHandler);
	}

	@Override
	public synchronized boolean deregisterMessageHandler(
			IMessageHandler messageHandler) {
		return messageHandlers.remove(messageHandler.getInterestedNamespace()) != null;
	}

}
