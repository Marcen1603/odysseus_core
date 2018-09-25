package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

public class MemStoreSourcePO<T extends IStreamObject<?>> extends AbstractIterableSource<T> {

	IDataDictionary dd;
	Resource store;
	private List<IStreamObject<?>> storeContent;
	int pos = 0;

	public MemStoreSourcePO(IDataDictionary dd, Resource store) {
		this.dd = dd;
		this.store = store;
	}

	@Override
	public boolean hasNext() {
		if (pos == storeContent.size()){
			propagateDone();
			return false;
		}
		return pos<storeContent.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void transferNext() {
		transfer((T) storeContent.get(pos++));
	}

	@Override
	protected void process_open() throws OpenFailedException {
		storeContent = dd.getStore(store);
		pos = 0;
	}

	@Override
	protected void process_start() throws StartFailedException {

	}

}
