package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;

public class MemStoreWritePO<R extends IStreamObject<?>> extends AbstractSink<R> {

	final IDataDictionaryWritable dd;
	final Resource store;
	final boolean clear;

	public MemStoreWritePO(IDataDictionary dd, Resource store, boolean clear) {
		this.dd = (IDataDictionaryWritable) dd;
		this.store = store;
		this.clear = clear;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		 List<IStreamObject<?>> s = dd.getOrCreateStore(store);
		if (clear){
			s.clear();
		}
	}
	
	@Override
	protected void process_next(R object, int port) {
		dd.getStore(store).add(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// ignore
	}

}
