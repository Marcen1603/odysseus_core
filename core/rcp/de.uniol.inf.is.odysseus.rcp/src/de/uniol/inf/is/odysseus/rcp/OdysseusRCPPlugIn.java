/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.event.IEventListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulerManagerEvent;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulerManagerEvent.SchedulerManagerEventType;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulingEvent.SchedulingEventType;

public class OdysseusRCPPlugIn extends AbstractUIPlugin implements IEventListener {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp";
	
	public static final String TENANT_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.TenantView";
	public static final String NAVIGATOR_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.OdysseusProjectNavigatorView";
	public static final String SOURCES_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SourcesView";
	public static final String QUERY_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.QueryView";
	public static final String SINK_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SinkView";
	
	public static final String OBSERVER_PERSPECTIVE_ID = "de.uniol.inf.is.odysseus.rcp.perspectives.ObserverPerspective";
	public static final String QUERIES_PERSPECTIVE_ID = "de.uniol.inf.is.odysseus.rcp.perspectives.QueriesPerspective";
	
	public static final String REFRESH_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.RefreshSourcesViewCommand";
	public static final String COLLAPSE_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.CollapseSourcesViewCommand";
	public static final String EXPAND_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.ExpandSourcesViewCommand";	
	//public static final String ADD_QUERY_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.AddQueryCommand";
	public static final String REMOVE_QUERY_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.RemoveQueryCommand";
	public static final String STOP_QUERY_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.StopQueryCommand";
	public static final String START_QUERY_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.ResumeQueryCommand";
	public static final String SHOW_QUERY_DIALOG_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.ShowQueryDialogCommand";
	public static final String COPY_QUERYTEXT_COMMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.QueryViewCopyCommand";
	public static final String REFRESH_QUERY_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.RefreshQueryViewCommand";

	public static final String QUERY_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.QueryParameter";
	public static final String PARSER_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.ParserParameter";
	public static final String QUERY_ID_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.QueryIDParameter";
	public static final String PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.TransformationConfigurationNameParameter";
	public static final String USER_NAME_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.UserNameParameter";
	public static final String USER_PASSWORD_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.UserPasswordParameter";

	public static final String BUILD_CONFIGURATION_EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.buildConfiguration";

	public static final String ODYSSEUS_PROJECT_NATURE_ID = "de.uniol.inf.is.odysseus.rcp.OdysseusProjectNature";
	
	public static final String RCP_USER_TOKEN = "RCP";

	public static final String SOURCES_NAME_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.SourceNameParameter";

	private static OdysseusRCPPlugIn instance;
	private static IExecutor executor = null;
	
	public static OdysseusRCPPlugIn getDefault() {
		return instance;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		
		// Bilder registrieren
		ImageManager.getInstance().register("repository", "icons/repository_rep.gif");
		ImageManager.getInstance().register("user", "icons/user.png");
		ImageManager.getInstance().register("loggedinuser", "icons/user--plus.png");
		ImageManager.getInstance().register("users", "icons/users.png");
		ImageManager.getInstance().register("sla", "icons/document-block.png");
		ImageManager.getInstance().register("percentile", "icons/document-tag.png");
		ImageManager.getInstance().register("tenant", "icons/user-business.png");
		ImageManager.getInstance().register("role", "icons/tick-small-circle.png");
		ImageManager.getInstance().register("source", "icons/application-import.png");
		ImageManager.getInstance().register("view", "icons/table.png");
		ImageManager.getInstance().register("attribute", "icons/status.png");
		
		IDE.registerAdapters();
		
		instance = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
			
		instance = null;
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
	
	public void bindExecutor( IExecutor ex ) {
		executor = ex;

		StatusBarManager.getInstance().setMessage(StatusBarManager.EXECUTOR_ID, "Executor "+executor.getName()+" ready");
		try {
			StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID,executor.getCurrentSchedulerID()+" ("+executor.getCurrentSchedulingStrategyID()+ ") "+(executor.isRunning()?" running ":" stopped "));
			executor.getSchedulerManager().subscribeToAll(this);
			executor.getSchedulerManager().getActiveScheduler().subscribeToAll(this);
			// New: Start Scheduler at Query Start
			executor.startExecution();
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}
	
	public void unbindExecutor( IExecutor ex ) {
		executor = null;
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long eventNanoTime) {
		if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED){
			((SchedulerManagerEvent)event).getValue().unSubscribeFromAll(this);
		}else if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET){
			((SchedulerManagerEvent)event).getValue().subscribeToAll(this);
		}
		
		if (event.getEventType() == SchedulingEventType.SCHEDULING_STARTED
				|| event.getEventType() == SchedulingEventType.SCHEDULING_STOPPED ||
				event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED ||
				event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET	) {
			try {
				StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID,executor.getCurrentSchedulerID()+" ("+executor.getCurrentSchedulingStrategyID()+ ") "+(executor.isRunning()?" running ":" stopped "));
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		
	}

}
