package de.uniol.inf.is.odysseus.net.recovery.backupinfo.internal;

import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import de.uniol.inf.is.odysseus.net.recovery.backupinfo.IBackupInformation;

/**
 * All information needed to recover a query.
 * 
 * @author Michael Brand
 * @version 1.0
 */
public final class BackupInformation implements IBackupInformation {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 93452798488449895L;

	// Fields defined by getters of IBackupInformation
	private String nodeId;
	private int localQueryId;
	private String pqlStatement;
	private String queryState;
	private String sharedQueryId;
	private String distributorId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((distributorId == null) ? 0 : distributorId.hashCode());
		result = prime * result + localQueryId;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((pqlStatement == null) ? 0 : pqlStatement.hashCode());
		result = prime * result + ((queryState == null) ? 0 : queryState.hashCode());
		result = prime * result + ((sharedQueryId == null) ? 0 : sharedQueryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BackupInformation other = (BackupInformation) obj;
		if (distributorId == null) {
			if (other.distributorId != null)
				return false;
		} else if (!distributorId.equals(other.distributorId))
			return false;
		if (localQueryId != other.localQueryId)
			return false;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (pqlStatement == null) {
			if (other.pqlStatement != null)
				return false;
		} else if (!pqlStatement.equals(other.pqlStatement))
			return false;
		if (queryState == null) {
			if (other.queryState != null)
				return false;
		} else if (!queryState.equals(other.queryState))
			return false;
		if (sharedQueryId == null) {
			if (other.sharedQueryId != null)
				return false;
		} else if (!sharedQueryId.equals(other.sharedQueryId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BackupInformation [nodeId=" + nodeId + ", localQueryId=" + localQueryId + ", pqlStatement="
				+ pqlStatement + ", queryState=" + queryState + ", sharedQueryId=" + sharedQueryId + ", distributorId="
				+ distributorId + "]";
	}

	/**
	 * Gets a copy of the backup information.
	 */
	public BackupInformation clone() {
		final BackupInformation clone = new BackupInformation();
		clone.nodeId = this.nodeId;
		clone.localQueryId = this.localQueryId;
		clone.pqlStatement = this.pqlStatement;
		clone.queryState = this.queryState;
		clone.sharedQueryId = this.sharedQueryId;
		clone.distributorId = this.distributorId;
		return clone;
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(final String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public int getLocalQueryId() {
		return localQueryId;
	}

	public void setLocalQueryId(final int localQueryId) {
		this.localQueryId = localQueryId;
	}

	@Override
	public String getPqlStatement() {
		return pqlStatement;
	}

	public void setPqlStatement(final String pqlStatement) {
		this.pqlStatement = pqlStatement;
	}

	@Override
	public String getQueryState() {
		return queryState;
	}

	public void setQueryState(final String queryState) {
		this.queryState = queryState;
	}

	@Override
	public Optional<String> getSharedQueryId() {
		return Optional.ofNullable(sharedQueryId);
	}

	public void setSharedQueryId(final String sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

	@Override
	public Optional<String> getDistributorId() {
		return Optional.ofNullable(distributorId);
	}

	public void setDistributorId(final String distributorId) {
		this.distributorId = distributorId;
	}

	/**
	 * Reads member field from JSON object.
	 * 
	 * @throws JSONException
	 *             if any error occurs.
	 */
	public void fromJSON(JSONObject json) throws JSONException {
		nodeId = json.getString("nodeId");
		localQueryId = json.getInt("localQueryId");
		pqlStatement = json.getString("pqlStatement");
		queryState = json.getString("queryState");
		String sharedQueryIdStr = json.getString("sharedQueryId");
		if(!sharedQueryIdStr.equals(Optional.empty().toString())) {
			sharedQueryId = sharedQueryIdStr.split("(")[1].split(")")[0];
		}
		String distributorIdStr = json.getString("distributorId");
		if(!distributorIdStr.equals(Optional.empty().toString())) {
			distributorId = distributorIdStr.split("(")[1].split(")")[0];
		}
	}

}