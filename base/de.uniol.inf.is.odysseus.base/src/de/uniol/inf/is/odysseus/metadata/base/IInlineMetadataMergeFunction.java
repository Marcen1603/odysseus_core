package de.uniol.inf.is.odysseus.metadata.base;

public interface IInlineMetadataMergeFunction<T> {
	public void mergeInto(T result, T inLeft, T inRight);
}
