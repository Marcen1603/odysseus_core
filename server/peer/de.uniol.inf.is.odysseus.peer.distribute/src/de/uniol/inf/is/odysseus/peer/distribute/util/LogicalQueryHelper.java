package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;

public final class LogicalQueryHelper {

	private static final String PQL_PARSER_ID = "PQL";

	private static int connectionCounter = 0;
	
	private static IPQLGenerator pqlGenerator;
	private static IServerExecutor serverExecutor;

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = (IServerExecutor)serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}

	public static ILogicalQuery copyLogicalQuery(ILogicalQuery originQuery) {
		Preconditions.checkNotNull(originQuery, "Logical query to copy must not be null!");

		ILogicalQuery copy = new LogicalQuery(PQL_PARSER_ID, copyLogicalPlan(originQuery.getLogicalPlan()), originQuery.getPriority());

		copy.setName(originQuery.getName());
		copy.setQueryText(pqlGenerator.generatePQLStatement(copy.getLogicalPlan()));
		copy.setUser(originQuery.getUser());

		return copy;
	}

	public static ILogicalOperator copyLogicalPlan(ILogicalOperator originPlan) {
		Preconditions.checkNotNull(originPlan, "Logical plan to copy must not be null!");

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(originPlan.getOwner());

		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<>();

		walker.prefixWalk(originPlan, copyVisitor);
		return copyVisitor.getResult();
	}

	public static Collection<ILogicalOperator> getAllOperators(ILogicalQuery plan) {
		return getAllOperators(plan.getLogicalPlan());
	}

	public static Collection<ILogicalOperator> getAllOperators(ILogicalOperator operator) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperatorsImpl(operator, operators);
		return operators;
	}

	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {
			list.add(currentOperator);
			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}
		}
	}

	public static Collection<ILogicalOperator> replaceStreamAOs(Collection<ILogicalOperator> operators) {
		Preconditions.checkNotNull(operators);

		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		final List<ILogicalOperator> operatorsToAdd = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {

			if (operator instanceof StreamAO) {
				ILogicalOperator streamPlan = serverExecutor.getDataDictionary(PeerDistributePlugIn.getActiveSession().getTenant()).getStreamForTransformation(((StreamAO) operator).getStreamname(), PeerDistributePlugIn.getActiveSession());

				ILogicalOperator streamPlanCopy = copyLogicalPlan(streamPlan);

				setDestinationNames(operator, streamPlanCopy);			
				replaceWithSubplan(operator, streamPlanCopy);
					
				operatorsToRemove.add(operator);
				operatorsToAdd.add(streamPlanCopy);
			}
		}

		operators.removeAll(operatorsToRemove);
		operators.addAll(operatorsToAdd);

		return operators;
	}

	private static void setDestinationNames(ILogicalOperator fromOperator, ILogicalOperator toOperator) {
		Collection<ILogicalOperator> streamPlanOperators = getAllOperators(toOperator);
		for( ILogicalOperator streamPlanOperator : streamPlanOperators ) {
			streamPlanOperator.setDestinationName(fromOperator.getDestinationName());
		}
	}

	public static void replaceWithSubplan(ILogicalOperator leafOp, ILogicalOperator newOp) {
		if (leafOp.getSubscribedToSource().size() > 0) {
			throw new IllegalArgumentException("Method can only be called for a leaf");
		}

		for (LogicalSubscription subToSink : leafOp.getSubscriptions()) {
			ILogicalOperator target = subToSink.getTarget();

			target.unsubscribeFromSource(leafOp, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
			target.subscribeToSource(newOp, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());
		}
	}

	public static void removeTopAOs(Collection<ILogicalOperator> operators) {
		List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {
			if (operator instanceof TopAO) {
				operator.unsubscribeFromAllSources();
				operatorsToRemove.add(operator);
			}
		}

		for (ILogicalOperator operatorToRemove : operatorsToRemove) {
			operators.remove(operatorToRemove);
		}
	}

	public static Collection<ILogicalOperator> getSinks(Collection<ILogicalOperator> allOperators) {
		Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for (ILogicalOperator operator : allOperators) {
			if (operator.getSubscriptions().isEmpty()) {
				sinks.add(operator);
			}
		}
		return sinks;
	}
	
	/**
	 * Collects all operators within a query part, which are subscribed to sources outside the query part.
	 * @param part The query part to process.
	 * @return A collection of all relative sources within <code>part</code>.
	 */
	public static Collection<ILogicalOperator> getRelativeSourcesOfLogicalQueryPart(
			final ILogicalQueryPart part) {
		
		Preconditions.checkNotNull(part, "Logical query part to process must not be null!");
		
		// The return value
		final Collection<ILogicalOperator> relativeSources = Lists.newArrayList();
		
		for(ILogicalOperator operator : part.getOperators()) {
			
			if(operator.getSubscribedToSource().isEmpty()) {
				
				relativeSources.add(operator);
				continue;
				
			}
			
			for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
				
				if(!part.getOperators().contains(subToSource.getTarget())) {
					
					relativeSources.add(operator);
					break;
					
				}
				
			}
			
		}
		
		return relativeSources;
		
	}
	
	/**
	 * Collects all operators within a query part, which are subscribed to sinks outside the query part.
	 * @param part The query part to process.
	 * @return A collection of all relative sinks within <code>part</code>.
	 */
	public static Collection<ILogicalOperator> getRelativeSinksOfLogicalQueryPart(
			final ILogicalQueryPart part) {
		
		Preconditions.checkNotNull(part, "Logical query part to process must not be null!");
		
		// The return value
		final Collection<ILogicalOperator> relativeSinks = Lists.newArrayList();
		
		for(ILogicalOperator operator : part.getOperators()) {
			
			
			if(operator.getSubscriptions().isEmpty()) {
				
				relativeSinks.add(operator);
				continue;
				
			}
			
			for(LogicalSubscription subToSink : operator.getSubscriptions()) {
				
				if(!part.getOperators().contains(subToSink.getTarget())) {
					
					relativeSinks.add(operator);
					break;
					
				}
				
			}
			
		}
		
		return relativeSinks;
		
	}
	
	/**
	 * Collects all relative (and real) sources of a query part and it's copies. <br />
	 * The origin query part will not be modified.
	 * @param originPart The query part whose sources shall be collected.
	 * @param copies A collection of all copies of <code>originPart</code>.
	 * @return A mapping of copied relative (and real) sources to the origin ones.
	 * @throws NullPointerException if <code>originPart</code> or <code>copies</code> is null.
	 * @throws IllegalArgumentException if at least one entry of <code>copies</code> does not contain all sources of <code>originPart</code>.
	 */
	public static Map<ILogicalOperator, Collection<ILogicalOperator>> collectRelativeSources(
			ILogicalQueryPart originPart, Collection<ILogicalQueryPart> copies) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part to collect relative sources must be not null!");
		else if(copies == null)
			throw new NullPointerException("List of copies to collect relative sources must be not null!");
		
		// The return value
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSources = Maps.newHashMap();
		
		// Collect origin sinks
		List<ILogicalOperator> originSources = (List<ILogicalOperator>) LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(originPart);
		for(ILogicalOperator source : originSources) {
			
			Collection<ILogicalOperator> copiedSources = Lists.newArrayList();
			int sourceNo = -1;
			
			try {
			
				sourceNo = ((List<ILogicalOperator>) ((Collection<ILogicalOperator>) originPart.getOperators())).indexOf(source);
				
				for(ILogicalQueryPart copy : copies)
					copiedSources.add(((List<ILogicalOperator>) ((Collection<ILogicalOperator>) copy.getOperators())).get(sourceNo));
				
			} catch(IndexOutOfBoundsException e) {
				
				throw new IllegalArgumentException("At least one copy does not contain all sources of the origin!");
				
			}
			
			copiedToOriginSources.put(source, copiedSources);
			
		}
		
		return copiedToOriginSources;
		
	}
	
	/**
	 * Collects all relative (and real) sinks of a query part and it's copies. <br />
	 * The origin query part will not be modified.
	 * @param originPart The query part whose sinks shall be collected.
	 * @param copies A collection of all copies of <code>originPart</code>.
	 * @return A mapping of copied relative (and real) sinks to the origin ones.
	 * @throws NullPointerException if <code>originPart</code> or <code>copies</code> is null.
	 * @throws IllegalArgumentException if at least one entry of <code>copies</code> does not contain all sinks of <code>originPart</code>.
	 */
	public static Map<ILogicalOperator, Collection<ILogicalOperator>> collectRelativeSinks(
			ILogicalQueryPart originPart, Collection<ILogicalQueryPart> copies) throws NullPointerException, IllegalArgumentException {
		
		// Preconditions
		if(originPart == null)
			throw new NullPointerException("Origin query part to collect relative sinks must be not null!");
		else if(copies == null)
			throw new NullPointerException("List of copies to collect relative sinks must be not null!");
		
		// The return value
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = Maps.newHashMap();
		
		// Collect origin sinks
		List<ILogicalOperator> originSinks = (List<ILogicalOperator>) LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(originPart);
		for(ILogicalOperator sink : originSinks) {
			
			Collection<ILogicalOperator> copiedSinks = Lists.newArrayList();
			int sinkNo = -1;
			
			try {
			
				sinkNo = ((List<ILogicalOperator>) ((Collection<ILogicalOperator>) originPart.getOperators())).indexOf(sink);
				
				for(ILogicalQueryPart copy : copies)
					copiedSinks.add(((List<ILogicalOperator>) ((Collection<ILogicalOperator>) copy.getOperators())).get(sinkNo));
				
			} catch(IndexOutOfBoundsException e) {
				
				throw new IllegalArgumentException("At least one copy does not contain all sinks of the origin!");
				
			}
			
			copiedToOriginSinks.put(sink, copiedSinks);
			
		}
		
		return copiedToOriginSinks;
		
	}
	
	public static void disconnectQueryParts( Collection<ILogicalQueryPart> queryParts, IOperatorGenerator generator ) {
		Preconditions.checkNotNull(generator, "Operator generator must not be null!");
		
		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(queryParts);
		Map<LogicalSubscription, ILogicalOperator> subsToReplace = determineSubscriptionsAcrossQueryParts(queryPartAssignment);

		for (LogicalSubscription subToReplace : subsToReplace.keySet()) {
			ILogicalOperator sourceOperator = subsToReplace.get(subToReplace);
			ILogicalOperator sinkOperator = subToReplace.getTarget();
			
			generator.beginDisconnect(sourceOperator, sinkOperator);

			ILogicalOperator source = generator.createSourceofSink(sinkOperator);
			source.setOutputSchema(sourceOperator.getOutputSchema().clone());
			source.setName("RCV_" + connectionCounter);
			
			ILogicalOperator sink = generator.createSinkOfSource(sourceOperator);
			sink.setOutputSchema(sourceOperator.getOutputSchema().clone());
			sink.setName("SND_" + connectionCounter);

			sourceOperator.unsubscribeSink(subToReplace);

			sourceOperator.subscribeSink(sink, 0, subToReplace.getSourceOutPort(), sourceOperator.getOutputSchema());
			sinkOperator.subscribeToSource(source, subToReplace.getSinkInPort(), 0, source.getOutputSchema());
			
			connectionCounter++;
		}

	}

	private static Map<ILogicalOperator, ILogicalQueryPart> determineOperatorAssignment(Collection<ILogicalQueryPart> queryParts) {
		Map<ILogicalOperator, ILogicalQueryPart> map = Maps.newHashMap();
		for (ILogicalQueryPart part : queryParts) {
			for (ILogicalOperator operator : part.getOperators()) {
				map.put(operator, part);
			}
		}
		return map;
	}

	private static Map<LogicalSubscription, ILogicalOperator> determineSubscriptionsAcrossQueryParts(Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment) {
		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(queryPartAssignment.keySet());

		Map<LogicalSubscription, ILogicalOperator> subsToReplace = Maps.newHashMap();
		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator currentOperator = operatorsToVisit.remove(0);

			Collection<LogicalSubscription> sinkSubs = currentOperator.getSubscriptions();
			for (LogicalSubscription sinkSub : sinkSubs) {
				ILogicalOperator targetOperator = sinkSub.getTarget();

				ILogicalQueryPart currentQueryPart = queryPartAssignment.get(currentOperator);
				ILogicalQueryPart targetQueryPart = queryPartAssignment.get(targetOperator);
				if (!currentQueryPart.equals(targetQueryPart)) {
					subsToReplace.put(sinkSub, currentOperator);
				}
			}
		}
		return subsToReplace;
	}
	
	/**
	 * Removes all subscriptions whose target is not within the given query part.
	 * @param queryPart The query part to cut.
	 */
	public static void cutQueryPart(ILogicalQueryPart queryPart) {
		
		Preconditions.checkNotNull(queryPart, "Query part to be cut must be not null!");
		
		Collection<ILogicalOperator> operators = queryPart.getOperators();
		for(ILogicalOperator operator : operators) {
			
			Collection<LogicalSubscription> subsToRemove = Lists.newArrayList();
			for(LogicalSubscription subcription : operator.getSubscriptions()) {
				
				if(!operators.contains(subcription.getTarget()))
					subsToRemove.add(subcription);	
				
			}
			
			for(LogicalSubscription sub : subsToRemove)
				operator.unsubscribeSink(sub);
			
			subsToRemove.clear();
			for(LogicalSubscription subcription : operator.getSubscribedToSource()) {
				
				if(!operators.contains(subcription.getTarget()))
					subsToRemove.add(subcription);	
				
			}
			
			for(LogicalSubscription sub : subsToRemove)
				operator.unsubscribeSink(sub);
			
			
		}
		
	}

	public static Map<ILogicalQueryPart, ILogicalQueryPart> copyQueryPartsDeep(Collection<ILogicalQueryPart> queryParts) {
		Collection<ILogicalOperator> operators = Lists.newArrayList();
		for (ILogicalQueryPart part : queryParts) {
			operators.addAll(part.getOperators());
		}

		// copy --> original
		Map<ILogicalOperator, ILogicalOperator> operatorCopyMap = createOperatorCopyMap(operators);
		createSubscriptionsInCopies(operatorCopyMap);

		Map<ILogicalQueryPart, ILogicalQueryPart> map = Maps.newHashMap();

		for (ILogicalQueryPart queryPart : queryParts) {
			Collection<ILogicalOperator> copyOperatorsOfCopyPart = Lists.newArrayList();

			for (ILogicalOperator queryPartOperator : queryPart.getOperators()) {
				copyOperatorsOfCopyPart.add(getCopyOfMap(queryPartOperator, operatorCopyMap));
			}

			ILogicalQueryPart copyQueryPart = new LogicalQueryPart(copyOperatorsOfCopyPart);
			map.put(copyQueryPart, queryPart);
		}

		return map;
	}
	
	/**
	 * Makes as many copies of query parts as given by the number of copies. All copied query parts will be cut, so there will be no 
	 * subscription from or to outwards a query part. <br />
	 * The origin query parts will not be modified.
	 * @param queryParts A collection of query parts to copy.
	 * @param numCopies The number of copies to make.
	 * @throws NullPointerException if <code>queryParts</code> is null.
	 */
	public static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyAndCutQueryParts(
			Collection<ILogicalQueryPart> queryParts, int numCopies) throws NullPointerException {
		
		// Preconditions
		if(queryParts == null)
			throw new NullPointerException("Query parts to be copied must be not null!");
		
		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOriginPart = Maps.newHashMap();
	
		Collection<Map<ILogicalQueryPart, ILogicalQueryPart>> plainCopies = Lists.newArrayList();
		for(int copyNo = 0; copyNo < numCopies; copyNo++)
			plainCopies.add(LogicalQueryHelper.copyQueryPartsDeep(queryParts));
		
		for(Map<ILogicalQueryPart, ILogicalQueryPart> plainCopyMap : plainCopies) {
			
			for(ILogicalQueryPart copy : plainCopyMap.keySet() ) {
				
				Collection<ILogicalQueryPart> copyList = null;
				
				if(copiesToOriginPart.containsKey(plainCopyMap.get(copy)))
					copyList = copiesToOriginPart.get(plainCopyMap.get(copy));
				else {
					
					copyList = Lists.newArrayList();
					copiesToOriginPart.put(plainCopyMap.get(copy), copyList);
					
				}
			
				LogicalQueryHelper.cutQueryPart(copy);
				copyList.add(copy);
				
			}
			
		}

		return copiesToOriginPart;
		
	}

	private static Map<ILogicalOperator, ILogicalOperator> createOperatorCopyMap(Collection<ILogicalOperator> operators) {
		Map<ILogicalOperator, ILogicalOperator> copyMap = Maps.newHashMap();
		for (ILogicalOperator operator : operators) {
			copyMap.put(operator.clone(), operator);
		}
		return copyMap;
	}

	private static void createSubscriptionsInCopies(Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		for (ILogicalOperator copyOperator : operatorCopyMap.keySet()) {
			ILogicalOperator originalOperator = operatorCopyMap.get(copyOperator);

			for (LogicalSubscription sub : originalOperator.getSubscriptions()) {
				ILogicalOperator originalTarget = sub.getTarget();
				ILogicalOperator copyTarget = getCopyOfMap(originalTarget, operatorCopyMap);

				if( copyTarget != null ) {
					copyOperator.subscribeSink(copyTarget, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
				} else {
					copyOperator.subscribeSink(originalTarget, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
				}
			}
			
			for( LogicalSubscription sub : originalOperator.getSubscribedToSource() ) {
				ILogicalOperator orginalSource = sub.getTarget();
				ILogicalOperator copySource = getCopyOfMap(orginalSource, operatorCopyMap);
				
				if( copySource == null ) {
					copyOperator.subscribeToSource(orginalSource, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
				}
			}
		}
	}
	
	public static ILogicalOperator getCopyOfMap(ILogicalOperator originalOperator, Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		for (ILogicalOperator copy : operatorCopyMap.keySet()) {
			if (operatorCopyMap.get(copy).equals(originalOperator)) {
				return copy;
			}
		}

		throw new RuntimeException("Could not find the copy of " + originalOperator);
	}
	
	public static ILogicalOperator appendTopAO( ILogicalQueryPart queryPart ) {
		Preconditions.checkNotNull(queryPart, "Query part to append TopAO must not be null!");
		
		Collection<ILogicalOperator> sinks = getSinks(getAllOperators(queryPart.getOperators().iterator().next()));
		
		TopAO topAO = new TopAO();
		int inputPort = 0;
		for (ILogicalOperator sink : sinks) {
			topAO.subscribeToSource(sink, inputPort++, 0, sink.getOutputSchema());
		}

		return topAO;
	}

	public static Optional<ILogicalQueryPart> determineQueryPart(
			Collection<ILogicalQueryPart> queryParts, ILogicalOperator operator) {
		
		Preconditions.checkNotNull(queryParts, "Query parts for determination must be not null!");
		Preconditions.checkNotNull(operator, "Operator to determine query part must be not null!");
		
		for(ILogicalQueryPart part : queryParts) {
			
			if(part.getOperators().contains(operator))
				return Optional.of(part);
			
		}
		
		return Optional.absent();
		
	}
}
