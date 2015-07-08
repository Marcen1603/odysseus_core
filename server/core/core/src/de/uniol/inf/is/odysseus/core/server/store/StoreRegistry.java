package de.uniol.inf.is.odysseus.core.server.store;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

@SuppressWarnings("rawtypes")
public class StoreRegistry {

	// TODO: Make generic with services
	
	static final Map<String, IStore<?,?>> stores = new HashMap<>();
	static{
		stores.put(FileStore.TYPE, new FileStore());
		stores.put(MemoryStore.TYPE, new MemoryStore());
	}
	
	public StoreRegistry() {		
	}
	
	public static IStore<?,?> createStore(
			String storeType, OptionMap storeOptions) throws StoreException{
		
		IStore<?,?> store = stores.get(storeType);
		if (store != null){
			return store.newInstance(storeOptions);
		}
		
		return null;
	}

	
	
}
