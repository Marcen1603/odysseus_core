package de.uniol.inf.is.odysseus.net.querydistribute.modify;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartModificationException;

/**
 * The modification helper provides useful methods for modification.
 * 
 * @author Michael Brand
 */
public class ModificationHelper {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ModificationHelper.class);

	/**
	 * The bound {@link IOdysseusNodeManager}.
	 */
	private static IOdysseusNodeManager nodeManager;

	/**
	 * Binds an {@link IOdysseusNodeManager}.
	 */
	public static void bindNodeManager(IOdysseusNodeManager manager) {
		nodeManager = manager;
	}

	/**
	 * Unbinds an {@link IOdysseusNodeManager}, if <code>manager</code> is the bound one.
	 */
	public static void unbindNodeManager(IOdysseusNodeManager manager) {
		if (manager == nodeManager) {
			nodeManager = null;
		}
	}

	/**
	 * All possible patterns for a string identifying the start or end point of
	 * a modification.
	 */
	public static final String[] PATTERN_START_OR_END_POINT_ID = { "\\w+",
			"\\w+:\\w+", "\\w+.\\w+", "\\w+:\\w+.\\w+" };

	/**
	 * The separator for strings identifying the start and end point of a
	 * modification.
	 */
	public static final String PATTERN_START_OR_END_POINT_SEPARATOR = ",";

	/**
	 * The brackets for strings identifying the start and end point of a
	 * modification.
	 */
	public static final String[] PATTERN_START_OR_END_POINT_BRACKETS = { "\\[",
			"\\]" };

	/**
	 * Gets all possible patterns for the parameter identifying the start and
	 * end point of a modification.
	 * 
	 * @return All possible patterns for the parameter identifying the start and
	 *         end point of a modification.
	 */
	public static String[] getStartAndEndPointIDPatterns() {

		final String startBracket = ModificationHelper.PATTERN_START_OR_END_POINT_BRACKETS[0];
		final String endBracket = ModificationHelper.PATTERN_START_OR_END_POINT_BRACKETS[1];
		final String separator = ModificationHelper.PATTERN_START_OR_END_POINT_SEPARATOR;
		List<String> patterns = Lists.newArrayList();

		for (int index = 0; index < ModificationHelper.PATTERN_START_OR_END_POINT_ID.length; index++) {

			final String pattern = ModificationHelper.PATTERN_START_OR_END_POINT_ID[index];
			patterns.add(pattern);
			patterns.add(startBracket + pattern + endBracket);

			for (int sndIndex = 0; sndIndex < ModificationHelper.PATTERN_START_OR_END_POINT_ID.length; sndIndex++) {

				final String sndPattern = ModificationHelper.PATTERN_START_OR_END_POINT_ID[sndIndex];
				patterns.add(startBracket + pattern + separator + sndPattern
						+ endBracket);

			}

		}

		String[] result = new String[patterns.size()];
		patterns.toArray(result);
		return result;

	}

	/**
	 * Checks if two operators are equal or if the first is above the second.
	 * 
	 * @param op1
	 *            The first operator.
	 * @param op2
	 *            The second operator.
	 * @return True, if <code>op1</code> is equal to <code>op2</code> or if
	 *         <code>op1</code> is above <code>op2</code>.
	 */
	public static boolean isOperatorAboveOrEqual(ILogicalOperator op1,
			ILogicalOperator op2) {

		Preconditions.checkNotNull(op1, "Operator must be not null!");
		Preconditions.checkNotNull(op2, "Operator must be not null!");

		if (op1.equals(op2)) {

			return true;

		}

		for (LogicalSubscription subToSource : op1.getSubscribedToSource()) {

			if (ModificationHelper.isOperatorAboveOrEqual(
					subToSource.getSource(), op2))
				return true;

		}

		return false;

	}

	/**
	 * Assures not to split a {@link RenameAO} and it's source.
	 * 
	 * @param operator
	 *            The operator, which may be subscribed by a {@link RenameAO}.
	 * @return The subscribed {@link RenameAO} or <code>operator</code>, if no
	 *         {@link RenameAO} is subscribed.
	 */
	public static ILogicalOperator handleRenameAOs(ILogicalOperator operator) {

		// Preconditions
		Preconditions.checkNotNull(operator, "Operator must be not null!");
		if (operator.getSubscriptions() == null
				|| operator.getSubscriptions().size() != 1) {

			return operator;

		}

		ILogicalOperator target = operator.getSubscriptions().iterator().next()
				.getSink();
		if (target instanceof RenameAO) {

			return target;

		}

		return operator;

	}

	/**
	 * Finds an operator by it's unique id or by it's resource name for
	 * {@link AbstractAccessAO}s and {@link StreamAO}s
	 * 
	 * @param queryParts
	 *            The query parts to search within.
	 * @param id
	 *            The unique id or resource name.
	 * @return The operator with <code>id</code> as unique id or as resource
	 *         name for {@link AbstractAccessAO}s and {@link StreamAO}s.
	 *         {@link Optional#absent()}, if no operator could be found.
	 */
	public static Optional<ILogicalOperator> findOperatorByID(
			Collection<ILogicalQueryPart> queryParts, String id) {

		// Preconditions
		Preconditions.checkNotNull(queryParts,
				"List of query parts must be not null!");
		Preconditions.checkNotNull(!queryParts.isEmpty(),
				"List of query parts must be not empty!");
		Preconditions.checkNotNull(id, "ID of operator must be not null!");

		for (ILogicalQueryPart queryPart : queryParts) {

			for (ILogicalOperator operator : queryPart.getOperators()) {

				if ((operator instanceof AbstractAccessAO && ((AbstractAccessAO) operator)
						.getAccessAOName().getResourceName().equals(id))
						|| (operator instanceof StreamAO && ((StreamAO) operator)
								.getStreamname().getResourceName().equals(id))
						|| (operator.getUniqueIdentifier() != null && operator
								.getUniqueIdentifier().equals(id))) {

					return Optional.of(ModificationHelper
							.handleRenameAOs(operator));

				}

			}

		}

		return Optional.absent();

	}

	/**
	 * Checks if the first is above the second.
	 * 
	 * @param op1
	 *            The first operator.
	 * @param op2
	 *            The second operator.
	 * @return True, if <code>op1</code> is above <code>op2</code>.
	 */
	public static boolean isOperatorAbove(ILogicalOperator op1,
			ILogicalOperator op2) {

		Preconditions.checkNotNull(op1, "Operator must be not null!");
		Preconditions.checkNotNull(op2, "Operator must be not null!");

		if (op1.equals(op2)) {

			return false;

		}

		for (LogicalSubscription subToSource : op1.getSubscribedToSource()) {

			if (ModificationHelper.isOperatorAboveOrEqual(
					subToSource.getSource(), op2))
				return true;

		}

		return false;

	}

	/**
	 * Finds all copies of an operator.
	 * 
	 * @param operator
	 *            The given operator.
	 * @param copyMap
	 *            The mapping of copied query parts to their originals.
	 * @return A collection of operators found in {@link Map#values()} of
	 *         <code>copyMap</code> having the same index as
	 *         <code>operator</code> in the original query part (key of
	 *         <code>copyMap</code>).
	 */
	@SuppressWarnings("unchecked")
	public static Collection<ILogicalOperator> findCopies(
			ILogicalOperator operator,
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap) {

		Preconditions.checkNotNull(operator,
				"Origin operator must be not null!");
		Preconditions.checkNotNull(copyMap,
				"Map of copied query parts to originals must be not null!");

		Collection<ILogicalOperator> copies = Lists.newArrayList();

		for (ILogicalQueryPart oringinalPart : copyMap.keySet()) {

			if (!oringinalPart.contains(operator)) {

				continue;

			}

			final int operatorIndex = ((List<ILogicalOperator>) oringinalPart
					.getOperators()).indexOf(operator);

			for (ILogicalQueryPart copiedPart : copyMap.get(oringinalPart)) {

				List<ILogicalOperator> operatorsAsList = (List<ILogicalOperator>) copiedPart
						.getOperators();
				copies.add(operatorsAsList.get(operatorIndex));

			}

			break;

		}

		return copies;

	}

	/**
	 * Connects sets of operators.
	 * 
	 * @param sources
	 *            The operators to be connected each with each operator within
	 *            <code>targets</code>.
	 * @param targets
	 *            The operators to be connected each with each operator within
	 *            <code>sources</code>.
	 * @param originSubscription
	 *            The original subscription to be used.
	 */
	public static void connectOperators(Collection<ILogicalOperator> sources,
			Collection<ILogicalOperator> targets,
			LogicalSubscription originSubscription) {

		Preconditions.checkNotNull(originSubscription,
				"Logical subscription must be not null!");
		Preconditions
				.checkNotNull(sources, "Logical sources must be not null!");
		Preconditions
				.checkNotNull(targets, "Logical targtes must be not null!");

		for (int copyNo = 0; copyNo < sources.size(); copyNo++) {

			ILogicalOperator source = ((List<ILogicalOperator>) sources)
					.get(copyNo);
			ILogicalOperator target = ((List<ILogicalOperator>) targets)
					.get(copyNo % targets.size());

			source.subscribeToSource(target,
					originSubscription.getSinkInPort(),
					originSubscription.getSourceOutPort(),
					originSubscription.getSchema());

		}

	}

	/**
	 * Collects all copied query parts.
	 * 
	 * @param copyMap
	 *            The mapping of copied query parts to original ones.
	 * @return A single collection containing all copied query parts.
	 */
	public static Collection<ILogicalQueryPart> collectCopies(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap) {

		Preconditions.checkNotNull(copyMap, "Copy map must be not null!");

		Collection<ILogicalQueryPart> copies = Lists.newArrayList();
		for (ILogicalQueryPart originalPart : copyMap.keySet()) {

			copies.addAll(copyMap.get(originalPart));

		}

		return copies;

	}

	/**
	 * Determines the degree of modification (e.g., the number of fragments).
	 * 
	 * @param degreeParameter
	 *            The parameter identifying the degree.
	 * @param minDegree
	 *            The minimal degree of modification.
	 * @return The degree of modification given by the parameters for
	 *         modification.
	 * @throws QueryPartModificationException
	 *             if any error occurs.
	 */
	public static int determineDegreeOfModification(String degreeParameter,
			int minDegree) throws QueryPartModificationException {

		int degree;
		try {

			degree = Integer.parseInt(degreeParameter);

		} catch (Exception e) {

			throw new QueryPartModificationException(
					"The degree of modification, must be an integer!");

		}

		if (degree < minDegree) {

			throw new QueryPartModificationException(
					"The degree of modification must be greater than 2!");

		}

		return degree;

	}

	/**
	 * Determines the operators, where the modification starts and ends given by
	 * the parameters for modification.
	 * 
	 * @param queryParts
	 *            The given query parts.
	 * @param startAndEndPointParameter
	 *            The parameter identifying the modification start and end.
	 * @return A pair as follows: <br />
	 *         {@link IPair#getE1()} is the operator, where the modification
	 *         starts. <br />
	 *         {@link IPair#getE2()} is the operator, where the modification
	 *         ends or {@link Optional#absent()}, if no id for an end operator
	 *         is given.
	 * @throws QueryPartModificationException
	 *             if any error occurs.
	 */
	public static IPair<ILogicalOperator, Optional<ILogicalOperator>> determineStartAndEndPoints(
			Collection<ILogicalQueryPart> queryParts,
			String startAndEndPointParameter)
			throws QueryPartModificationException {

		boolean matchesPattern = false;
		for (String pattern : ModificationHelper
				.getStartAndEndPointIDPatterns()) {

			if (startAndEndPointParameter.matches(pattern)) {

				matchesPattern = true;
				break;

			}

		}
		if (!matchesPattern) {

			throw new QueryPartModificationException(
					startAndEndPointParameter
							+ " is not a valid string for a start and end point of a modification");

		}

		for (String pattern : ModificationHelper.PATTERN_START_OR_END_POINT_BRACKETS) {

			startAndEndPointParameter = startAndEndPointParameter.replaceAll(
					pattern, "");

		}

		String[] startAndEndPointParameterArray = startAndEndPointParameter
				.split(ModificationHelper.PATTERN_START_OR_END_POINT_SEPARATOR);
		String startID = startAndEndPointParameterArray[0].trim().split("\\.")[0];
		Optional<String> endID = Optional.absent();
		if (startAndEndPointParameterArray.length > 1) {

			endID = Optional.of(startAndEndPointParameterArray[1].trim());

		}

		if (endID.isPresent() && startID.equals(endID.get())) {

			throw new QueryPartModificationException(
					"IDs for start and end point of a modification must be different!");

		}

		Optional<ILogicalOperator> startOperator = ModificationHelper
				.findOperatorByID(queryParts, startID);
		if (!startOperator.isPresent()) {

			throw new QueryPartModificationException(
					"Could not find an operator with " + startID
							+ " as ID or resource!");

		}

		Optional<ILogicalOperator> endOperator = Optional.absent();
		if (endID.isPresent()) {

			endOperator = ModificationHelper.findOperatorByID(queryParts,
					endID.get());

		}

		return new Pair<ILogicalOperator, Optional<ILogicalOperator>>(
				startOperator.get(), endOperator);

	}

	/**
	 * Checks, if there are enough nodes available for the desired degree of
	 * modification.
	 * 
	 * @param numQueryPartsToAllocate
	 *            The number of query parts to allocate.
	 * @return True, if the number of available nodes is greater than or equal
	 *         to the degree of modification.
	 */
	public static boolean validateDegreeOfModification(
			int numQueryPartsToAllocate) {

		if (ModificationHelper.nodeManager == null) {

			ModificationHelper.LOG.error("No Node Manager found!");
			return false;

		}

		int availableNodes = ModificationHelper.nodeManager.getNodes()
				.size();

		if (availableNodes + 1 < numQueryPartsToAllocate) {

			ModificationHelper.LOG.warn("Got {} nodes. {} nodes needed!",
					availableNodes, numQueryPartsToAllocate);
			return false;

		} else if (availableNodes + 1 == numQueryPartsToAllocate) {

			ModificationHelper.LOG
					.warn("Got enough nodes for a suitable modification only if the local node is considered!");

		}

		return true;

	}

	/**
	 * Calculates the number of query parts within a 2-dimensional collection.
	 * 
	 * @param values
	 *            The given 2-dimensional collection.
	 */
	public static int calcSize(Collection<Collection<ILogicalQueryPart>> values) {

		int size = 0;
		for (Collection<ILogicalQueryPart> innerCollection : values) {

			size += innerCollection.size();

		}

		return size;

	}

}