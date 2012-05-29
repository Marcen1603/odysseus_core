package de.uniol.inf.is.odysseus.core.server.metadata;

public class UseLeftInputMetadata<T> implements IMetadataMergeFunction<T> {

	
	@Override
	public T mergeMetadata(T left, T right) {
		return left;
	}

	@Override
	public void init() {
	}

	public IMetadataMergeFunction<T> clone(){
		return this;
	}


}
