package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public abstract class AbstractFragmentationHelper {

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(AbstractFragmentationHelper.class);

	private static final int parameterIndex_startAndEndPointIDs = 0;

	private static final int parameterIndex_degreeOfFragmentation = 1;

	private static final int minDegreeOfFragmentation = 2;

	private static final String[] startAndEndPointPatterns = { "\\w+",
			"\\[\\w+\\]", "\\[\\w+\\s\\w+\\]" };

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
	
	protected final ImmutableList<String> fragmentationParameters;

	public AbstractFragmentationHelper(List<String> fragmentationParameters) {

		// Preconditions
		Preconditions.checkNotNull(fragmentationParameters,
				"Fragmentation parameters must be not null!");

		this.fragmentationParameters = ImmutableList
				.copyOf(fragmentationParameters);

	}

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

		AbstractFragmentationHelper.log.debug("Degree of fragmentation = "
				+ degree);
		return degree;

	}

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

		startAndEndPointParameter = startAndEndPointParameter
				.replace("\\[", "");
		startAndEndPointParameter = startAndEndPointParameter
				.replace("\\]", "");

		final String[] startAndEndPointParameterArray = startAndEndPointParameter
				.split(" ");
		final String startID = startAndEndPointParameterArray[0].trim();
		Optional<String> endID = Optional.absent();
		if (startAndEndPointParameterArray.length > 1) {

			endID = Optional.of(startAndEndPointParameterArray[1].trim());

		}

		if (startID.equals(endID)) {

			throw new QueryPartModificationException(
					"IDs for start and end point of a fragmentation must be different!");

		}

		AbstractFragmentationHelper.log
				.debug("ID of start point for fragmentation = " + startID);
		if (endID.isPresent()) {

			AbstractFragmentationHelper.log
					.debug("ID of end point for fragmentation = " + endID.get());

		} else {

			AbstractFragmentationHelper.log
					.debug("ID of end point for fragmentation is not given");

		}

		final Optional<ILogicalOperator> startOperator = AbstractFragmentationHelper
				.findOperatorByID(queryParts, startID);
		if (!startOperator.isPresent()) {

			throw new QueryPartModificationException(
					"Could not find an operator with " + startID
							+ " as ID or resource!");

		} else {

			AbstractFragmentationHelper.log
					.debug("Operator of start point for fragmentation = "
							+ startOperator.get());

		}

		Optional<ILogicalOperator> endOperator = Optional.absent();
		if (endID.isPresent()) {

			endOperator = AbstractFragmentationHelper.findOperatorByID(
					queryParts, endID.get());
			if (!endOperator.isPresent()) {

				AbstractFragmentationHelper.log
						.debug("Operator of end point for fragmentation = "
								+ endOperator.get());

			} else {

				AbstractFragmentationHelper.log
						.debug("Operator of end point for fragmentation = "
								+ endOperator.get());

			}

		}

		return new Pair<ILogicalOperator, Optional<ILogicalOperator>>(
				startOperator.get(), endOperator);

	}
	
	public abstract boolean isOperatorException(ILogicalOperator operator);

}