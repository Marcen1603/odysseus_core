package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.StoreRegistry;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateKVStoreCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 9178099993534874528L;
	
	private static final String optionsNameCreateOnlyIfNotExists ="createOnlyIfNotExists";
	
	private String name;
	private String type;
	private OptionMap options;

	public CreateKVStoreCommand(String name, String type, OptionMap options, ISession caller) {
		super(caller);
		this.name = name;
		this.type = type;
		this.options = options;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		if (dd.containsStore(name, getCaller())){
			if(options.getBoolean(optionsNameCreateOnlyIfNotExists, true)) {
				// store already exists and it is NOT wanted to use the existing store
				throw new QueryParseException("Store with name "+name+" is already defined! Drop first.");
			} else {
				// store already exists and it is wanted to use the existing store
				return;
			}
		}
		// These stores are always with key string and object value
		@SuppressWarnings("unchecked")
		IStore<String, Object> store = (IStore<String, Object>) StoreRegistry.createStore(type, options);
		if (store == null){
			throw new QueryParseException("Coult not create store of type "+type+" with options "+options+" Available stores are "+StoreRegistry.getKeys());
		}
		dd.addStore(name, store, getCaller());
	}

}
