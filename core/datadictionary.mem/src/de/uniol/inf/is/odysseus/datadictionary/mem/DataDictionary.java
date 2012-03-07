package de.uniol.inf.is.odysseus.datadictionary.mem;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DataDictionary extends AbstractDataDictionary {

	@Override
	protected IStore<String, ILogicalOperator> createStreamDefinitionsStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, IUser> createViewOrStreamFromUserStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, ILogicalOperator> createViewDefinitionsStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, SDFSchema> createEntityMapStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, IUser> createEntityFromUserStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, String> createSourceTypeMapStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, SDFDatatype> createDatatypesStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<Integer, ILogicalQuery> createSavedQueriesStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<Integer, IUser> createSavedQueriesForUserStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<Integer, String> createSavedQueriesBuildParameterNameStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, ILogicalOperator> createSinkDefinitionsStore() {
		return newMemoryStore();
	}

	@Override
	protected IStore<String, IUser> createSinkFromUserStore() {
		return newMemoryStore();
	}
	
	private static <T extends Comparable<?>,U> MemoryStore<T,U> newMemoryStore(){
		return new MemoryStore<T, U>();
	}
}
