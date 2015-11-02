package de.uniol.inf.is.odysseus.net;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class OdysseusNode implements IOdysseusNode {

	private final OdysseusNodeID nodeID;
	private final String nodeName;
	private final Map<String, String> propertyMap = Maps.newConcurrentMap();
	
	public OdysseusNode( OdysseusNodeID nodeID, String nodeName ) {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(nodeName), "NodeName must not be null or empty!");
		
		this.nodeID = nodeID;
		this.nodeName = nodeName;
	}
	
	@Override
	public final OdysseusNodeID getID() {
		return nodeID;
	}

	@Override
	public final String getName() {
		return nodeName;
	}

	@Override
	public void addProperty(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Property key must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "Property value must not be null or empty!");

		propertyMap.put(key, value);
	}

	@Override
	public void removeProperty(String key) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Property key must not be null or empty!");
		
		propertyMap.remove(key);
	}

	@Override
	public Optional<String> getProperty(String key) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Property key must not be null or empty!");

		return Optional.fromNullable(propertyMap.get(key));
	}

	@Override
	public boolean existsProperty(String key) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Property key must not be null or empty!");

		return propertyMap.containsKey(key);
	}

	@Override
	public Collection<String> getProperyKeys() {
		return Lists.newArrayList(propertyMap.keySet());
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof OdysseusNode )) {
			return false;
		}
		if( obj == this ) {
			return true;
		}
		
		OdysseusNode other = (OdysseusNode)obj;
		return this.nodeID.equals(other.nodeID);
	}
	
	@Override
	public int hashCode() {
		return this.nodeID.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{Node ").append(nodeID).append(": ").append(nodeName).append(" [\n");
		for(String propertyKey : propertyMap.keySet() ) {
			sb.append(propertyKey).append("=").append(propertyMap.get(propertyKey)).append("\n");
		}
		sb.append("]}");
		return sb.toString();
	}
}
