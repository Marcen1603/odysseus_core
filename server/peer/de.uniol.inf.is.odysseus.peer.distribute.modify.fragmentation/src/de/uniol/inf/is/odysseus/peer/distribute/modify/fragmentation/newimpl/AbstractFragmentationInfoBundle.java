package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * An fragmentation info bundle is an ADT for objects and informations relevant
 * for fragmentation.
 * 
 * @author Michael Brand
 */
public abstract class AbstractFragmentationInfoBundle {

	/**
	 * The degree of fragmentation.
	 */
	private int degreeOfFragmentation;

	/**
	 * The operator marking the start of fragmentation within the original list
	 * of operators.
	 */
	private ILogicalOperator originalStartOperator;

	/**
	 * The copies of {@link #originalStartOperator}.
	 */
	private ImmutableCollection<ILogicalOperator> copiedStartOperators;

	/**
	 * The copies of {@link #originalEndOperator}.
	 */
	private ImmutableCollection<ILogicalOperator> copiedEndOperators;

	/**
	 * The operator marking the end of fragmentation within the original list of
	 * operators, if given.
	 */
	private Optional<ILogicalOperator> originalEndOperator;

	/**
	 * All query parts to build fragments given from the original list of query
	 * parts.
	 */
	private ImmutableCollection<ILogicalQueryPart> originalRelevantParts;

	/**
	 * All query parts not to build fragments given from the original list of
	 * query parts.
	 */
	private ImmutableCollection<ILogicalQueryPart> originalIrrelevantParts;

	/**
	 * The mapping of copied query parts to their originals.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap;

	/**
	 * Sets the degree of fragmentation.
	 * 
	 * @param degree
	 *            The degree of fragmentation.
	 */
	public void setDegreeOfFragmentation(int degree) {

		this.degreeOfFragmentation = degree;

	}

	/**
	 * Gets the degree of fragmentation.
	 * 
	 * @return The degree of fragmentation.
	 */
	public int getDegreeOfFragmentation() {

		return this.degreeOfFragmentation;

	}

	/**
	 * The operator marking the start of fragmentation.
	 * 
	 * @param operator
	 *            An operator within the original list of operators.
	 */
	public void setOriginStartOperator(ILogicalOperator operator) {

		this.originalStartOperator = operator;

	}

	/**
	 * The operator marking the start of fragmentation.
	 * 
	 * @return An operator within the original list of operators.
	 */
	public ILogicalOperator getOriginStartOperator() {

		return this.originalStartOperator;

	}

	/**
	 * The copies of {@link #getOriginStartOperator()}.
	 * 
	 * @param operators
	 *            A list of operators being copies of the original start
	 *            operator.
	 */
	public void setCopiedStartOperators(Collection<ILogicalOperator> operators) {

		this.copiedStartOperators = ImmutableList.copyOf(operators);

	}

	/**
	 * The copies of {@link #getOriginStartOperator()}.
	 * 
	 * @return A list of operators being copies of the original start operator.
	 */
	public ImmutableCollection<ILogicalOperator> getCopiedStartOperators() {

		return this.copiedStartOperators;

	}

	/**
	 * The operator marking the end of fragmentation.
	 * 
	 * @param operator
	 *            An operator within the original list of operators or
	 *            {@link Optional#absent()}, if no end is given.
	 */
	public void setOriginEndOperator(Optional<ILogicalOperator> operator) {

		this.originalEndOperator = operator;

	}

	/**
	 * The operator marking the end of fragmentation.
	 * 
	 * @return An operator within the original list of operators or
	 *         {@link Optional#absent()}, if no end is given.
	 */
	public Optional<ILogicalOperator> getOriginEndOperator() {

		return this.originalEndOperator;

	}

	/**
	 * The copies of {@link #getOriginEndOperator()}.
	 * 
	 * @param operators
	 *            A list of operators being copies of the original end operator.
	 */
	public void setCopiedEndOperators(Collection<ILogicalOperator> operators) {

		this.copiedEndOperators = ImmutableList.copyOf(operators);

	}

	/**
	 * The copies of {@link #getOriginEndOperator()}.
	 * 
	 * @return A list of operators being copies of the original end operator.
	 */
	public ImmutableCollection<ILogicalOperator> getCopiedEndOperators() {

		return this.copiedEndOperators;

	}

	/**
	 * All query parts to build fragments.
	 * 
	 * @param queryParts
	 *            A list of query parts built from the original list of query
	 *            parts.
	 */
	public void setOriginalRelevantParts(
			Collection<ILogicalQueryPart> queryParts) {

		this.originalRelevantParts = ImmutableList.copyOf(queryParts);

	}

	/**
	 * All query parts to build fragments.
	 * 
	 * @return A list of query parts built from the original list of query
	 *         parts.
	 */
	public ImmutableCollection<ILogicalQueryPart> getOriginalRelevantParts() {

		return this.originalRelevantParts;

	}

	/**
	 * All query parts not to build fragments.
	 * 
	 * @param queryParts
	 *            A list of query parts built from the original list of query
	 *            parts.
	 */
	public void setOriginalIrrelevantParts(
			Collection<ILogicalQueryPart> queryParts) {

		this.originalIrrelevantParts = ImmutableList.copyOf(queryParts);

	}

	/**
	 * All query parts not to build fragments.
	 * 
	 * @return A list of query parts built from the original list of query
	 *         parts.
	 */
	public ImmutableCollection<ILogicalQueryPart> getOriginalIrrelevantParts() {

		return this.originalIrrelevantParts;

	}

	/**
	 * The mapping of copied query parts to their originals.
	 * 
	 * @param copyMap
	 *            The mapping of copied query parts to their originals.
	 */
	public void setCopyMap(
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap) {

		this.copyMap = copyMap;

	}

	/**
	 * The mapping of copied query parts to their originals.
	 * 
	 * @return The mapping of copied query parts to their originals.
	 */
	public Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> getCopyMap() {

		return this.copyMap;

	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Degree of fragmentation: " + this.degreeOfFragmentation);
		buffer.append("\nOrigin operator marking the start of fragmentation: "
				+ this.originalStartOperator);
		buffer.append("\nCopied operators marking the start of fragmentation: "
				+ this.copiedStartOperators);
		buffer.append("\nOrigin operator marking the end of fragmentation: "
				+ this.originalEndOperator);
		buffer.append("\nCopied operators marking the end of fragmentation: "
				+ this.copiedEndOperators);
		buffer.append("\nQuery parts to form fragments: "
				+ this.originalRelevantParts);
		buffer.append("\nQuery parts not to form fragments: "
				+ this.originalIrrelevantParts);
		buffer.append("\nMapping of copied query parts to their originals: "
				+ this.copyMap);
		return buffer.toString();

	}

}