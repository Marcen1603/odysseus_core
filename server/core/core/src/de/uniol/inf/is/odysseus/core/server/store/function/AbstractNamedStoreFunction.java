package de.uniol.inf.is.odysseus.core.server.store.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.ExecutorBinder;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public abstract class AbstractNamedStoreFunction<T> extends AbstractFunction<T> {

	private static final long serialVersionUID = 8618484144361039066L;
	private IStore<String, Object> store;

	public AbstractNamedStoreFunction(String symbol, int arity, SDFDatatype[][] acceptedTypes, SDFDatatype returnType,
			boolean optimizeConstantParameter) {
		super(symbol, arity, acceptedTypes, returnType, optimizeConstantParameter);
	}

	protected IStore<String, Object> getStore(String storeKey) {
		if (store == null) {
			IServerExecutor exec = ExecutorBinder.getExecutor();
			ISession session = getSessions().get(0);
			IDataDictionary dd = exec.getDataDictionary(getSessions().get(0));
			store = dd.getStore(storeKey, session);
		}
		return store;
	}

}
