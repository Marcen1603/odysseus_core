package de.uniol.inf.is.odysseus.scars.operator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;

public class MetadataObjectRelationalCreationPO<M extends IProbability> extends MetadataCreationPO<M, MVRelationalTuple<M>> {

	public MetadataObjectRelationalCreationPO(Class<M> type) {
		super(type);
	}
	
	public MetadataObjectRelationalCreationPO(MetadataObjectRelationalCreationPO<M> po) {
		super(po);
	}
	
	public void process_next(MVRelationalTuple<M> object, int port) {
		try {
			assignMetadata(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.transfer(object);
	};
	
	@SuppressWarnings("unchecked")
	private void assignMetadata(Object tuple ){
		try {
			if( tuple instanceof IMetaAttributeContainer ) {
				((IMetaAttributeContainer<M>)tuple).setMetadata(getType().newInstance());
			}
			if( tuple instanceof MVRelationalTuple<?>) {
				MVRelationalTuple<M> t = (MVRelationalTuple<M>) tuple;
				for( int i = 0; i < t.getAttributeCount(); i++ ) {
					assignMetadata(t.getAttribute(i));
				}
			}
		} catch( IllegalAccessException ex ) {
			ex.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
}
