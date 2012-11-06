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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.store.OsgiObjectInputStream;

public class ObjectStreamInputHandler extends
		AbstractSocketInputHandler<ObjectInputStream> {

	public ObjectStreamInputHandler() {
		// default constructor needed for Service
	}

	@Override
	public IInputHandler<ObjectInputStream> getInstance(
			Map<String, String> options) {
		return new ObjectStreamInputHandler(options.get("host"),
				Integer.parseInt(options.get("port")), options.get("user"),
				options.get("password"));
	}

	public ObjectStreamInputHandler(String hostname, int port, String user,
			String password) {
		super(hostname, port, user, password);
	}

	private ObjectInputStream channel;

	@Override
	public void init() {
		try {
			super.init();
			this.channel = new OsgiObjectInputStream(getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNext() {
		return true;
		//		try {
//			return channel.available() != 0;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
	}

	@Override
	public ObjectInputStream getNext() {
		return channel;
	}

	@Override
	public void terminate() {
		try {
			channel.close();
			super.terminate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "ObjectStream";
	}

}
