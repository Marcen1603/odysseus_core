package de.uniol.inf.is.odysseus.base.planmanagement;


public interface IReoptimizeHandler<ReoptimizableListenerType> {
	public void addReoptimizeListener(
			ReoptimizableListenerType reoptimizationListener);

	public void removeReoptimizeListener(
			ReoptimizableListenerType reoptimizationListener);
}
