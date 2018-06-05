package de.uniol.inf.is.odysseus.net.querydistribute.modify.replication;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;

/**
 * A replication info bundle is an ADT for objects and informations relevant for
 * replication.
 * 
 * @author Michael Brand
 */
public class ReplicationInfoBundle {

	/**
	 * The degree of replication.
	 */
	private int mDegreeOfReplication;

	/**
	 * The operator marking the start of replication within the original list of
	 * operators, if given.
	 */
	private Optional<ILogicalOperator> mOriginalStartOperator;

	/**
	 * The operator marking the end of replication within the original list of
	 * operators, if given.
	 */
	private Optional<ILogicalOperator> mOriginalEndOperator;

	/**
	 * All query parts to build replicates given from the original list of query
	 * parts.
	 */
	private ImmutableCollection<ILogicalQueryPart> mOriginalRelevantParts;

	/**
	 * All query parts not to build replicates given from the original list of
	 * query parts.
	 */
	private ImmutableCollection<ILogicalQueryPart> mOriginalIrrelevantParts;

	/**
	 * The mapping of copied query parts to their originals.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> mCopyMap;

	/**
	 * All inserted merge operators.
	 */
	private Collection<ILogicalOperator> mMergeOperators = Lists.newArrayList();

	/**
	 * Sets the degree of replication.
	 * 
	 * @param degree
	 *            The degree of replication.
	 */
	public void setDegreeOfReplication(int degree) {

		this.mDegreeOfReplication = degree;

	}

	/**
	 * Gets the degree of replication.
	 * 
	 * @return The degree of replication.
	 */
	public int getDegreeOfReplication() {

		return this.mDegreeOfReplication;

	}

	/**
	 * The operator marking the start of replication.
	 * 
	 * @param operator
	 *            An operator within the original list of operators or
	 *            {@link Optional#absent()}, if no start is given.
	 */
	public void setOriginStartOperator(Optional<ILogicalOperator> operator) {

		this.mOriginalStartOperator = operator;

	}

	/**
	 * The operator marking the start of replication.
	 * 
	 * @return An operator within the original list of operators or
	 *         {@link Optional#absent()}, if no start is given.
	 */
	public Optional<ILogicalOperator> getOriginStartOperator() {

		return this.mOriginalStartOperator;

	}

	/**
	 * The operator marking the end of replication.
	 * 
	 * @param operator
	 *            An operator within the original list of operators or
	 *            {@link Optional#absent()}, if no end is given.
	 */
	public void setOriginEndOperator(Optional<ILogicalOperator> operator) {

		this.mOriginalEndOperator = operator;

	}

	/**
	 * The operator marking the end of replication.
	 * 
	 * @return An operator within the original list of operators or
	 *         {@link Optional#absent()}, if no end is given.
	 */
	public Optional<ILogicalOperator> getOriginEndOperator() {

		return this.mOriginalEndOperator;

	}

	/**
	 * All query parts to build replicates.
	 * 
	 * @param queryParts
	 *            A list of query parts built from the original list of query
	 *            parts.
	 */
	public void setOriginalRelevantParts(
			Collection<ILogicalQueryPart> queryParts) {

		this.mOriginalRelevantParts = ImmutableList.copyOf(queryParts);

	}

	/**
	 * All query parts to build replicates.
	 * 
	 * @return A list of query parts built from the original list of query
	 *         parts.
	 */
	public ImmutableCollection<ILogicalQueryPart> getOriginalRelevantParts() {

		return this.mOriginalRelevantParts;

	}

	/**
	 * All query parts not to build replicates.
	 * 
	 * @param queryParts
	 *            A list of query parts built from the original list of query
	 *            parts.
	 */
	public void setOriginalIrrelevantParts(
			Collection<ILogicalQueryPart> queryParts) {

		this.mOriginalIrrelevantParts = ImmutableList.copyOf(queryParts);

	}

	/**
	 * All query parts not to build replicates.
	 * 
	 * @return A list of query parts built from the original list of query
	 *         parts.
	 */
	public ImmutableCollection<ILogicalQueryPart> getOriginalIrrelevantParts() {

		return this.mOriginalIrrelevantParts;

	}

	/**
	 * The mapping of copied query parts to their originals.
	 * 
	 * @param copyMap
	 *            The mapping of copied query parts to their originals.
	 */
	public void setCopyMap(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap) {

		this.mCopyMap = copyMap;

	}

	/**
	 * The mapping of copied query parts to their originals.
	 * 
	 * @return The mapping of copied query parts to their originals.
	 */
	public Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> getCopyMap() {

		return this.mCopyMap;

	}

	/**
	 * Adds a new merge operator.
	 * 
	 * @param operator
	 *            The merge operators.
	 */
	public void addMergeOperator(ILogicalOperator operator) {

		this.mMergeOperators.add(operator);

	}

	/**
	 * The inserted merge operators.
	 * 
	 * @return All inserted merge operators.
	 */
	public Collection<ILogicalOperator> getMergeOperators() {

		return this.mMergeOperators;

	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Degree of replication: " + this.mDegreeOfReplication);
		buffer.append("\nOrigin operator marking the start of replication: "
				+ this.mOriginalStartOperator);
		buffer.append("\nOrigin operator marking the end of replication: "
				+ this.mOriginalEndOperator);
		buffer.append("\nQuery parts to replicate: "
				+ this.mOriginalRelevantParts);
		buffer.append("\nQuery parts not to replicate: "
				+ this.mOriginalIrrelevantParts);
		buffer.append("\nMapping of copied query parts to their originals: "
				+ this.mCopyMap);
		buffer.append("\nAll inserted merge operators: " + this.mMergeOperators);
		return buffer.toString();

	}

}