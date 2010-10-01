package de.uniol.inf.is.odysseus.metadata;

public interface IInlineMetadataMergeFunction<T> {
	public void mergeInto(T result, T inLeft, T inRight);

	public IInlineMetadataMergeFunction<? super T> clone() ;
}
