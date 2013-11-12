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

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class ProtocolHandlerRegistry implements IProtocolHandlerRegistry {

	static Logger logger = LoggerFactory.getLogger(ProtocolHandlerRegistry.class);

	static private Map<String, IProtocolHandler<?>> handlers = new HashMap<String, IProtocolHandler<?>>();

	static public void register(IProtocolHandler<?> handler) {
		logger.debug("Register new Handler " + handler.getName());
		if (!handlers.containsKey(handler.getName().toLowerCase())) {
			handlers.put(handler.getName().toLowerCase(), handler);
		} else {
			logger.error("Handler with name " + handler.getName()
					+ " already registered");
		}
	}
	
	static public void remove(IProtocolHandler<?> handler){
		logger.debug("Remove handler "+handler.getName());
		handlers.remove(handler.getName().toLowerCase());
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static public IProtocolHandler<?> getInstance(String name, ITransportDirection direction, IAccessPattern access,
            Map<String, String> options, IDataHandler dataHandler) {
        IProtocolHandler<?> ret = handlers.get(name.toLowerCase());
        if (ret != null) {
            return ret.createInstance(direction, access, options, dataHandler);
        }
        logger.error("No handler with name " + name + " found!");
        return null;
    }
	
	public static ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(handlers.keySet());
	}
}
