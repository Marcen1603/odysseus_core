package de.uniol.inf.is.odysseus.net.recovery.backupinfo;

import java.io.Serializable;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;

/**
 * All information needed to recover a query.
 * 
 * @author Michael Brand
 * @version 1.0
 */
public interface IBackupInformation extends Serializable {	

	/**
	 * Gets the id of the node on that the query is installed.
	 * 
	 * @return See {@link OdysseusNodeID#fromString(String)}.
	 */
	public String getNodeId();

	/**
	 * Sets the id of the node on that the query is installed.
	 * 
	 * @param nodeID
	 *            See {@link OdysseusNodeID#toString()}.
	 */
	public void setNodeId(String nodeID);

	/**
	 * Gets the local id of the query.
	 */
	public int getLocalQueryId();

	/**
	 * Sets the local id of the query.
	 */
	public void setLocalQueryId(int localQueryId);

	/**
	 * Gets the PQL statement for the query.
	 */
	public String getPqlStatement();

	/**
	 * Sets the PQL statement for the query.
	 */
	public void setPqlStatement(String pqlStatement);

	/**
	 * Gets the state of the query.
	 * 
	 * @return See {@link QueryState#valueOf(String)}.
	 */
	public String getQueryState();

	/**
	 * Sets the state of the query.
	 * 
	 * @param queryState
	 *            See {@link QueryState#toString()}.
	 */
	public void setQueryState(String queryState);

	/**
	 * Gets the shared id of the query. <br />
	 * Only for distributed queries.
	 * 
	 * @return See {@link UUID#fromString(String)}.
	 */
	public String getSharedQueryId();

	/**
	 * Sets the shared id of the query. <br />
	 * Only for distributed queries.
	 * 
	 * @param sharedQueryId
	 *            See {@link UUID#toString()}.
	 */
	public void setSharedQueryId(String sharedQueryId);

	/**
	 * Gets the id of the node on that the query is distributed. <br />
	 * Only for distributed queries.
	 * 
	 * @return See {@link OdysseusNodeID#fromString(String)}.
	 */
	public String getDistributorId();

	/**
	 * Sets the id of the node on that the query is distributed. <br />
	 * Only for distributed queries.
	 * 
	 * @param distributorId
	 *            See {@link OdysseusNodeID#toString()}.
	 */
	public void setDistributorId(String distributorId);

}