package de.uniol.inf.is.odysseus.iql.qdl.typing;

import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.iql.basic.typing.OperatorsObservable;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.AbstractIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

@Singleton
public class QDLTypingEntryPoint extends AbstractIQLTypingEntryPoint<QDLTypeBuilder> implements IDatadictionaryProviderListener, OperatorsObservable.Listener, IDataDictionaryListener {

	@Inject
	public QDLTypingEntryPoint(QDLTypeBuilder creator) {
		super(creator);	
		initOperators();
		initSources();		
	}
	

	
	

	private void initSources() {
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		DataDictionaryProvider.subscribe(tenant, this);	
		IDataDictionary dataDictionary = DataDictionaryProvider.getDataDictionary(tenant);
		dataDictionary.addListener(this);
		OperatorsObservable.addListener(this);
				
		for (Entry<Resource, ILogicalOperator> entry : dataDictionary.getStreamsAndViews(OdysseusRCPPlugIn.getActiveSession())) {
			builder.addSource(entry.getValue());
		}
	}
	
	private void initOperators() {
		
		for (IOperatorBuilder operatorBuilder : QDLServiceBinding.getOperatorBuilderFactory().getOperatorBuilder()) {
			builder.addOperator(operatorBuilder);
		}		
		
	}
	
	@Override
	public void onNewOperator(IOperatorBuilder operatorBuilder) {
		builder.addOperator(operatorBuilder);
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
	public void addedViewDefinition(IDataDictionary sender, String name,ILogicalOperator op) {
		builder.addSource(op);
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name,ILogicalOperator op) {
		builder.removeSource(op);
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		
	}

}
