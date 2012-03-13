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
package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IStreamHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class StreamHandlerJxtaImpl implements IStreamHandler {

	static Logger logger = LoggerFactory.getLogger(StreamHandlerJxtaImpl.class);

	private PipeAdvertisement adv;

	private String queryId;
	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	private ILogListener log;

	public StreamHandlerJxtaImpl(PipeAdvertisement adv, String queryId,
			ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.adv = adv;
		this.queryId = queryId;
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
		this.log = thinPeerJxtaImpl.getLog();
		logger.debug("Initialisiere StreamHandler auf dem Thin-Peer: "
				+ this.adv.toString());
	}

	@SuppressWarnings("null")
    @Override
	public void run() {
		JxtaSocket socket = null;
		ObjectInputStream iStream = null;
		InputStream in = null;
		while (socket == null) {
			try {
				socket = new JxtaSocket(thinPeerJxtaImpl.getNetPeerGroup(), adv);
				socket.setSoTimeout(0);
				break;
			} catch (IOException e2) {
				socket = null;
				// e2.printStackTrace();
			}
		}
	//	logger.debug("Connected to "+adv);
		try {
			in = socket.getInputStream();
			iStream = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object o = null;
		while (iStream != null) {
			try {
				o = iStream.readObject();
				if (o != null && (o instanceof Integer) && (((Integer) o).equals(0))) {
					iStream.close();
					socket.close();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				// } catch (ClassNotFoundException e) {
				// e.printStackTrace();
			}
			log.addResult(queryId, o);
		}

	}

}
