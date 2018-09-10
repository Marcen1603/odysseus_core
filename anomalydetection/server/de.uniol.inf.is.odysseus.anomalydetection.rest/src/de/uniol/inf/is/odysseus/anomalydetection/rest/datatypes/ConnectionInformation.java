package de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes;

import java.util.List;

import de.uniol.inf.is.odysseus.rest.socket.AttributeInformation;

/**
 * Information which is needed to connect to the right source (needed by the
 * client to connect to Odysseus)
 * 
 * @author Tobias Brandt
 *
 */
public class ConnectionInformation {

	private String name;
	private String description;
	private String ip;
	private int queryId;
	private String queryName;
	private boolean useName;
	private List<AttributeInformation> attributes;
	private String operatorName;
	private int operatorOutputPort;
	private boolean useOperatorOutputPort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public boolean isUseName() {
		return useName;
	}

	public void setUseName(boolean useName) {
		this.useName = useName;
	}

	public List<AttributeInformation> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeInformation> attributes) {
		this.attributes = attributes;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public int getOperatorOutputPort() {
		return operatorOutputPort;
	}

	public void setOperatorOutputPort(int operatorOutputPort) {
		this.operatorOutputPort = operatorOutputPort;
	}

	public boolean isUseOperatorOutputPort() {
		return useOperatorOutputPort;
	}

	public void setUseOperatorOutputPort(boolean useOperatorOutputPort) {
		this.useOperatorOutputPort = useOperatorOutputPort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((operatorName == null) ? 0 : operatorName.hashCode());
		result = prime * result + operatorOutputPort;
		result = prime * result + queryId;
		result = prime * result + ((queryName == null) ? 0 : queryName.hashCode());
		result = prime * result + (useName ? 1231 : 1237);
		result = prime * result + (useOperatorOutputPort ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectionInformation other = (ConnectionInformation) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (operatorName == null) {
			if (other.operatorName != null)
				return false;
		} else if (!operatorName.equals(other.operatorName))
			return false;
		if (operatorOutputPort != other.operatorOutputPort)
			return false;
		if (queryId != other.queryId)
			return false;
		if (queryName == null) {
			if (other.queryName != null)
				return false;
		} else if (!queryName.equals(other.queryName))
			return false;
		if (useName != other.useName)
			return false;
		if (useOperatorOutputPort != other.useOperatorOutputPort)
			return false;
		return true;
	}

}
