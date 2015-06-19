package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.List;

import de.uniol.inf.is.odysseus.rest.socket.AttributeInformation;

public class ConnectionInformation {
	public String ip;
	public int queryId;
	public String queryName;
	public boolean useName;
	public List<AttributeInformation> attributes;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ConnectionInformation that = (ConnectionInformation) o;

		if (queryId != that.queryId)
			return false;
		if (ip != null ? !ip.equals(that.ip) : that.ip != null)
			return false;
		return !(attributes != null ? !attributes.equals(that.attributes) : that.attributes != null);

	}

	@Override
	public int hashCode() {
		int result = ip != null ? ip.hashCode() : 0;
		result = 31 * result + queryId;
		result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
		return result;
	}
}
