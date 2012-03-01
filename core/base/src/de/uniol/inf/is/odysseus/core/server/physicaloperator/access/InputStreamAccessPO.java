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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;

public class InputStreamAccessPO<In, Out extends IMetaAttributeContainer<?>> extends
		AbstractInputStreamAccessPO<In, Out>{

	private String host;
	private int port;

	public InputStreamAccessPO(String host, int port,
			IDataTransformation<In, Out> transformation, String user, String password) {
		super(transformation, user, password);
		this.host = host;
		this.port = port;
	}

	public InputStreamAccessPO(InputStreamAccessPO<In, Out> inputStreamAccessPO) {
		super(inputStreamAccessPO);
		this.host = inputStreamAccessPO.host;
		this.port = inputStreamAccessPO.port;
	}

	@Override
	final protected void process_open() throws OpenFailedException {
		done = false;
		if (this.iStream == null) {
			Socket s;
			try {
				s = new Socket(host, port);
				this.iStream = new ObjectInputStream(s.getInputStream());
				// Send login information
				if (user != null && password != null) {
					PrintWriter out = new PrintWriter
						    (s.getOutputStream(), true);
					out.println(user);
					out.println(password);
				}
			} catch (Exception e) {
				throw new OpenFailedException(e.getMessage()+" on "+this.host+" "+this.port);
			}
		}
	}
	
	@Override
	protected void process_close()  {
		if (this.iStream != null){
			try {
				iStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	final synchronized public boolean hasNext() {
		if (buffer != null) {
			return true;
		}
		try {
			In inElem = (In) this.iStream.readObject();
			if (inElem == null) {
				// TODO: Darf eigentlich gar nicht sein ...
				throw new Exception("null element from socket");
			}
			buffer = this.transformation.transform(inElem);
			return true;
		} catch (EOFException e) {
			propagateDone();
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
	public InputStreamAccessPO<In, Out> clone(){
		return new InputStreamAccessPO<In, Out>(this);
	}
	
	@Override
	@SuppressWarnings({"rawtypes"})
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof InputStreamAccessPO)) {
			return false;
		}
		InputStreamAccessPO isapo = (InputStreamAccessPO) ipo;
		if(this.host.equals(isapo.host) && this.port == isapo.port) {
			return true;
		}
		return false;
	}


}
