package de.uniol.inf.is.odysseus.iql.qdl.typing;

import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

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
	

	private void refresProjects() {
		final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		if (projects != null && projects.length > 0) {
			Job job = new Job("Build projects") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					for (IProject project : projects) {
						try {
							project.build(IncrementalProjectBuilder.CLEAN_BUILD, null);
						} catch (CoreException e) {
						}
					}
					return Status.OK_STATUS;
				}
			};
			job.schedule(); 
		}      
			
		
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
	public void addedViewDefinition(IDataDictionary sender, String name,ILogicalOperator op) {
		builder.addSource(op);
		refresProjects();
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name,ILogicalOperator op) {
		builder.removeSource(op);
		refresProjects();
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		
	}

}
