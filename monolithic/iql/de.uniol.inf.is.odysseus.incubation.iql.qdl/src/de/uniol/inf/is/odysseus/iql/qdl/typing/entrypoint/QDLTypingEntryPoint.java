package de.uniol.inf.is.odysseus.iql.qdl.typing.entrypoint;

import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.iql.basic.typing.OperatorsObservable;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.AbstractIQLTypingEntryPoint;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.typing.builder.IQDLTypeBuilder;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

@Singleton
public class QDLTypingEntryPoint extends AbstractIQLTypingEntryPoint<IQDLTypeBuilder> implements IDatadictionaryProviderListener, OperatorsObservable.Listener, IDataDictionaryListener {

	@Inject
	public QDLTypingEntryPoint(IQDLTypeBuilder creator) {
		super(creator);	
		initOperators();
		initSources();
	}
	

	private void refresProjects() {
//		final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
//				.getProjects();
//		if (projects != null && projects.length > 0) {
//			Job job = new Job("Build projects") {
//				@Override
//				protected IStatus run(IProgressMonitor monitor) {
//					for (IProject project : projects) {
//						try {
//							project.build(IncrementalProjectBuilder.CLEAN_BUILD, null);
//						} catch (CoreException e) {
//						}
//					}
//					return Status.OK_STATUS;
//				}
//			};
//			job.schedule(); 
//		}      
//			
		
	}
	
	

	private void initSources() {
		ITenant tenant = UserManagementProvider.instance.getDefaultTenant();
		DataDictionaryProvider.instance.subscribe(tenant, this);	
		IDataDictionary dataDictionary = DataDictionaryProvider.instance.getDataDictionary(tenant);
		dataDictionary.addListener(this);
		OperatorsObservable.addListener(this);
						
		for (Entry<Resource, ILogicalPlan> entry : dataDictionary.getStreamsAndViews(OdysseusRCPPlugIn.getActiveSession())) {
			builder.createSource(entry.getValue().getRoot().getName(), entry.getValue().getRoot());
		}
	}
	
	private void initOperators() {
		
		for (IOperatorBuilder operatorBuilder : QDLServiceBinding.getOperatorBuilderFactory().getOperatorBuilder()) {
			builder.createOperator(operatorBuilder);
		}		
		
	}
	
	@Override
	public void onNewOperator(IOperatorBuilder operatorBuilder) {
		builder.createOperator(operatorBuilder);
		refresProjects();
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
	public void addedViewDefinition(IDataDictionary sender, String name,ILogicalPlan op, boolean isView, ISession session) {
		builder.createSource(name, op.getRoot());
		refresProjects();
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name,ILogicalPlan op, boolean isView, ISession session) {
		builder.removeSource(name, op.getRoot());
		refresProjects();
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		
	}

}
