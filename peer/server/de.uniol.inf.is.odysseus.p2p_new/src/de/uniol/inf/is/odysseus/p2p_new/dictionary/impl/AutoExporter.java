package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class AutoExporter implements IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(AutoExporter.class);
	private final IP2PDictionary dictionary;
	
	AutoExporter( IP2PDictionary dictionary ) {
		Preconditions.checkNotNull(dictionary, "P2PDictionary must not be null!");
		
		this.dictionary = dictionary;
	}
	
	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		if( !dictionary.isExported(name) && !dictionary.isImported(name)) {
			try {
				dictionary.exportSource(name);
			} catch (PeerException e) {
				LOG.error("Could not automatically export {}", name, e);
			}
		}
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		if( dictionary.isExported(name)) {
			dictionary.removeSourceExport(name);
		}
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
	}

}
