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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class TransportHandlerRegistry implements ITransportHandlerRegistry {

	static Logger logger = LoggerFactory.getLogger(TransportHandlerRegistry.class);

	static public ITransportHandlerRegistry instance;
	
	private Map<String, ITransportHandler> handlers = new HashMap<String, ITransportHandler>();

	public TransportHandlerRegistry() {
		instance = this;
	}
	
	public void register(ITransportHandler handler) {
		logger.trace("Register new Handler " + handler.getName());
		String name = handler.getName().toLowerCase();
		register(handler, name);
		if (handler instanceof IHasAlias){
			name = ((IHasAlias) handler).getAliasName().toLowerCase();
			register(handler, name);
		}
	}

	private void register(ITransportHandler handler, String name) {
		if (!handlers.containsKey(name)) {
			handlers.put(name, handler);
		} else {
			logger.warn("Handler with name " + name
					+ " already registered");
		}
	}
	
	public void remove(ITransportHandler handler){
		logger.trace("Remove handler "+handler.getName());
		handlers.remove(handler.getName().toLowerCase());
		if (handler instanceof IHasAlias){
			handlers.remove(((IHasAlias)handler).getAliasName().toLowerCase());
		}
	}
	
	@Override
	public ITransportHandler getInstance(String name, IProtocolHandler<?> protocolHandler, OptionMap options){
		ITransportHandler ret = handlers.get(name.toLowerCase());
		if (ret != null){
			return ret.createInstance(protocolHandler, options);
		}
		logger.error("No handler with name "+name+" found!");
		if (logger.isDebugEnabled()){
			logger.debug("Available handler: ");
			for (String k: handlers.keySet()){
				logger.debug(k);
			}
		}
		return null;
	}
	
	@Override
	public ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(handlers.keySet());
	}
		
	
	@Override
	public ITransportHandler getITransportHandlerClass(
			String transportHandler) {
		if (transportHandler != null) {
			ITransportHandler dh = handlers.get(transportHandler.toLowerCase());
			return dh;
		}	
		return null;	
	}
}
