package de.uniol.inf.is.odysseus.net.source;

import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Resource;
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
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class DistributedDataSourceManager implements IDistributedDataListener, IDatadictionaryProviderListener, IDataDictionaryListener {

	private static final String DATA_SOURCE_DISTRIBUTION_NAME = "net.data.source";

	private static IDistributedDataManager dataManager;
	private static IPQLGenerator pqlGenerator;

	private static ISession currentSession;

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

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
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
		if (addedData.getName().equals(DATA_SOURCE_DISTRIBUTION_NAME)) {

		}
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData) {

	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		if (removedData.getName().equals(DATA_SOURCE_DISTRIBUTION_NAME)) {

		}
	}

	@Override
	public void distributedDataManagerStopped(IDistributedDataManager sender) {
		DataDictionaryProvider.unsubscribe(this);
	}

	@Override
	public void newDatadictionary(IDataDictionary dd) {
		dd.addListener(this);
		
		Set<Entry<Resource,ILogicalOperator>> streamsAndViews = dd.getStreamsAndViews(getActiveSession());
		for( Entry<Resource,ILogicalOperator> streamOrView : streamsAndViews ) {
			String name = streamOrView.getKey().getResourceName();
			String username = streamOrView.getKey().getUser();
			
			ILogicalOperator operator = streamOrView.getValue();
			
			createDistributedData(name, operator, username);
		}
	}

	@Override
	public void removedDatadictionary(IDataDictionary dd) {
		dd.removeListener(this);
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView, ISession session) {
		createDistributedData(name, op, session.getUser().getName());
	}

	private void createDistributedData(String name, ILogicalOperator op, String username) {
		String realSourceName = removeUserFromName(name);
		
		String pqlStatement = pqlGenerator.generatePQLStatement(op);
		
		System.err.println("New source defined! Name is " + realSourceName);
		System.err.println("User is " + username);
		System.err.println("PQL: " + pqlStatement);
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView, ISession session) {

	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {

	}
	
	private static String removeUserFromName(String streamName) {
		int pos = streamName.indexOf(".");
		if (pos >= 0) {
			return streamName.substring(pos + 1);
		}
		return streamName;
	}
	
	public static ISession getActiveSession() {
		if( currentSession == null || !currentSession.isValid()) {
			currentSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return currentSession;
	}
}
