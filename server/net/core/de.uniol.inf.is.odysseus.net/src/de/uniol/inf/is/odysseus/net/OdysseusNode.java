package de.uniol.inf.is.odysseus.net;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class OdysseusNode implements IOdysseusNode {

	private static final long serialVersionUID = -4225329500758311790L;

	private final OdysseusNodeID nodeID;
	private final String nodeName;
	private final Map<String, String> propertyMap = new ConcurrentHashMap<>();
	private final boolean isLocal;

	public OdysseusNode( OdysseusNodeID nodeID, String nodeName, boolean isLocal ) {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(nodeName), "NodeName must not be null or empty!");

		this.nodeID = nodeID;
		this.nodeName = nodeName;
		this.isLocal = isLocal;
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
		sb.append("{").append(nodeID);
		if( isLocal) {
			sb.append("*");
		}
		sb.append(": ").append(nodeName).append("}");

		return sb.toString();
	}

	@Override
	public String toString(boolean verbose){
		String ret = toString();
		if (verbose){
			ret = ret + propertyMap.entrySet();
		}
		return ret;
	}

	@Override
	public boolean isLocal() {
		return isLocal;
	}
}
