package de.uniol.inf.is.odysseus.metadata;

public interface IMetadataMergeFunction<T> {

	public T mergeMetadata(T left, T right);
	
	public void init();

	public IMetadataMergeFunction<T> clone();
}
