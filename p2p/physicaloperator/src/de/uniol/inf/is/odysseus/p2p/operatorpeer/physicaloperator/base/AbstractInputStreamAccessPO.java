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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;

abstract public class AbstractInputStreamAccessPO <In, Out extends IMetaAttributeContainer<?>> extends
AbstractIterableSource<Out>{
	
	protected ObjectInputStream iStream;
	final protected String user;
	final protected String password;
	protected Out buffer;
	protected boolean done = false;
	
	public AbstractInputStreamAccessPO(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	public AbstractInputStreamAccessPO(
			AbstractInputStreamAccessPO<In, Out> inputStreamAccessPO) {
		this.user = inputStreamAccessPO.user;
		this.password = inputStreamAccessPO.password;
	}

	@Override
	protected void process_done() {
		done = true;
		try {
			this.iStream.close();
		} catch (IOException e) {
			// we are done, we don't care anymore for exceptions
		}
	}

	@Override
	synchronized public void transferNext() {
		if (buffer == null) {
			if (!hasNext()) {
				propagateDone();// TODO wie soll mit diesem fehler umgegangen
				// werden
				throw new NoSuchElementException();
			}
		}
		transfer(buffer);
		buffer = null;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	

}
