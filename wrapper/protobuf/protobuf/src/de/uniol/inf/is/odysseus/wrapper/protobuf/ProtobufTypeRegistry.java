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
package de.uniol.inf.is.odysseus.wrapper.protobuf;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;

public class ProtobufTypeRegistry {

	static Logger logger = LoggerFactory.getLogger(ProtobufTypeRegistry.class);

	static private HashMap<String, GeneratedMessage> typeRegistry = new HashMap<String, GeneratedMessage>();

	private ProtobufTypeRegistry(){
	}
	
	static void registerMessageType(String name,
			GeneratedMessage message) {
		logger.trace("Register Messagetype " + message.getClass()
				+ " for Datatypes with name " + name);
		typeRegistry.put(name.toLowerCase(), message);
	}

	static void deregisterMessageType(String name,
			GeneratedMessage message) {
		logger.trace("Deregister Messagetype " + message.getClass()
				+ " for Datatypes with name " + name);
		typeRegistry.remove(name.toLowerCase());
	}

	public static GeneratedMessage getMessageType(String name) {
		return typeRegistry.get(name.toLowerCase());
	}

}
