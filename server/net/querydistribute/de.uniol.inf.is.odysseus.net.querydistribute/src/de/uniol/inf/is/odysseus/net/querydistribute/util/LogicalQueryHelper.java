package de.uniol.inf.is.odysseus.net.querydistribute.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartTransmissionException;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public final class LogicalQueryHelper {

	private static final Logger LOG = LoggerFactory.getLogger(LogicalQueryHelper.class);
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
		serverExecutor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}

	public static ILogicalQuery copyLogicalQuery(ILogicalQuery originQuery) {
		Preconditions.checkNotNull(originQuery, "Logical query to copy must not be null!");

		ILogicalQuery copy = new LogicalQuery(PQL_PARSER_ID, originQuery.getLogicalPlan().copyPlan(), originQuery.getPriority());

		copy.setName(originQuery.getName());
		copy.setQueryText(pqlGenerator.generatePQLStatement(copy.getLogicalPlan()));
		copy.setUser(originQuery.getUser());

		return copy;
	}

	public static String generatePQLStatementFromQueryPart(ILogicalQueryPart part) {

		List<ILogicalOperator> operators = Lists.newArrayList(part.getOperators());

		String statement = "";
		while (!operators.isEmpty()) {
			ILogicalOperator operator = operators.remove(0);

			List<ILogicalOperator> visitedOperators = Lists.newArrayList();
			collectOperatorsWithSubscriptions(operator, visitedOperators);

			statement = statement + pqlGenerator.generatePQLStatement(operator) + System.lineSeparator();

			operators.removeAll(visitedOperators);
		}

		return statement;
	}

	private static void collectOperatorsWithSubscriptions(ILogicalOperator operator, List<ILogicalOperator> visitedOperators) {
		if (!visitedOperators.contains(operator)) {
			visitedOperators.add(operator);

			for (LogicalSubscription sub : operator.getSubscribedToSource()) {
				collectOperatorsWithSubscriptions(sub.getSource(), visitedOperators);
			}

			for (LogicalSubscription sub : operator.getSubscriptions()) {
				collectOperatorsWithSubscriptions(sub.getSink(), visitedOperators);
			}
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

	public static Collection<ILogicalOperator> getRelativeSourcesOfLogicalQueryPart(final ILogicalQueryPart part) {

		Preconditions.checkNotNull(part, "Logical query part to process must not be null!");

		// The return value
		final Collection<ILogicalOperator> relativeSources = Lists.newArrayList();

		for (ILogicalOperator operator : part.getOperators()) {

			if (operator.getSubscribedToSource().isEmpty()) {

				relativeSources.add(operator);
				continue;

			}

			for (LogicalSubscription subToSource : operator.getSubscribedToSource()) {

				if (!part.getOperators().contains(subToSource.getSource())) {

					relativeSources.add(operator);
					break;

				}

			}

		}

		return relativeSources;

	}

	/**
	 * Collects all operators within a query part, which are subscribed to sinks
	 * outside the query part.
	 * 
	 * @param part
	 *            The query part to process.
	 * @return A collection of all relative sinks within <code>part</code>.
	 */
	public static Collection<ILogicalOperator> getRelativeSinksOfLogicalQueryPart(final ILogicalQueryPart part) {

		Preconditions.checkNotNull(part, "Logical query part to process must not be null!");

		// The return value
		final Collection<ILogicalOperator> relativeSinks = Lists.newArrayList();

		for (ILogicalOperator operator : part.getOperators()) {

			if (operator.getSubscriptions().isEmpty()) {

				relativeSinks.add(operator);
				continue;

			}

			for (LogicalSubscription subToSink : operator.getSubscriptions()) {

				if (!part.getOperators().contains(subToSink.getSink())) {

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
	 * 
	 * @param originPart
	 *            The query part whose sources shall be collected.
	 * @param copies
	 *            A collection of all copies of <code>originPart</code>.
	 * @return A mapping of copied relative (and real) sources to the origin
	 *         ones.
	 * @throws NullPointerException
	 *             if <code>originPart</code> or <code>copies</code> is null.
	 * @throws IllegalArgumentException
	 *             if at least one entry of <code>copies</code> does not contain
	 *             all sources of <code>originPart</code>.
	 */
	@SuppressWarnings("unchecked")
	public static Map<ILogicalOperator, Collection<ILogicalOperator>> collectRelativeSources(ILogicalQueryPart originPart, Collection<ILogicalQueryPart> copies) throws NullPointerException, IllegalArgumentException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part to collect relative sources must be not null!");
		else if (copies == null)
			throw new NullPointerException("List of copies to collect relative sources must be not null!");

		// The return value
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSources = Maps.newHashMap();

		// Collect origin sinks
		List<ILogicalOperator> originSources = (List<ILogicalOperator>) LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(originPart);
		for (ILogicalOperator source : originSources) {

			Collection<ILogicalOperator> copiedSources = Lists.newArrayList();
			int sourceNo = -1;

			try {

				sourceNo = ((List<ILogicalOperator>) (originPart.getOperators())).indexOf(source);

				for (ILogicalQueryPart copy : copies)
					copiedSources.add(((List<ILogicalOperator>) (copy.getOperators())).get(sourceNo));

			} catch (IndexOutOfBoundsException e) {

				throw new IllegalArgumentException("At least one copy does not contain all sources of the origin!");

			}

			copiedToOriginSources.put(source, copiedSources);

		}

		return copiedToOriginSources;

	}

	/**
	 * Collects all relative (and real) sinks of a query part and it's copies. <br />
	 * The origin query part will not be modified.
	 * 
	 * @param originPart
	 *            The query part whose sinks shall be collected.
	 * @param copies
	 *            A collection of all copies of <code>originPart</code>.
	 * @return A mapping of copied relative (and real) sinks to the origin ones.
	 * @throws NullPointerException
	 *             if <code>originPart</code> or <code>copies</code> is null.
	 * @throws IllegalArgumentException
	 *             if at least one entry of <code>copies</code> does not contain
	 *             all sinks of <code>originPart</code>.
	 */
	@SuppressWarnings("unchecked")
	public static Map<ILogicalOperator, Collection<ILogicalOperator>> collectRelativeSinks(ILogicalQueryPart originPart, Collection<ILogicalQueryPart> copies) throws NullPointerException, IllegalArgumentException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part to collect relative sinks must be not null!");
		else if (copies == null)
			throw new NullPointerException("List of copies to collect relative sinks must be not null!");

		// The return value
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = Maps.newHashMap();

		// Collect origin sinks
		List<ILogicalOperator> originSinks = (List<ILogicalOperator>) LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(originPart);
		for (ILogicalOperator sink : originSinks) {

			Collection<ILogicalOperator> copiedSinks = Lists.newArrayList();
			int sinkNo = -1;

			try {

				sinkNo = ((List<ILogicalOperator>) (originPart.getOperators())).indexOf(sink);

				for (ILogicalQueryPart copy : copies)
					copiedSinks.add(((List<ILogicalOperator>) (copy.getOperators())).get(sinkNo));

			} catch (IndexOutOfBoundsException e) {

				throw new IllegalArgumentException("At least one copy does not contain all sinks of the origin!");

			}

			copiedToOriginSinks.put(sink, copiedSinks);

		}

		return copiedToOriginSinks;

	}

	public static void disconnectQueryParts(Map<ILogicalQueryPart, IOdysseusNode> allocationMap, IOperatorGenerator generator) throws QueryPartTransmissionException {
		Preconditions.checkNotNull(generator, "Operator generator must not be null!");

		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(allocationMap.keySet());
		Map<LogicalSubscription, ILogicalOperator> subsToReplace = determineSubscriptionsAcrossQueryParts(queryPartAssignment);

		for (LogicalSubscription subToReplace : subsToReplace.keySet()) {
			ILogicalOperator sourceOperator = subsToReplace.get(subToReplace);
			ILogicalOperator sinkOperator = subToReplace.getSink();

			ILogicalQueryPart sinkQueryPart = queryPartAssignment.get(sinkOperator);
			ILogicalQueryPart sourceQueryPart = queryPartAssignment.get(sourceOperator);
			IOdysseusNode sinkNode = allocationMap.get(sinkQueryPart);
			IOdysseusNode sourceNode = allocationMap.get(sourceQueryPart);
			generator.beginDisconnect(sourceQueryPart, sourceOperator, sourceNode, sinkQueryPart, sinkOperator, sinkNode);

			ILogicalOperator source = generator.createSourceofSink(sinkQueryPart, sinkOperator, sinkNode);
			source.setOutputSchema(sourceOperator.getOutputSchema().clone());
			source.setName("RCV_" + connectionCounter);

			ILogicalOperator sink = generator.createSinkOfSource(sourceQueryPart, sourceOperator, sourceNode);
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
				ILogicalOperator targetOperator = sinkSub.getSink();

				ILogicalQueryPart currentQueryPart = queryPartAssignment.get(currentOperator);
				ILogicalQueryPart targetQueryPart = queryPartAssignment.get(targetOperator);
				if (!currentQueryPart.equals(targetQueryPart)) {
					subsToReplace.put(sinkSub, currentOperator);
				}
			}
		}
		return subsToReplace;
	}

	public static void cutQueryPart(ILogicalQueryPart queryPart) {

		Preconditions.checkNotNull(queryPart, "Query part to be cut must be not null!");

		Collection<ILogicalOperator> operators = queryPart.getOperators();
		for (ILogicalOperator operator : operators) {

			Collection<LogicalSubscription> subsToRemove = Lists.newArrayList();
			for (LogicalSubscription subcription : operator.getSubscriptions()) {

				if (!operators.contains(subcription.getSink())) {
					subsToRemove.add(subcription);
				}
			}

			for (LogicalSubscription sub : subsToRemove) {
				operator.unsubscribeSink(sub);
			}

			subsToRemove.clear();
			for (LogicalSubscription subcription : operator.getSubscribedToSource()) {

				if (!operators.contains(subcription.getSource())) {
					subsToRemove.add(subcription);
				}
			}

			for (LogicalSubscription sub : subsToRemove) {
				operator.unsubscribeFromSource(sub);
			}

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

		for (ILogicalQueryPart copyQueryPart : map.keySet()) {
			ILogicalQueryPart originalQueryPart = map.get(copyQueryPart);

			Collection<ILogicalQueryPart> avoidedPartsInCopy = Lists.newArrayList();
			for (ILogicalQueryPart avoidedPartInOriginal : originalQueryPart.getAvoidingQueryParts()) {
				ILogicalQueryPart avoidedPartCopy = getCopyOfMap(avoidedPartInOriginal, map);

				if (avoidedPartCopy != null) {
					avoidedPartsInCopy.add(avoidedPartCopy);
				} else {
					LOG.error("Could not find copy of queryPart {}", avoidedPartInOriginal);
				}
			}

			copyQueryPart.addAvoidingQueryParts(avoidedPartsInCopy);
		}

		return map;
	}

	/**
	 * Makes as many copies of query parts as given by the number of copies. All
	 * copied query parts will be cut, so there will be no subscription from or
	 * to outwards a query part. <br />
	 * The origin query parts will not be modified.
	 * 
	 * @param queryParts
	 *            A collection of query parts to copy.
	 * @param numCopies
	 *            The number of copies to make.
	 * @throws NullPointerException
	 *             if <code>queryParts</code> is null.
	 */
	public static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyAndCutQueryParts(Collection<ILogicalQueryPart> queryParts, int numCopies) throws NullPointerException {

		// Preconditions
		if (queryParts == null)
			throw new NullPointerException("Query parts to be copied must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOriginPart = Maps.newHashMap();

		// Take care of unique IDs
		Collection<Map<ILogicalQueryPart, ILogicalQueryPart>> plainCopies = Lists.newArrayList();
		for (int copyNo = 0; copyNo < numCopies; copyNo++) {

			Map<ILogicalQueryPart, ILogicalQueryPart> partialPlainCopies = LogicalQueryHelper.copyQueryPartsDeep(queryParts);

			for (ILogicalQueryPart copiedPart : partialPlainCopies.keySet()) {

				for (ILogicalOperator copiedOperator : copiedPart.getOperators()) {

					if (copiedOperator.getUniqueIdentifier() != null) {

						copiedOperator.setUniqueIdentifier(copiedOperator.getUniqueIdentifier() + String.valueOf(copyNo));

					}

				}

			}

			plainCopies.add(partialPlainCopies);

		}

		for (Map<ILogicalQueryPart, ILogicalQueryPart> plainCopyMap : plainCopies) {

			for (ILogicalQueryPart copy : plainCopyMap.keySet()) {

				Collection<ILogicalQueryPart> copyList = null;

				if (copiesToOriginPart.containsKey(plainCopyMap.get(copy)))
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
				ILogicalOperator originalTarget = sub.getSink();
				ILogicalOperator copyTarget = getCopyOfMap(originalTarget, operatorCopyMap);

				if (copyTarget != null) {
					copyOperator.subscribeSink(copyTarget, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
				}
			}

			for (LogicalSubscription sub : originalOperator.getSubscribedToSource()) {
				ILogicalOperator orginalSource = sub.getSource();
				ILogicalOperator copySource = getCopyOfMap(orginalSource, operatorCopyMap);

				if (copySource == null) {
					copyOperator.subscribeToSource(orginalSource, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
				}
			}
		}
	}

	public static <T> T getCopyOfMap(T original, Map<T, T> copyMap) {
		for (T copy : copyMap.keySet()) {
			if (copyMap.get(copy).equals(original)) {
				return copy;
			}
		}

		return null;
	}

	/**
	 * Collects all copies of a given operator.
	 * 
	 * @param originPartOfOperator
	 *            The part containing the operator.
	 * @param copiesOfOriginPart
	 *            The copies of <code>originPartOfOperator</code>
	 * @param operator
	 *            The operator, whose copies shall be collected.
	 */
	@SuppressWarnings("unchecked")
	public static Collection<ILogicalOperator> collectCopies(ILogicalQueryPart originPartOfOperator, Collection<ILogicalQueryPart> copiesOfOriginPart, ILogicalOperator operator) {

		Preconditions.checkNotNull(operator, "The operator, whose copies shall be collected, must not be null!");
		Preconditions.checkNotNull(originPartOfOperator, "The query part containing the operator must not be null!");
		Preconditions.checkNotNull(copiesOfOriginPart, "The copies of the query part containing the operator must not be null!");

		// The return value
		Collection<ILogicalOperator> copies = Lists.newArrayList();

		int operatorIndex = ((List<ILogicalOperator>) originPartOfOperator.getOperators()).indexOf(operator);
		for (ILogicalQueryPart copyOfOriginPart : copiesOfOriginPart)
			copies.add(((List<ILogicalOperator>) copyOfOriginPart.getOperators()).get(operatorIndex));

		return copies;

	}

	public static ILogicalOperator appendTopAO(ILogicalQueryPart queryPart) {
		Preconditions.checkNotNull(queryPart, "Query part to append TopAO must not be null!");

		Collection<ILogicalOperator> sinks = LogicalPlan.getSinks(LogicalPlan.getAllOperators(queryPart.getOperators().iterator().next()));

		TopAO topAO = new TopAO();
		int inputPort = 0;
		for (ILogicalOperator sink : sinks) {
			topAO.subscribeToSource(sink, inputPort++, 0, sink.getOutputSchema());
		}

		return topAO;
	}

	public static Optional<ILogicalQueryPart> determineQueryPart(Collection<ILogicalQueryPart> queryParts, ILogicalOperator operator) {

		Preconditions.checkNotNull(queryParts, "Query parts for determination must be not null!");
		Preconditions.checkNotNull(operator, "Operator to determine query part must be not null!");

		for (ILogicalQueryPart part : queryParts) {

			if (part.getOperators().contains(operator))
				return Optional.of(part);

		}

		return Optional.absent();

	}

	/**
	 * Initializes all operators of a collection of query parts.
	 * 
	 * @param queryParts
	 *            The given collection of query parts. <br />
	 *            Must be not null.
	 */
	public static void initializeOperators(Collection<ILogicalQueryPart> queryParts) {

		Preconditions.checkNotNull(queryParts, "Parts, which operators shall be initialized, must be not null!");

		for (ILogicalQueryPart queryPart : queryParts) {

			LogicalQueryHelper.initializeOperators(queryPart);

		}

	}

	/**
	 * Initializes all operators of a query part.
	 * 
	 * @param queryPart
	 *            The given query part. <br />
	 *            Must be not null.
	 */
	public static void initializeOperators(ILogicalQueryPart queryPart) {

		Preconditions.checkNotNull(queryPart, "Part, which operators shall be initialized, must be not null!");

		for (ILogicalOperator operator : queryPart.getOperators()) {

			operator.initialize();

		}

	}

	public Optional<ILogicalQueryPart> determineQueryPart(ILogicalOperator operator, Collection<ILogicalQueryPart> queryParts) {
		for (ILogicalQueryPart part : queryParts) {
			if (part.getOperators().contains(operator)) {
				return Optional.of(part);
			}
		}
		return Optional.absent();
	}

	public static Optional<String> getBaseTimeunitString(SDFSchema schema) {
		Preconditions.checkNotNull(schema, "Schema to get basetimeunit from must not be null!");

		SDFConstraint baseTimeunitConstraint = schema.getConstraint(SDFConstraint.BASE_TIME_UNIT);
		if (baseTimeunitConstraint != null) {
			Object value = baseTimeunitConstraint.getValue();
			if (value != null && value instanceof TimeUnit) {
				return Optional.of(((TimeUnit) value).toString());
			}
		}
		
		return Optional.absent();
	}
}
