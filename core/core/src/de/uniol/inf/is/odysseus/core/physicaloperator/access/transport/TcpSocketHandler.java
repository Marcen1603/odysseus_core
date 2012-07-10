/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

public class TcpSocketHandler extends AbstractTransportHandler {

	private String hostname;
	private int port;
	private Socket socket;
	
	
	public TcpSocketHandler() {
		// Needed for DS
	}

	@Override
	public void process_open() throws UnknownHostException, IOException {
		socket = new Socket(this.hostname, this.port);
	}

	@Override
	public void process_close() throws IOException {
		if (socket != null) {
			socket.getInputStream().close();
			socket.close();
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		socket.getOutputStream().write(message);
	}

	@Override
	public ITransportHandler createInstance(Map<String, String> options) {
		TcpSocketHandler th = new TcpSocketHandler();
		th.hostname = options.get("host");
		th.port = Integer.parseInt(options.get("port"));
		return th;
	}

	@Override
	public InputStream getInputStream() {
		if (socket != null) {
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "TCP";
	}
	
}
