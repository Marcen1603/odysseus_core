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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class ProtocolHandlerRegistry implements IProtocolHandlerRegistry {
	

	public static ProtocolHandlerRegistry instance;
	
	static Logger logger = LoggerFactory
			.getLogger(ProtocolHandlerRegistry.class);

	static private Map<String, IProtocolHandler<?>> handlers = new HashMap<String, IProtocolHandler<?>>();

	public ProtocolHandlerRegistry() {
		instance = this;
	}
	
	public void register(IProtocolHandler<?> handler) {
		try {
			logger.trace("Register new Handler " + handler.getName());
			if (!handlers.containsKey(handler.getName().toLowerCase())) {
				handlers.put(handler.getName().toLowerCase(), handler);
			} else {
				logger.error("Handler with name " + handler.getName()
						+ " already registered");
			}
		} catch (Exception e) {
			logger.error("Cannot register handler " + handler);
		}
	}

	public void remove(IProtocolHandler<?> handler) {
		logger.trace("Remove handler " + handler.getName());
		handlers.remove(handler.getName().toLowerCase());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IProtocolHandler<?> getInstance(String name,
			ITransportDirection direction, IAccessPattern access,
			OptionMap options, IStreamObjectDataHandler dataHandler) {
		IProtocolHandler<?> ret = handlers.get(name.toLowerCase());
		if (ret != null) {
			return ret.createInstance(direction, access, options, dataHandler);
		}
		logger.error("No handler with name " + name + " found!");
		return null;
	}

	@Override
	public ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(handlers.keySet());
	}
	
	@Override
	public IProtocolHandler<?> getIProtocolHandlerClass(
			String protocolHandler) {
		if (protocolHandler != null) {
			IProtocolHandler<?> dh = handlers.get(protocolHandler.toLowerCase());
			return dh;
		}	
		return null;	
	}

}
