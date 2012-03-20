package de.uniol.inf.is.odysseus.fusion.store.context;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class FusionContextStore<T> {
	
	private HashMap<Integer, RelationalTuple<? extends IMetaAttribute>> contextStore;
	private SDFSchema storeSchema;
	
	public FusionContextStore(SDFSchema schema) {
		storeSchema = schema;
		contextStore = new HashMap<Integer, RelationalTuple<? extends IMetaAttribute>>();
	}
	
	public void insertValue(RelationalTuple<? extends IMetaAttribute> tuple){
		contextStore.put((Integer)tuple.getAttribute(0), tuple);
	}
	
	public SDFSchema getStoreSchema(){
		return storeSchema;
	}

}
