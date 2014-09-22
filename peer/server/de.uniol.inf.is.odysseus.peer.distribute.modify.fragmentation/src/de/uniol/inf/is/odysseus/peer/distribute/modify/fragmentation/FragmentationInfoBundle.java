package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * An fragmentation info bundle is an ADT for objects and informations relevant
 * for fragmentation.
 * 
 * @author Michael Brand
 */
public class FragmentationInfoBundle {

	/**
	 * The degree of fragmentation.
	 */
	private int mDegreeOfFragmentation;

	/**
	 * The operator marking the start of fragmentation within the original list
	 * of operators.
	 */
	private ILogicalOperator mOriginalStartOperator;

	/**
	 * The operator marking the end of fragmentation within the original list of
	 * operators, if given.
	 */
	private Optional<ILogicalOperator> mOriginalEndOperator;

	/**
	 * All query parts to build fragments given from the original list of query
	 * parts.
	 */
	private ImmutableCollection<ILogicalQueryPart> mOriginalRelevantParts;

	/**
	 * All query parts not to build fragments given from the original list of
	 * query parts.
	 */
	private ImmutableCollection<ILogicalQueryPart> mOriginalIrrelevantParts;

	/**
	 * The mapping of copied query parts to their originals.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> mCopyMap;

	/**
	 * All inserted fragment operators.
	 */
	private Collection<ILogicalOperator> mFragmentOperators = Lists.newArrayList();

	/**
	 * All inserted reunion operators.
	 */
	private Collection<ILogicalOperator> mReunionOperators = Lists.newArrayList();

	/**
	 * Sets the degree of fragmentation.
	 * 
	 * @param degree
	 *            The degree of fragmentation.
	 */
	public void setDegreeOfFragmentation(int degree) {

		this.mDegreeOfFragmentation = degree;

	}

	/**
	 * Gets the degree of fragmentation.
	 * 
	 * @return The degree of fragmentation.
	 */
	public int getDegreeOfFragmentation() {

		return this.mDegreeOfFragmentation;

	}

	/**
	 * The operator marking the start of fragmentation.
	 * 
	 * @param operator
	 *            An operator within the original list of operators.
	 */
	public void setOriginStartOperator(ILogicalOperator operator) {

		this.mOriginalStartOperator = operator;

	}

	/**
	 * The operator marking the start of fragmentation.
	 * 
	 * @return An operator within the original list of operators.
	 */
	public ILogicalOperator getOriginStartOperator() {

		return this.mOriginalStartOperator;

	}

	/**
	 * The operator marking the end of fragmentation.
	 * 
	 * @param operator
	 *            An operator within the original list of operators or
	 *            {@link Optional#absent()}, if no end is given.
	 */
	public void setOriginEndOperator(Optional<ILogicalOperator> operator) {

		this.mOriginalEndOperator = operator;

	}

	/**
	 * The operator marking the end of fragmentation.
	 * 
	 * @return An operator within the original list of operators or
	 *         {@link Optional#absent()}, if no end is given.
	 */
	public Optional<ILogicalOperator> getOriginEndOperator() {

		return this.mOriginalEndOperator;

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

		this.mOriginalRelevantParts = ImmutableList.copyOf(queryParts);

	}

	/**
	 * All query parts to build fragments.
	 * 
	 * @return A list of query parts built from the original list of query
	 *         parts.
	 */
	public ImmutableCollection<ILogicalQueryPart> getOriginalRelevantParts() {

		return this.mOriginalRelevantParts;

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

		this.mOriginalIrrelevantParts = ImmutableList.copyOf(queryParts);

	}

	/**
	 * All query parts not to build fragments.
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
	 * Adds a new fragment operator.
	 * 
	 * @param operator
	 *           The fragment operators.
	 */
	public void addFragmentOperator(
			ILogicalOperator operator) {

		this.mFragmentOperators.add(operator);

	}

	/**
	 * The inserted fragment operators.
	 * 
	 * @return All inserted fragment operators.
	 */
	public Collection<ILogicalOperator> getFragmentOperators() {

		return this.mFragmentOperators;

	}

	/**
	 * Adds a new reunion operator.
	 * 
	 * @param operator
	 *           The reunion operators.
	 */
	public void addReunionOperator(
			ILogicalOperator operator) {

		this.mReunionOperators.add(operator);

	}

	/**
	 * The inserted reunion operators.
	 * 
	 * @return All inserted fragment operators.
	 */
	public Collection<ILogicalOperator> getReunionOperators() {

		return this.mReunionOperators;

	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Degree of fragmentation: " + this.mDegreeOfFragmentation);
		buffer.append("\nOrigin operator marking the start of fragmentation: "
				+ this.mOriginalStartOperator);
		buffer.append("\nOrigin operator marking the end of fragmentation: "
				+ this.mOriginalEndOperator);
		buffer.append("\nQuery parts to form fragments: "
				+ this.mOriginalRelevantParts);
		buffer.append("\nQuery parts not to form fragments: "
				+ this.mOriginalIrrelevantParts);
		buffer.append("\nMapping of copied query parts to their originals: "
				+ this.mCopyMap);
		buffer.append("\nAll inserted fragment operators: "
				+ this.mFragmentOperators);
		buffer.append("\nAll inserted reunion operators: "
				+ this.mReunionOperators);
		return buffer.toString();

	}

}