package de.uniol.inf.is.odysseus.metadata.base;

public interface IMetadataMergeFunction<T> {

	public T mergeMetadata(T left, T right);
	
	public void init();

	public IMetadataMergeFunction<T> clone();
}
