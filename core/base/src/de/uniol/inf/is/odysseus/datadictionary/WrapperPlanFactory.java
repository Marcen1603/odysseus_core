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
/*
 * Created on 19.01.2005
 *
 */
package de.uniol.inf.is.odysseus.datadictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;

/**
 * @author Marco Grawunder
 */
public class WrapperPlanFactory {
	
	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(WrapperPlanFactory.class);
		}
		return _logger;
	}
	
	private static Map<String, ISource<?>> sources = new HashMap<String, ISource<?>>();
	private static Map<String, ISink<?>> sinks = new HashMap<String, ISink<?>>();

	public synchronized static void init() throws Exception {
		//logger.debug("init wrapper");
	}

	public synchronized static ISource<?> getAccessPlan(String uri) {
		ISource<?> po = sources.get(uri);
		return po;
	}

	public synchronized static void putAccessPlan(String uri, ISource<?> s) {
		sources.put(uri, s);
	}
	
	//Fuer P2P
	public synchronized static Map<String, ISource<?>> getSources() {
		return sources;
	}

	public synchronized static void clearSources() {
		sources.clear();
	}

	public static void removeClosedSources() {
		Iterator<Entry<String, ISource<?>>> it = sources.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, ISource<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				it.remove();
			}
		}
	}
	
	public static void removeClosedSinks(){
		Iterator<Entry<String, ISink<?>>> it = sinks.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, ISink<?>> curEntry = it.next();
			if(!curEntry.getValue().hasOwner()){
				it.remove();
			}
		}
	}

	public static ISink<?> getSink(String sinkName) {
		return sinks.get(sinkName);
	}

	public static void putSink(String name, ISink<?> sinkPO) {
		sinks.put(name, sinkPO);
	}

	
	
}
