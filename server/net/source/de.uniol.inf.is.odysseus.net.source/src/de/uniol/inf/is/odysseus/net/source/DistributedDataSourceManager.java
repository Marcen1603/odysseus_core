package de.uniol.inf.is.odysseus.net.source;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class DistributedDataSourceManager implements IDistributedDataListener, IDatadictionaryProviderListener, IDataDictionaryListener {

	private static final String DATA_SOURCE_DISTRIBUTION_NAME = "net.data.source";
	
	private static IDistributedDataManager dataManager;

	// called by OSGi-DS
	public void bindDistributedDataManager(IDistributedDataManager serv) {
		dataManager = serv;
		
		dataManager.addListener(this);
	}

	// called by OSGi-DS
	public void unbindDistributedDataManager(IDistributedDataManager serv) {
		if (dataManager == serv) {
			dataManager.removeListener(this);
			
			dataManager = null;
		}
	}
	
	@Override
	public void distributedDataManagerStarted(IDistributedDataManager sender) {
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		DataDictionaryProvider.subscribe(tenant, this);
		
		IDataDictionary dd = DataDictionaryProvider.getDataDictionary(tenant);
		if (dd != null) {
			newDatadictionary(dd);
		}
	}

	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		if( addedData.getName().equals(DATA_SOURCE_DISTRIBUTION_NAME)) {
			
		}
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData) {
		
	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		if( removedData.getName().equals(DATA_SOURCE_DISTRIBUTION_NAME)) {
			
		}
	}

	@Override
	public void distributedDataManagerStopped(IDistributedDataManager sender) {
		DataDictionaryProvider.unsubscribe(this);
	}

	@Override
	public void newDatadictionary(IDataDictionary dd) {
		dd.addListener(this);
	}

	@Override
	public void removedDatadictionary(IDataDictionary dd) {
		dd.removeListener(this);
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView, ISession session) {
		
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView, ISession session) {
		
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		
	}
}
