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
package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class ObjectSinkStreamHandler implements ISinkStreamHandler<Object> {
	OutputStream outS;
	ObjectOutputStream dout;
	Socket socket;

	public ObjectSinkStreamHandler(Socket socket) {
		this.socket = socket;
		try {
			outS = socket.getOutputStream();
			dout = new ObjectOutputStream(outS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void transfer(Object o) throws IOException {
		dout.writeObject(o);
		dout.flush();

	}

	public void done() throws IOException {
		dout.writeObject(Integer.valueOf(0));
		dout.flush();
		dout.close();
		socket.close();
	}

}