package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;

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
	private static final int parameterIndex_startAndEndPointIDs = 0;

	/**
	 * The index of the parameter identifying the degree of fragmentation.
	 */
	private static final int parameterIndex_degreeOfFragmentation = 1;

	/**
	 * All possible patterns for a string identifying the start or end point of
	 * fragmentation.
	 */
	private static final String[] startOrEndPointPatterns = { "\\w+",
			"\\w+:\\w+" };

	/**
	 * The separator for strings identifying the start or end point of
	 * fragmentation.
	 */
	private static final String startOrEndPointSeparator = ",";

	/**
	 * The minimum degree of fragmentation.
	 */
	public static final int minDegreeOfFragmentation = 2;

	/**
	 * All possible patterns for the parameter identifying the start and end
	 * point of fragmentation.
	 */
	public static final String[] startAndEndPointPatterns = {
			AbstractFragmentationHelper.startOrEndPointPatterns[0],
			"\\[" + AbstractFragmentationHelper.startOrEndPointPatterns[0]
					+ "\\]",
			AbstractFragmentationHelper.startOrEndPointPatterns[1],
			"\\[" + AbstractFragmentationHelper.startOrEndPointPatterns[1]
					+ "\\]",
			"\\[" + AbstractFragmentationHelper.startOrEndPointPatterns[0]
					+ AbstractFragmentationHelper.startOrEndPointSeparator
					+ AbstractFragmentationHelper.startOrEndPointPatterns[0]
					+ "\\]",
			"\\[" + AbstractFragmentationHelper.startOrEndPointPatterns[0]
					+ AbstractFragmentationHelper.startOrEndPointSeparator
					+ AbstractFragmentationHelper.startOrEndPointPatterns[1]
					+ "\\]",
			"\\[" + AbstractFragmentationHelper.startOrEndPointPatterns[1]
					+ AbstractFragmentationHelper.startOrEndPointSeparator
					+ AbstractFragmentationHelper.startOrEndPointPatterns[0]
					+ "\\]",
			"\\[" + AbstractFragmentationHelper.startOrEndPointPatterns[1]
					+ AbstractFragmentationHelper.startOrEndPointSeparator
					+ AbstractFragmentationHelper.startOrEndPointPatterns[1]
					+ "\\]" };

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
	private static final boolean isOperatorAboveOrEqual(ILogicalOperator op1,
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
	protected static final ILogicalOperator handleRenameAOs(
			ILogicalOperator operator) {

		// Preconditions
		Preconditions.checkNotNull(operator, "Operator must be not null!");
		if (operator.getSubscriptions() == null
				|| operator.getSubscriptions().size() != 1) {

			return operator;

		}

		final ILogicalOperator target = operator.getSubscriptions().iterator()
				.next().getTarget();
		if (target instanceof RenameAO) {

			return target;

		}

		return operator;

	}

	/**
	 * Finds an operator by it's unique id or by it's resource name for
	 * {@link AbstractAccessAO}s.
	 * 
	 * @param queryParts
	 *            The query parts to search within.
	 * @param id
	 *            The unique id or resource name.
	 * @return The operator with <code>id</code> as unique id or as resource
	 *         name for {@link AbstractAccessAO}s. {@link Optional#absent()}, if
	 *         no operator could be found.
	 */
	protected static final Optional<ILogicalOperator> findOperatorByID(
			Collection<ILogicalQueryPart> queryParts, String id) {

		// Preconditions
		Preconditions.checkNotNull(queryParts,
				"List of query parts must be not null!");
		Preconditions.checkNotNull(!queryParts.isEmpty(),
				"List of query parts must be not empty!");
		Preconditions.checkNotNull(id, "ID of operator must be not null!");

		for (ILogicalQueryPart queryPart : queryParts) {

			for (ILogicalOperator operator : queryPart.getOperators()) {

				if (operator instanceof AbstractAccessAO
						&& ((AbstractAccessAO) operator).getAccessAOName()
								.getResourceName().equals(id)) {

					return Optional.of(AbstractFragmentationHelper
							.handleRenameAOs(operator));

				} else if (operator.getUniqueIdentifier() != null
						&& operator.getUniqueIdentifier().equals(id)) {

					return Optional.of(AbstractFragmentationHelper
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
	public static final boolean isOperatorAbove(ILogicalOperator op1,
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
	 * The parameters for the fragmentation.
	 */
	protected final ImmutableList<String> fragmentationParameters;

	/**
	 * Creates a new fragmentation helper.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 */
	public AbstractFragmentationHelper(List<String> fragmentationParameters) {

		// Preconditions
		Preconditions.checkNotNull(fragmentationParameters,
				"Fragmentation parameters must be not null!");

		this.fragmentationParameters = ImmutableList
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
	 *             {@value #minDegreeOfFragmentation}.
	 */
	public int determineDegreeOfFragmentation()
			throws QueryPartModificationException {

		// Preconditions
		if (this.fragmentationParameters.size() < AbstractFragmentationHelper.parameterIndex_degreeOfFragmentation + 1) {

			throw new QueryPartModificationException(
					"Fragmentation parameters must at least contain two values!");

		}

		int degree;
		try {

			degree = Integer
					.parseInt(this.fragmentationParameters
							.get(AbstractFragmentationHelper.parameterIndex_degreeOfFragmentation));

		} catch (Exception e) {

			throw new QueryPartModificationException(
					"The second fragmentation parameter, the degree of fragmentation, must be an integer!");

		}

		if (degree < AbstractFragmentationHelper.minDegreeOfFragmentation) {

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
	 *             {@value #startAndEndPointPatterns}.
	 */
	public IPair<ILogicalOperator, Optional<ILogicalOperator>> determineStartAndEndPoints(
			Collection<ILogicalQueryPart> queryParts)
			throws QueryPartModificationException {

		// Preconditions
		if (this.fragmentationParameters.size() < AbstractFragmentationHelper.parameterIndex_startAndEndPointIDs + 1) {

			throw new QueryPartModificationException(
					"Fragmentation parameters must at least contain one value!");

		}

		String startAndEndPointParameter = this.fragmentationParameters
				.get(AbstractFragmentationHelper.parameterIndex_startAndEndPointIDs);
		boolean matchesPattern = false;
		for (String pattern : AbstractFragmentationHelper.startAndEndPointPatterns) {

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

		startAndEndPointParameter = startAndEndPointParameter.replaceAll("\\[",
				"");
		startAndEndPointParameter = startAndEndPointParameter.replaceAll("\\]",
				"");

		final String[] startAndEndPointParameterArray = startAndEndPointParameter
				.split(AbstractFragmentationHelper.startOrEndPointSeparator);
		final String startID = startAndEndPointParameterArray[0].trim();
		Optional<String> endID = Optional.absent();
		if (startAndEndPointParameterArray.length > 1) {

			endID = Optional.of(startAndEndPointParameterArray[1].trim());

		}

		if (startID.equals(endID)) {

			throw new QueryPartModificationException(
					"IDs for start and end point of a fragmentation must be different!");

		}

		final Optional<ILogicalOperator> startOperator = AbstractFragmentationHelper
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
	 * Checks if an operator shall not be part of a fragment.
	 * 
	 * @param operator
	 *            The operator to check.
	 * @return True, if <code>operator</code> shall not be part of a fragment;
	 *         false else.
	 */
	public abstract boolean isOperatorException(ILogicalOperator operator);

}