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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class P2PInputStreamAccessPO<In, Out extends IMetaAttributeContainer<?>>
		extends AbstractInputStreamAccessPO<In, Out> {

	static Logger logger = LoggerFactory.getLogger(P2PInputStreamAccessPO.class);
	
	private PipeAdvertisement adv;
	private JxtaSocket socket;

	private ConnectionHandler con;
	private PeerGroup peerGroup;

	class ConnectionHandler extends Thread {

		@Override
		public void run() {
			while (socket == null) {
				try {
					socket = new JxtaSocket(getPeerGroup(), null, adv, 15000,
							true);
					
					logger.debug("JxtaSocket erzeugt " + adv.toString());
					break;
				} catch (IOException e2) {
					// Timeout passiert weil ein anderer Peer seinen
					// P2PPipePOfalse
					// noch nicht geöffnet hat, daher wird es solange probiert
					// bis der P2PPipePO
					// geöffnet ist. Hier noch sinnvolles Abruchskriterium
					// nötig.
					socket = null;
					e2.printStackTrace();
				}
			}
		}

	}

	public P2PInputStreamAccessPO(String adv, PeerGroup peergroup, String user, String password) {
		super(user, password);
		this.adv = MessageTool.createPipeAdvertisementFromXml(adv);
		this.peerGroup = peergroup;
		logger.debug("Initialisiere P2PInputStreamAccessPO: "
				+ this.adv.toString());

	}

	@Override
	protected void process_open() throws OpenFailedException {

		done = false;
		con = new ConnectionHandler();
		con.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean hasNext() {
		if (socket == null) {
			if (buffer != null) {
				return true;
			}
			return false;
		}
		if (buffer != null) {
			return true;
		}
		try {
			if (this.iStream == null) {
				InputStream in = socket.getInputStream();
				this.iStream = new ObjectInputStream(in);
			}
			Object o = this.iStream.readObject();
			if ((o instanceof Integer) && (((Integer) o).equals(0))) {
				propagateDone();
				return false;
			} else if (o instanceof Tuple) {
				logger.debug("tuple received "+adv.getID().toString() +" "+o);
			}

			In inElem = (In) o;

			if (inElem == null) {
				throw new Exception("null element from socket");
			}
			buffer = (Out) inElem;
			return true;
		} catch (EOFException e) {
			e.printStackTrace();
			propagateDone();
			return false;
		} catch (IOException e) {
			// Hier müsste er dann auf einen HotPeer wechseln
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO fehlerzustand irgendwie signalisieren?
			propagateDone();
			return false;
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}


	@Override
	protected void process_done() {
		done = true;
		try {
			this.iStream.close();
			socket.close();
		} catch (IOException e) {
			// we are done, we don't care anymore for exceptions
		}
	}


	public PeerGroup getPeerGroup() {
		return peerGroup;
	}
	
	@Override
	public P2PInputStreamAccessPO<In, Out> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}


}
