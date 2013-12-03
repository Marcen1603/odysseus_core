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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractViewableAttribute implements IViewableAttribute {

	final private int port;
	
	public AbstractViewableAttribute(int port) {
		this.port = port;
	}

	@Override
	public int getPort() {
		return port;
	}
	
	/**
	 * Split attributes by port number
	 * @param attributes
	 * @return
	 */
	static public Map<Integer, Set<IViewableAttribute>> getAttributesAsPortMapSet(
			List<IViewableAttribute> attributes) {
		Map<Integer, Set<IViewableAttribute>> ret = new HashMap<Integer, Set<IViewableAttribute>>();
		for (IViewableAttribute a: attributes){
			Set<IViewableAttribute> set = ret.get(a.getPort());
			if (set == null){
				set = new HashSet<IViewableAttribute>();
				ret.put(a.getPort(), set);
			}
			set.add(a);
		}
		return ret;
	}


	/**
	 * Split attributes by port number
	 * @param attributes
	 * @return
	 */
	static public Map<Integer, List<IViewableAttribute>> getAttributesAsPortMapList(
			List<IViewableAttribute> attributes) {
		Map<Integer, List<IViewableAttribute>> ret = new HashMap<Integer, List<IViewableAttribute>>();
		for (IViewableAttribute a: attributes){
			List<IViewableAttribute> set = ret.get(a.getPort());
			if (set == null){
				set = new LinkedList<IViewableAttribute>();
				ret.put(a.getPort(), set);
			}
			set.add(a);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
