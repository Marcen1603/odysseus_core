package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.base;


public interface ICost<T> extends Comparable<ICost<T>> {
	public int getScore();
	public void setScore(int score);
}
