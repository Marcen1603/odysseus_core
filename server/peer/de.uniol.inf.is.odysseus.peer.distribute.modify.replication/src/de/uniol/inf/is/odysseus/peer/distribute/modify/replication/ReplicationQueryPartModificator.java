package de.uniol.inf.is.odysseus.peer.distribute.modify.replication;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * A modifier of {@link ILogicalQueryPart}s, which replicates query parts and inserts operators to 
 * merge the result sets of the replicates for each relative sink within every single query part.
 * @author Michael Brand
 */
public class ReplicationQueryPartModificator implements IQueryPartModificator {
	
	// TODO handling of evaluation scenarios
	
	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(ReplicationQueryPartModificator.class);
	
	/**
	 * The name of this modificator.
	 */
	private static final String name = "replication";
	
	/**
	 * The minimum degree of replication.
	 */
	private static final int min_degree = 2;
	
	/**
	 * The class of the used merger.
	 */
	private static final Class<? extends ILogicalOperator> mergerClass = ReplicationMergeAO.class;

	@Override
	public String getName() {
		
		return ReplicationQueryPartModificator.name;
		
	}

	// TODO javaDoc
	@Override
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, 
			QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException {
		
		// Preconditions
		if(queryParts == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Query parts to be modified must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList();
		if(queryParts.isEmpty()) {
			
			ReplicationQueryPartModificator.log.warn("No query parts given to replicate");
			return modifiedParts;
			
		}
		
		// Determine degree of replication
		final int degreeOfReplication = 
				ReplicationQueryPartModificator.determineDegreeOfReplication(queryParts.size(), modificatorParameters);
		
		// Copy the query parts
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin = 
				ReplicationQueryPartModificator.copyQueryParts(queryParts, degreeOfReplication);
		
		// Modify each query part
		for(ILogicalQueryPart originPart : queryParts)
			replicatesToOrigin.putAll(ReplicationQueryPartModificator.modify(originPart, replicatesToOrigin));
		
		// Create the return value
		for(ILogicalQueryPart originPart : replicatesToOrigin.keySet())
				modifiedParts.addAll(replicatesToOrigin.get(originPart));
		
		return modifiedParts;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modify(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin) throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin query part for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatesToOrigin == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(!replicatesToOrigin.keySet().contains(originPart)) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must contain " +
					"the origin query part for modification!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = 
				ReplicationQueryPartModificator.collectRelativeSinks(originPart , modifiedReplicatesToOrigin.get(originPart));
		
		// Modify each sink
		for(ILogicalOperator originSink : copiedToOriginSinks.keySet()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.modifySink(originPart, replicatesToOrigin, originSink, 
					copiedToOriginSinks.get(originSink)));
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifySink(
			ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin,
			ILogicalOperator originSink, Collection<ILogicalOperator> replicatedSinks) throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin query part for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatesToOrigin == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(!replicatesToOrigin.keySet().contains(originPart)) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must contain " +
					"the origin query part for modification!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(originSink == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin sink for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatedSinks == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("List of replicated sinks for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Process real sinks
		if(originSink.getSubscriptions().isEmpty()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processSubscription(originPart, replicatesToOrigin, originSink, 
					replicatedSinks));
			
		}
		
		// Process replicates for each subscription
		for(LogicalSubscription subscription : originSink.getSubscriptions()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processSubscription(originPart, replicatesToOrigin, originSink, 
					replicatedSinks, subscription));
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processSubscription(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> replicatedSinks) throws QueryPartModificationException {
		
		return ReplicationQueryPartModificator.processSubscription(originPart, replicatesToOrigin, originSink, replicatedSinks, null);
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processSubscription(ILogicalQueryPart originPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, ILogicalOperator originSink, 
			Collection<ILogicalOperator> replicatedSinks, LogicalSubscription subscription) throws QueryPartModificationException {
		
		// Preconditions 1
		if(originPart == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin query part for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatesToOrigin == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(!replicatesToOrigin.keySet().contains(originPart)) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must contain " +
					"the origin query part for modification!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(originSink == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin sink for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatedSinks == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("List of replicated sinks for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Real sink
		if(subscription == null) {
			
			// The merger to be inserted
			ILogicalOperator merger = null;
			try {
				
				merger = ReplicationQueryPartModificator.mergerClass.newInstance();
				
			} catch(InstantiationException | IllegalAccessException ex) {
				
				QueryPartModificationException e = new QueryPartModificationException("Merger could not be instantiated!", ex);
				ReplicationQueryPartModificator.log.error(e.getMessage(), e);
				throw e;
				
			}
			
			for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
			
				ILogicalOperator replicatedSink = ((List<ILogicalOperator>) replicatedSinks).get(sinkNo);
				replicatedSink.subscribeSink(merger, sinkNo, 0, replicatedSink.getOutputSchema());
				
			}
			
			// Create new query part
			Collection<ILogicalOperator> operators = Lists.newArrayList(merger);
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			modifiedQueryParts.add(new LogicalQueryPart(operators));
			modifiedReplicatesToOrigin.put(new LogicalQueryPart(operators), modifiedQueryParts);
			
			if(ReplicationQueryPartModificator.log.isDebugEnabled()) {
				
				String strSinks = "(";
				for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
					
					strSinks += ((List<ILogicalOperator>) replicatedSinks).get(sinkNo).getName();
							
					if(sinkNo == replicatedSinks.size() - 1)
						strSinks += ")";
					else strSinks += ", ";
					
				}
				ReplicationQueryPartModificator.log.debug("Inserted a merger after {}.", strSinks);
				
			}
			
		}
		
		// Preconditions 2
		if(subscription == null || originPart.getOperators().contains(subscription.getTarget()))
			return modifiedReplicatesToOrigin;
		
		// The query part containing the origin target
		Optional<ILogicalQueryPart> optTargetPart = 
				LogicalQueryHelper.determineQueryPart(modifiedReplicatesToOrigin.keySet(), subscription.getTarget());
		
		// Insert merger
		if(!optTargetPart.isPresent()) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processTargetOfSubscription(originPart, replicatesToOrigin, 
					replicatedSinks, subscription));
			
		} else {
			
			// Find clones of target
			int targetNo = ((ImmutableList<ILogicalOperator>) optTargetPart.get().getOperators()).indexOf(subscription.getTarget());
			List<ILogicalOperator> replicatedTargets = Lists.newArrayList();
			for(ILogicalQueryPart replicate : replicatesToOrigin.get(optTargetPart.get()))
				replicatedTargets.add(((ImmutableList<ILogicalOperator>) replicate.getOperators()).get(targetNo));
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.processTargetOfSubscription(originPart, replicatesToOrigin, 
					replicatedSinks, replicatedTargets, optTargetPart.get(), (List<ILogicalQueryPart>) replicatesToOrigin.get(optTargetPart.get()), 
					subscription));
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processTargetOfSubscription(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, Collection<ILogicalOperator> replicatedSinks, 
			LogicalSubscription subscription) throws QueryPartModificationException {
		
		return ReplicationQueryPartModificator.processTargetOfSubscription(originPart, replicatesToOrigin, replicatedSinks,
				null, null, null, subscription);
	
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> processTargetOfSubscription(ILogicalQueryPart originPart, 
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, Collection<ILogicalOperator> replicatedSinks, 
			List<ILogicalOperator> replicatedTargets, ILogicalQueryPart originPartOfTarget, 
			List<ILogicalQueryPart> queryPartsOfReplicatedTarget, LogicalSubscription subscription) throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin query part for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatesToOrigin == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(!replicatesToOrigin.keySet().contains(originPart)) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must contain " +
					"the origin query part for modification!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatedSinks == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("List of replicated sinks for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(subscription == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Subscription to process must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// Subscribe merger for every target
		if(replicatedTargets == null) {
			
			modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.insertMerger(originPart, modifiedReplicatesToOrigin, 
					replicatedSinks, subscription));
			
		} else {
		
			for(int targetNo = 0; targetNo < replicatedTargets.size(); targetNo++) {
			
				modifiedReplicatesToOrigin.putAll(ReplicationQueryPartModificator.insertMerger(originPart, modifiedReplicatesToOrigin, 
						replicatedSinks, replicatedTargets.get(targetNo), originPartOfTarget, queryPartsOfReplicatedTarget.get(targetNo), subscription));
				
			} 
			
		}
		
		return modifiedReplicatesToOrigin;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertMerger(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin, Collection<ILogicalOperator> replicatedSinks,
			LogicalSubscription subscription) throws QueryPartModificationException {
		
		return ReplicationQueryPartModificator.insertMerger(originPart, modifiedReplicatesToOrigin, replicatedSinks, null, null, null, subscription);
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertMerger(ILogicalQueryPart originPart,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> replicatesToOrigin, Collection<ILogicalOperator> replicatedSinks,
			ILogicalOperator replicatedTarget, ILogicalQueryPart originPartOfTarget, ILogicalQueryPart queryPartOfReplicatedTarget, 
			LogicalSubscription subscription) throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin query part for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatesToOrigin == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(!replicatesToOrigin.keySet().contains(originPart)) {
			
			QueryPartModificationException e = new QueryPartModificationException("Mapping of replicates to origin query parts must contain " +
					"the origin query part for modification!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicatedSinks == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("List of replicated sinks for modification must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(subscription == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Subscription to process must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedReplicatesToOrigin = Maps.newHashMap(replicatesToOrigin);
		
		// The merger to be inserted
		ILogicalOperator merger = null;
		try {
			
			merger = ReplicationQueryPartModificator.mergerClass.newInstance();
			
		} catch(InstantiationException | IllegalAccessException ex) {
			
			QueryPartModificationException e = new QueryPartModificationException("Merger could not be instantiated!", ex);
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// Subscribe merger to sinks
		for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
			
			((List<ILogicalOperator>) replicatedSinks).get(sinkNo).subscribeSink(merger, sinkNo, subscription.getSourceOutPort(), 
					subscription.getSchema());
			
		}
		
		// Subcribe target to merger
		if(replicatedTarget == null) {
			
			merger.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), 0, subscription.getSchema());
			
			// Create new query part
			Collection<ILogicalOperator> operators = Lists.newArrayList(merger);
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			modifiedQueryParts.add(new LogicalQueryPart(operators));
			modifiedReplicatesToOrigin.put(new LogicalQueryPart(operators), modifiedQueryParts);
			
		}
		else {
			
			merger.subscribeSink(replicatedTarget, subscription.getSinkInPort(), 0, subscription.getSchema());
			
			// Create modified query part
			Collection<ILogicalOperator> operatorsWithMerger = Lists.newArrayList(queryPartOfReplicatedTarget.getOperators());
			operatorsWithMerger.add(merger);
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			for(ILogicalQueryPart part : replicatesToOrigin.get(originPartOfTarget)) {
				
				if(!part.equals(queryPartOfReplicatedTarget))
					modifiedQueryParts.add(part);
				
			}
			modifiedQueryParts.add(new LogicalQueryPart(operatorsWithMerger));
			modifiedReplicatesToOrigin.put(originPartOfTarget, modifiedQueryParts);
			
		}
		
		if(ReplicationQueryPartModificator.log.isDebugEnabled()) {
			
			String strSinks = "(";
			for(int sinkNo = 0; sinkNo < replicatedSinks.size(); sinkNo++) {
				
				strSinks += ((List<ILogicalOperator>) replicatedSinks).get(sinkNo).getName();
						
				if(sinkNo == replicatedSinks.size() - 1)
					strSinks += ")";
				else strSinks += ", ";
				
			}
			ReplicationQueryPartModificator.log.debug("Inserted a merger between {} and {}.", strSinks, replicatedTarget);
			
		}
		return modifiedReplicatesToOrigin;
		
	}

	// TODO javaDoc
	private static Map<ILogicalOperator, Collection<ILogicalOperator>> collectRelativeSinks(
			ILogicalQueryPart originPart, Collection<ILogicalQueryPart> replicates) throws QueryPartModificationException {
		
		// Preconditions
		if(originPart == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Origin query part to collect relative sinks must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(replicates == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("List of replicates to collect relative sinks must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = Maps.newHashMap();
		
		// Collect origin sinks
		List<ILogicalOperator> originSinks = (List<ILogicalOperator>) LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(originPart);
		for(int sinkNo = 0; sinkNo < originSinks.size(); sinkNo++) {
			
			Collection<ILogicalOperator> copiedSinks = Lists.newArrayList();
			
			for(ILogicalQueryPart replicate : replicates)
				copiedSinks.add(((List<ILogicalOperator>) LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(replicate)).get(sinkNo));
			
			copiedToOriginSinks.put(originSinks.get(sinkNo), copiedSinks);
			ReplicationQueryPartModificator.log.debug("Found {} as a relative sink of {}.", originSinks.get(sinkNo).getName(), originPart);
			
		}
		
		return copiedToOriginSinks;
		
	}

	// TODO javaDoc
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyQueryParts(
			Collection<ILogicalQueryPart> queryParts, int degreeOfReplication) throws QueryPartModificationException {
		
		// Preconditions
		if(queryParts == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Query parts to be copied must be not null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(degreeOfReplication < 1) {
			
			QueryPartModificationException e = new QueryPartModificationException("Degree of replication must be greater than 0!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOriginPart = Maps.newHashMap();
		
		for(ILogicalQueryPart part : queryParts) {
			
			Collection<ILogicalQueryPart> copies = Lists.newArrayList();
			
			for(int copyNo = 0; copyNo < degreeOfReplication; copyNo++)
				copies.add(LogicalQueryHelper.copyQueryPartDeep(part).getE2());
			
			copiesToOriginPart.put(part, copies);
				
		}
	
//		Collection<Map<ILogicalQueryPart, ILogicalQueryPart>> plainCopies = Lists.newArrayList();
//		for(ILogicalQueryPart part : queryParts) {
//		
//			IPair<ILogicalQueryPart, ILogicalQueryPart> originAndCopy = null;
//			for(int copyNo = 0; copyNo < degreeOfReplication; copyNo++)
//				originAndCopy = LogicalQueryHelper.copyQueryPartDeep(part);
//			Map<ILogicalQueryPart, ILogicalQueryPart> copies = Maps.newHashMap();
//			copies.put(originAndCopy.getE2(), originAndCopy.getE1());
//			plainCopies.add(copies);
//			
//		}
//		
//		for(Map<ILogicalQueryPart, ILogicalQueryPart> plainCopyMap : plainCopies) {
//			
//			for(ILogicalQueryPart copy : plainCopyMap.keySet() ) {
//				
//				Collection<ILogicalQueryPart> copyList = null;
//				
//				if(copiesToOriginPart.containsKey(plainCopyMap.get(copy)))
//					copyList = copiesToOriginPart.get(plainCopyMap.get(copy));
//				else {
//					
//					copyList = Lists.newArrayList();
//					copiesToOriginPart.put(plainCopyMap.get(copy), copyList);
//					
//				}
//				
//				copyList.add(copy);
//				ReplicationQueryPartModificator.log.debug("Created query part {} as a copy of query part {}.", copy, plainCopyMap.get(copy));
//				
//			}
//			
//		}

		return copiesToOriginPart;
		
	}

	/**
	 * Determines the degree of replications given by the user (Odysseus script). <br />
	 * #PEER_MODIFICATION replication <degree>
	 * @param numQueryParts The number of query parts, which shall be replicated.
	 * @param modificatorParameters The parameters for the modification given by the user without the parameter "replication".
	 * @return The degree of parallelism given by the user.
	 * @throws QueryPartModificationException if <code>numQueryyParts</code> is less than 1, <code>modificatorParameters</code> is null or 
	 * does not contain at least the desired degree of replication, which must be an integer greater or equal to 
	 * {@value #min_degree}.
	 */
	private static int determineDegreeOfReplication(int numQueryParts, List<String> modificatorParameters) throws QueryPartModificationException {
		
		// Preconditions 1
		if(numQueryParts < 1) {
			
			QueryPartModificationException e = new QueryPartModificationException("At least one query part is needed for replication!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(modificatorParameters == null) {
			
			QueryPartModificationException e = new QueryPartModificationException("Parameters for query part replicator must not be null!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		} else if(modificatorParameters.isEmpty()) {
			
			QueryPartModificationException e = new QueryPartModificationException("Parameters for query part replicator must at least contain one " +
					"arguments: degree of replication!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// The return value
		int degreeOfReplication = -1;
		
		// Preconditions 2
		try {
			
			degreeOfReplication = Integer.parseInt(modificatorParameters.get(0));
			if(degreeOfReplication < ReplicationQueryPartModificator.min_degree) {
				
				QueryPartModificationException e = new QueryPartModificationException("First parameter for query part replicator, " +
						"the degree of replication, must be at least" + ReplicationQueryPartModificator.min_degree + "!");
				ReplicationQueryPartModificator.log.error(e.getMessage(), e);
				throw e;
				
			}
			
		} catch(NumberFormatException nfe) {
			
			QueryPartModificationException e = new QueryPartModificationException("First parameter for query part replicator must be an integer!", nfe);
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}		
		
		// The bound IP2PDictionary
		Optional<IP2PDictionary> optDict = Activator.getP2PDictionary();
		
		// Preconditions 3
		if(!optDict.isPresent()) {
			
			QueryPartModificationException e = new QueryPartModificationException("No P2PDictionary available!");
			ReplicationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;
			
		}
		
		// Check number of available peers (inclusive the local one)
		int numRemotePeers = optDict.get().getRemotePeerIDs().size();
		if(numRemotePeers + 1 < degreeOfReplication * numQueryParts) {
			
			ReplicationQueryPartModificator.log.warn("Replication leads to at least {} query parts, " +
					"but there are only {} peers available. Consider to provide more peers. " +
					"For the given configuration some query parts will be executed on the same peer.", 
					degreeOfReplication * numQueryParts, numRemotePeers + 1);
			
		}
		
		ReplicationQueryPartModificator.log.debug("Degree of replication set to {}.", degreeOfReplication);
		return degreeOfReplication;
		
	}

}