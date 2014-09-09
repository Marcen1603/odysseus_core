package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * An fragmentation helper provides useful methods for fragmentation.
 * 
 * @author Michael Brand
 */
public abstract class AbstractFragmentationHelper {

	/**
	 * The index of the parameter identifying the start and end point of
	 * fragmentation.
	 */
	public static final int PARAMETER_INDEX_START_AND_END_POINT_IDS = 0;

	/**
	 * The index of the parameter identifying the degree of fragmentation.
	 */
	public static final int PARAMETER_INDEX_DEGREE_OF_FRAGMENTATION = 1;

	/**
	 * All possible patterns for a string identifying the start or end point of
	 * fragmentation.
	 */
	public static final String[] PATTERN_START_OR_END_POINT_ID = { "\\w+",
			"\\w+:\\w+", "\\w+.\\w+", "\\w+:\\w+.\\w+" };

	/**
	 * The separator for strings identifying the start and end point of
	 * fragmentation.
	 */
	public static final String PATTERN_START_OR_END_POINT_SEPARATOR = ",";

	/**
	 * The brackets for strings identifying the start and end point of
	 * fragmentation.
	 */
	public static final String[] PATTERN_START_OR_END_POINT_BRACKETS = { "\\[",
			"\\]" };

	/**
	 * The minimum degree of fragmentation.
	 */
	public static final int MIN_DEGREE_OF_FRAGMENTATION = 2;

	/**
	 * Gets all possible patterns for the parameter identifying the start and
	 * end point of fragmentation.
	 * 
	 * @return All possible patterns for the parameter identifying the start and
	 *         end point of fragmentation.
	 */
	private static String[] getStartAndEndPointIDPatterns() {

		final String startBracket = AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_BRACKETS[0];
		final String endBracket = AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_BRACKETS[1];
		final String separator = AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_SEPARATOR;
		List<String> patterns = Lists.newArrayList();

		for (int index = 0; index < AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_ID.length; index++) {

			final String pattern = AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_ID[index];
			patterns.add(pattern);
			patterns.add(startBracket + pattern + endBracket);

			for (int sndIndex = 0; sndIndex < AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_ID.length; sndIndex++) {

				final String sndPattern = AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_ID[sndIndex];
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
	private static boolean isOperatorAboveOrEqual(ILogicalOperator op1,
			ILogicalOperator op2) {

		Preconditions.checkNotNull(op1, "Operator must be not null!");
		Preconditions.checkNotNull(op2, "Operator must be not null!");

		if (op1.equals(op2)) {

			return true;

		}

		for (LogicalSubscription subToSource : op1.getSubscribedToSource()) {

			if (AbstractFragmentationHelper.isOperatorAboveOrEqual(
					subToSource.getTarget(), op2))
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
	private static ILogicalOperator handleRenameAOs(ILogicalOperator operator) {

		// Preconditions
		Preconditions.checkNotNull(operator, "Operator must be not null!");
		if (operator.getSubscriptions() == null
				|| operator.getSubscriptions().size() != 1) {

			return operator;

		}

		ILogicalOperator target = operator.getSubscriptions().iterator().next()
				.getTarget();
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
	private static Optional<ILogicalOperator> findOperatorByID(
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

					return Optional.of(AbstractFragmentationHelper
							.handleRenameAOs(operator));

				}

			}

		}

		return Optional.absent();

	}

	/**
	 * Searches for fragmentation rules for a given combination of a
	 * fragmentation strategy and an operator.
	 * 
	 * @param strategy
	 *            The given fragmentation strategy.
	 * @param operator
	 *            The given operator.
	 * @return All available rules.
	 */
	public static Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> getFragmentationRules(
			AbstractFragmentationQueryPartModificator strategy,
			ILogicalOperator operator) {

		Preconditions.checkNotNull(strategy,
				"Fragmentation strategy must be not null!");
		Preconditions.checkNotNull(operator, "Operator must be not null!");

		Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = Lists
				.newArrayList();

		for (IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> rule : Activator
				.getFragmentationRules()) {

			if (rule.getStrategyClass().isAssignableFrom(strategy.getClass())
					&& rule.getOperatorClass().isAssignableFrom(
							operator.getClass())) {

				rules.add(rule);

			}

		}

		return rules;

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

			if (AbstractFragmentationHelper.isOperatorAboveOrEqual(
					subToSource.getTarget(), op2))
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
	 * The parameters for the fragmentation.
	 */
	protected ImmutableList<String> mFragmentationParameters;

	/**
	 * Creates a new fragmentation helper.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 */
	public AbstractFragmentationHelper(List<String> fragmentationParameters) {

		Preconditions.checkNotNull(fragmentationParameters,
				"Fragmentation parameters must be not null!");

		this.mFragmentationParameters = ImmutableList
				.copyOf(fragmentationParameters);

	}

	/**
	 * Determines the degree of fragmentation.
	 * 
	 * @return The degree of fragmentation given by the parameters for
	 *         fragmentation.
	 * @throws QueryPartModificationException
	 *             If the parameters for fragmentation do not contain two
	 *             parameters, if the second parameter is no integer or if the
	 *             degree of fragmentation if lower than
	 *             {@value #MIN_DEGREE_OF_FRAGMENTATION}.
	 */
	public int determineDegreeOfFragmentation()
			throws QueryPartModificationException {

		// Preconditions
		if (this.mFragmentationParameters.size() < AbstractFragmentationHelper.PARAMETER_INDEX_DEGREE_OF_FRAGMENTATION + 1) {

			throw new QueryPartModificationException(
					"Fragmentation parameters must at least contain two values!");

		}

		int degree;
		try {

			degree = Integer
					.parseInt(this.mFragmentationParameters
							.get(AbstractFragmentationHelper.PARAMETER_INDEX_DEGREE_OF_FRAGMENTATION));

		} catch (Exception e) {

			throw new QueryPartModificationException(
					"The second fragmentation parameter, the degree of fragmentation, must be an integer!");

		}

		if (degree < AbstractFragmentationHelper.MIN_DEGREE_OF_FRAGMENTATION) {

			throw new QueryPartModificationException(
					"The second fragmentation parameter, the degree of fragmentation must be greater than 2!");

		}

		return degree;

	}

	/**
	 * Determines the operators, where the fragmentation starts and ends given
	 * by the parameters for fragmentation.
	 * 
	 * @param queryParts
	 *            The given query parts.
	 * @return A pair as follows: <br />
	 *         {@link IPair#getE1()} is the operator, where the fragmentation
	 *         starts <br />
	 *         {@link IPair#getE2()} is the operator, where the fragmentation
	 *         ends or {@link Optional#absent()}, if no id for an end operator
	 *         is given.
	 * @throws QueryPartModificationException
	 *             If the parameters for fragmentation is empty or if the first
	 *             parameter does not match any patterns within
	 *             {@value #getStartAndEndPointIDPatterns()}.
	 */
	public IPair<ILogicalOperator, Optional<ILogicalOperator>> determineStartAndEndPoints(
			Collection<ILogicalQueryPart> queryParts)
			throws QueryPartModificationException {

		// Preconditions
		if (this.mFragmentationParameters.size() < AbstractFragmentationHelper.PARAMETER_INDEX_START_AND_END_POINT_IDS + 1) {

			throw new QueryPartModificationException(
					"Fragmentation parameters must at least contain one value!");

		}

		String startAndEndPointParameter = this.mFragmentationParameters
				.get(AbstractFragmentationHelper.PARAMETER_INDEX_START_AND_END_POINT_IDS);
		boolean matchesPattern = false;
		for (String pattern : AbstractFragmentationHelper
				.getStartAndEndPointIDPatterns()) {

			if (startAndEndPointParameter.matches(pattern)) {

				matchesPattern = true;
				break;

			}

		}
		if (!matchesPattern) {

			throw new QueryPartModificationException(
					startAndEndPointParameter
							+ " is not a valid string for a start and end point of a fragmentation");

		}

		for (String pattern : AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_BRACKETS) {

			startAndEndPointParameter = startAndEndPointParameter.replaceAll(
					pattern, "");

		}

		String[] startAndEndPointParameterArray = startAndEndPointParameter
				.split(AbstractFragmentationHelper.PATTERN_START_OR_END_POINT_SEPARATOR);
		String startID = startAndEndPointParameterArray[0].trim().split("\\.")[0];
		Optional<String> endID = Optional.absent();
		if (startAndEndPointParameterArray.length > 1) {

			endID = Optional.of(startAndEndPointParameterArray[1].trim());

		}

		if (startID.equals(endID)) {

			throw new QueryPartModificationException(
					"IDs for start and end point of a fragmentation must be different!");

		}

		Optional<ILogicalOperator> startOperator = AbstractFragmentationHelper
				.findOperatorByID(queryParts, startID);
		if (!startOperator.isPresent()) {

			throw new QueryPartModificationException(
					"Could not find an operator with " + startID
							+ " as ID or resource!");

		}

		Optional<ILogicalOperator> endOperator = Optional.absent();
		if (endID.isPresent()) {

			endOperator = AbstractFragmentationHelper.findOperatorByID(
					queryParts, endID.get());

		}

		return new Pair<ILogicalOperator, Optional<ILogicalOperator>>(
				startOperator.get(), endOperator);

	}

	/**
	 * Checks, if a given operator can be part of a fragment.
	 * 
	 * @param strategy
	 *            The given strategy.
	 * @param operator
	 *            The given operator.
	 * @return True, if <code>operator</code> can be executed in
	 *         intra-operational parallelism and therefore be part of a
	 *         fragment.
	 */
	public boolean canOperatorBePartOfFragment(
			AbstractFragmentationQueryPartModificator strategy,
			ILogicalOperator operator) {

		Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = AbstractFragmentationHelper
				.getFragmentationRules(strategy, operator);

		for (IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> rule : rules) {

			if (!rule.canOperatorBePartOfFragments(strategy, operator, this)) {

				return false;

			}

		}

		return true;

	}

	/**
	 * Checks if a query part needs a special handling. <br />
	 * This may be the usage of partial aggregates.
	 * 
	 * @param part
	 *            The query part.
	 * @param strategy
	 *            The given strategy.
	 * @param helper
	 *            The current fragmentation helper.
	 * @return True, if <code>part</code> needs a special handling. This makes
	 *         direct connection without fragmentation and/or reunion operators
	 *         impossible.
	 */
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AbstractFragmentationQueryPartModificator strategy,
			AbstractFragmentationHelper helper) {

		for (ILogicalOperator operator : part.getOperators()) {

			Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = AbstractFragmentationHelper
					.getFragmentationRules(strategy, operator);

			for (IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> rule : rules) {

				if (rule.needSpecialHandlingForQueryPart(part, operator, helper)) {

					return true;

				}

			}

		}

		return false;

	}

}