package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class Helper {

	private static final Logger log = LoggerFactory.getLogger(Helper.class);

	public static boolean oneTargetNotInList(List<ILogicalOperator> operators, Collection<LogicalSubscription> subscriptions) {

		for (final LogicalSubscription subscription : subscriptions) {

			if (!operators.contains(subscription.getTarget()))
				return true;

		}
		return false;
	}

	public static ISession getActiveSession() {
		return UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
	}

	public static IPhysicalQuery getPhysicalQuery(ILogicalQuery query, String transCfgName) {
		IQueryBuildConfigurationTemplate settings = ServerExecutorService.getServerExecutor().getQueryBuildConfiguration(transCfgName);
		ArrayList<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>(settings.getConfiguration());
		QueryBuildConfiguration config = new QueryBuildConfiguration(newSettings.toArray(new IQueryBuildSetting<?>[0]), transCfgName);
		config.getTransformationConfiguration().setVirtualTransformation(true);

		return ServerExecutorService.getServerExecutor().getCompiler().transform(query, config.getTransformationConfiguration(), getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()));
	}

	public static List<ILogicalQuery> getLogicalQuery(String pqlStatement) {
		List<IExecutorCommand> commands = ServerExecutorService.getServerExecutor().getCompiler().translateQuery(pqlStatement, "PQL", getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()), null);
		List<ILogicalQuery> list = Lists.newArrayList();
		for (IExecutorCommand q : commands) {

			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();
				final List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(query.getLogicalPlan(), operators);
				list.add(query);
			}
		}
		return list;
	}

	public static boolean allSourcesAvailable(ILogicalOperator logicalPlan) {
		List<ILogicalOperator> sources = Lists.newArrayList();
		for(ILogicalOperator op : LogicalQueryHelper.getAllOperators(logicalPlan)) {
			if(op instanceof AccessAO || op instanceof StreamAO) {
				sources.add(op);
			}
		}
		
		boolean sourcesAvailable = true;
		
		if(!sources.isEmpty()) {
			log.debug("Sub query contains {} source operators", sources.size());
			log.debug("Checking if all sources are available");
			
			ISession session = getActiveSession();
		
			for(ILogicalOperator source : sources) {
				String sourceName = session.getUser().getName() + "." + source.getName();
				if (!DataDictionaryProvider.getDataDictionary(session.getTenant())
						.containsViewOrStream(sourceName, session)) {
					sourcesAvailable = false;
					break;
				}
			}				
		}
		return sourcesAvailable;
	}}
