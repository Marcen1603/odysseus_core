package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;

/**
 * A query distribution listener can be implemented to get information about a
 * query distribution process.
 * 
 * @author Michael Brand
 *
 */
public interface IQueryDistributionListener {

	/**
	 * This method can be used to get access to the input of a query
	 * distribution process.
	 * 
	 * @param query
	 *            The query to distribute.
	 */
	public void beforeDistribution(ILogicalQuery query);

	/**
	 * This method can be used to get access to the result of the preprocessing
	 * step within a query distribution process.
	 * 
	 * @param query
	 *            The processed query.
	 */
	public void afterPreProcessing(ILogicalQuery query);

	/**
	 * This method can be used to get access to the result of the partitioning
	 * step within a query distribution process.
	 * 
	 * @param query
	 *            The processed query.
	 * @param queryParts
	 *            The resulting query parts.
	 */
	public void afterPartitioning(ILogicalQuery query, Collection<ILogicalQueryPart> queryParts);

	/**
	 * This method can be used to get access to the result of the modification
	 * step within a query distribution process.
	 * 
	 * @param query
	 *            The processed query.
	 * @param originalParts
	 *            The original query parts (before modification).
	 * @param modifiedParts
	 *            The resulting query parts (after modification).
	 */
	public void afterModification(ILogicalQuery query, Collection<ILogicalQueryPart> originalParts, Collection<ILogicalQueryPart> modifiedParts);

	/**
	 * This method can be used to get access to the result of the allocation
	 * step within a query distribution process.
	 * 
	 * @param query
	 *            The processed query.
	 * @param allocationMap
	 *            The resulting allocation map.
	 */
	public void afterAllocation(ILogicalQuery query, Map<ILogicalQueryPart, IOdysseusNode> allocationMap);

	/**
	 * This method can be used to get access to the result of the postprocessing
	 * step within a query distribution process.
	 * 
	 * @param query
	 *            The processed query.
	 * @param allocationMap
	 *            The resulting allocation map.
	 */
	public void afterPostProcessing(ILogicalQuery query, Map<ILogicalQueryPart, IOdysseusNode> allocationMap);

	/**
	 * This method can be used to get access to the result of the transmission
	 * step within a query distribution process.
	 * 
	 * @param query
	 *            The processed query.
	 * @param allocationMap
	 *            The resulting allocation map.
	 * @param sharedQueryId
	 *            The shared query id of the distributed query.
	 */
	public void afterTransmission(ILogicalQuery query, Map<ILogicalQueryPart, IOdysseusNode> allocationMap, UUID sharedQueryId);

}