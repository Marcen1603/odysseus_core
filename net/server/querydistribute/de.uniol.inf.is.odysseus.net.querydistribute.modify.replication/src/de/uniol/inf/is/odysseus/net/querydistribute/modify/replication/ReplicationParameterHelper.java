package de.uniol.inf.is.odysseus.net.querydistribute.modify.replication;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.net.querydistribute.modify.IModificationParameterHelper;
import de.uniol.inf.is.odysseus.net.querydistribute.modify.ModificationHelper;

/**
 * A replication helper provides useful methods for replication.
 * 
 * @author Michael Brand
 */
public class ReplicationParameterHelper implements IModificationParameterHelper {

	/**
	 * The index of the parameter identifying the start and end point of
	 * replication, if given.
	 */
	public static final int PARAMETER_INDEX_START_AND_END_POINT_IDS = 0;

	/**
	 * The index of the parameter identifying the degree of replication, if no
	 * start point is given.
	 */
	public static final int PARAMETER_MIN_INDEX_DEGREE_OF_REPLICATION = 0;

	/**
	 * The index of the parameter identifying the degree of replication, if a
	 * start point is given.
	 */
	public static final int PARAMETER_MAX_INDEX_DEGREE_OF_REPLICATION = 1;

	/**
	 * The minimum degree of replication.
	 */
	public static final int MIN_DEGREE_OF_REPLICATION = 2;

	/**
	 * The parameters for the replication.
	 */
	protected ImmutableList<String> mReplicationParameters;

	/**
	 * Creates a new replication helper.
	 * 
	 * @param replicationParameters
	 *            The parameters for replication.
	 */
	public ReplicationParameterHelper(List<String> replicationParameters) {

		Preconditions.checkNotNull(replicationParameters, "Replication parameters must be not null!");

		this.mReplicationParameters = ImmutableList.copyOf(replicationParameters);

	}

	@Override
	public int determineDegreeOfModification() throws QueryPartModificationException {

		// Preconditions
		if (this.mReplicationParameters.size() < ReplicationParameterHelper.PARAMETER_MIN_INDEX_DEGREE_OF_REPLICATION
				+ 1) {

			throw new QueryPartModificationException("Replication parameters must at least contain one value!");

		}

		String degreeParameter = this.mReplicationParameters
				.get(ReplicationParameterHelper.PARAMETER_MIN_INDEX_DEGREE_OF_REPLICATION);
		if (this.mReplicationParameters.size() > ReplicationParameterHelper.PARAMETER_MAX_INDEX_DEGREE_OF_REPLICATION) {

			degreeParameter = this.mReplicationParameters
					.get(ReplicationParameterHelper.PARAMETER_MAX_INDEX_DEGREE_OF_REPLICATION);

		}

		return ModificationHelper.determineDegreeOfModification(degreeParameter,
				ReplicationParameterHelper.MIN_DEGREE_OF_REPLICATION);

	}

	@Override
	public IPair<Optional<ILogicalOperator>, Optional<ILogicalOperator>> determineStartAndEndPoints(
			Collection<ILogicalQueryPart> queryParts) throws QueryPartModificationException {

		// Preconditions
		if (this.mReplicationParameters.size() < ReplicationParameterHelper.PARAMETER_MAX_INDEX_DEGREE_OF_REPLICATION
				+ 1) {

			Optional<ILogicalOperator> dummy = Optional.absent();
			return new Pair<Optional<ILogicalOperator>, Optional<ILogicalOperator>>(dummy, dummy);

		}

		String startAndEndPointParameter = this.mReplicationParameters
				.get(ReplicationParameterHelper.PARAMETER_INDEX_START_AND_END_POINT_IDS);

		IPair<ILogicalOperator, Optional<ILogicalOperator>> startAndEnd = ModificationHelper
				.determineStartAndEndPoints(queryParts, startAndEndPointParameter);
		return new Pair<Optional<ILogicalOperator>, Optional<ILogicalOperator>>(Optional.of(startAndEnd.getE1()),
				startAndEnd.getE2());

	}

}