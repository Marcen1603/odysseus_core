package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.evaluation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDistributionType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDistribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.ServerExecutorService;

public class AddManyBadlyShareableQueries extends Thread {
	private static final long timeIntervalInMillis = 10000;
	private static int counter = 0;
	private static final int iterations = 100;
	
	public void run() {
		IServerExecutor executor = ServerExecutorService.getServerExecutor();
		List<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>();
		newSettings.add(ParameterDoDistribute.TRUE);
		newSettings.add(new ParameterDistributionType("centralized"));
		
		ISession user = UserManagementProvider.getSessionmanagement().loginSuperUser(null, "");
		
		while(counter < iterations) {
			String pqlQuery = "";
			executor.addQuery(pqlQuery, "PQL", user, "Standard", newSettings);
			counter++;
			waitASecond();
		}
		
	}
	
	private void waitASecond() {
		Thread.currentThread();
		try {
			Thread.sleep(timeIntervalInMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
