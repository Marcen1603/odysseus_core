package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.evaluation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDistributionType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDistribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.CentralizedDistributor;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.ServerExecutorService;

public class AddManyBadlyShareableQueries extends Thread {
	private static final long timeIntervalInMillis = 10000;
	private static int counter = 0;
	private static final int iterations = 100;
	private static int windowSize = 5;
	
	@Override
	public void run() {
		IServerExecutor executor = ServerExecutorService.getServerExecutor();
		List<IQueryBuildSetting<?>> newSettings = new ArrayList<IQueryBuildSetting<?>>();
		newSettings.add(ParameterDoDistribute.TRUE);
		newSettings.add(new ParameterDistributionType("centralized"));
		
		ISession user = UserManagementProvider.getSessionmanagement().loginSuperUser(null, "");
		
		while(counter < iterations && !CentralizedDistributor.getInstance().isEvaluationFinished()) {
			windowSize = counter % 3 == 0 ? windowSize * 2 : windowSize;
			String source1 = counter%2 == 0 ? "auction" : "SELECT({predicate=RelationalPredicate('initialbid > 10')}, auction)";
			String pqlQuery = "s" + counter + " = join({predicate = RelationalPredicate('id = auction')}, WINDOW({size = " + windowSize + ",advance = 1,type = 'time'}, " + source1 + "), WINDOW({size = " + windowSize + ",advance = 1,type = 'time'}, bid))";
			Iterator<Integer> it = executor.addQuery(pqlQuery, "PQL", user, "Standard", null, newSettings).iterator();
			while(it.hasNext()) {
				executor.startQuery(it.next(), user);
			}
			counter++;
			waitASecond();
		}
		System.out.println(CentralizedDistributor.getInstance().evaluationToString());
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
