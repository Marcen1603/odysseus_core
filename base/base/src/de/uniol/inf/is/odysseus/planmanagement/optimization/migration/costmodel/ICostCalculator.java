package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import java.util.Collection;
import java.util.List;

/**
 * Calculates cost of an specific object type. ICostCalculator can pick the best
 * <code>num</code> alternatives out of a collection.
 * 
 * @author Tobias Witt
 * 
 * @param <T>
 *            Object type, which is rated.
 */
public interface ICostCalculator<T> {
	/**
	 * Calculates the {@link ICost} of <code>candidate</code>.
	 * 
	 * @param candidate
	 * @return Cost.
	 */
	public ICost<T> calculateCost(T candidate);

	/**
	 * Picks the best alternative out of the <code>candidates</code>.
	 * 
	 * @param candidates
	 *            {@link Collection} of candidates.
	 * @return Best candidate.
	 */
	public T pickBest(Collection<T> candidates);

	/**
	 * Picks the <code>num</code> best alternatives out of the
	 * <code>candidates</code>.
	 * 
	 * @param candidates
	 *            {@link Collection} of candidates.
	 * @param num
	 *            Number of alternatives to choose.
	 * @return {@link List} of best alternatives.
	 */
	public List<T> pickBest(Collection<T> candidates, int num);
}
