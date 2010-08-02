package de.uniol.inf.is.odysseus.priority;

public interface IPostPrioritizationArea<T> {
	
	public void insert(T element);
	public void purgeElements(T element);
	public boolean hasPartner(T element);

}
