package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

/**
 * Cost of an object.
 * 
 * @author Tobias Witt
 *
 * @param <T> Object type, which was rated.
 */
public interface ICost<T> extends Comparable<ICost<T>> {
	public int getScore();
	public void setScore(int score);
}
