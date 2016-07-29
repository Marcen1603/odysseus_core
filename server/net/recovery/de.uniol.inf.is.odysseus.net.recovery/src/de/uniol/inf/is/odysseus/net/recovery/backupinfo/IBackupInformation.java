package de.uniol.inf.is.odysseus.net.recovery.backupinfo;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

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
	 * @return The string representation of the id. See
	 *         {@link OdysseusNodeID#fromString(String)}.
	 */
	public String getNodeId();

	/**
	 * Gets the local id of the query.
	 * 
	 * @return See {@link ILogicalQuery#getID()}.
	 */
	public int getLocalQueryId();

	/**
	 * Gets the PQL statement for the query.
	 * 
	 * @return See
	 *         {@link IPQLGenerator#generatePQLStatement(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator)}
	 */
	public String getPqlStatement();

	/**
	 * Gets the State of the query.
	 * 
	 * @return See {@link QueryState}.
	 */
	public String getQueryState();

	/**
	 * Gets the shared id of the query. <br />
	 * Only for distributed queries.
	 * 
	 * @return See {@link UUID#fromString(String)}.
	 */
	public Optional<String> getSharedQueryId();

	/**
	 * Gets the id of the node on that the query is distributed. <br />
	 * Only for distributed queries.
	 * 
	 * @return The string representation of the id. See
	 *         {@link OdysseusNodeID#fromString(String)}.
	 */
	public Optional<String> getDistributorId();

}