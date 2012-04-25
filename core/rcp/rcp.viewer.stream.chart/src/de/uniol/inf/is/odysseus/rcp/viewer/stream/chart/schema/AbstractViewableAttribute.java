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
