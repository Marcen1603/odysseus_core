package de.uniol.inf.is.odysseus.core.server.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

@SuppressWarnings("rawtypes")
public class StoreRegistry {

	static final Logger LOG = LoggerFactory.getLogger(StoreRegistry.class);

	static final Map<String, IStore<?,?>> stores = new HashMap<>();

	public void bind(IStore store){
		if (!stores.containsKey(store.getType())){
			stores.put(store.getType().toLowerCase(), store);
		}else{
			LOG.warn("Store with "+store.getType()+" already registered. Ignoring "+store);
		}
	}

	public void unbind(IStore store){
		stores.remove(store.getType().toLowerCase());
	}

	public StoreRegistry() {
	}

	public static IStore<?,?> createStore(
			String storeType, OptionMap storeOptions) throws StoreException{

		IStore<?,?> store = stores.get(storeType.toLowerCase());
		if (store != null){
			return store.newInstance(storeOptions);
		}

		return null;
	}

	public static Set<String> getKeys() {
		return stores.keySet();
	}



}
