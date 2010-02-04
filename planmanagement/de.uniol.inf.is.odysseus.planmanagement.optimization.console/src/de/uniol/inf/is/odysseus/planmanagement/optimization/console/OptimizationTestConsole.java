package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

/**
 * custom OSGi console to test planoptimization scenarios
 * 
 * @author Tobias Witt
 *
 */
public class OptimizationTestConsole implements	org.eclipse.osgi.framework.console.CommandProvider {
	
	private IAdvancedExecutor executor;
	
	@SuppressWarnings("unchecked")
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration("relational", ITimeInterval.class));
	
	public void bindExecutor(IAdvancedExecutor executor){
		this.executor = executor;
		System.out.println("executor gebunden");
	}

	@Override
	public String getHelp() {
		return " --- Optimization Test Console Functions --- \n"
			+"  m - default planmigration test\n";
	}
	
	public void _m (CommandInterpreter ci) {
		try {
			this.executor.addQuery("CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )", 
					parser(),this.trafoConfigParam);
			Collection<Integer> queryIds = this.executor.addQuery("SELECT (a * 2) as value FROM test WHERE a > 2", 
					parser(), /*new ParameterDefaultRoot(new MySink()),*/ this.trafoConfigParam);
			/*Collection<Integer> queryIds = this.executor.addQuery("SELECT (t1.a + t2.a) as value FROM test RANGE 5 SECONDS AS t1, test RANGE 5 SECONDS AS t2 WHERE t2.a > 2", 
					parser(), new ParameterDefaultRoot(new new MySink()), this.trafoConfigParam);*/
			this.executor.startExecution();
			
			IEditablePlan plan = (IEditablePlan)this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(queryIds.iterator().next());
			//query.stop();
			
			//query.reoptimize();
			System.out.println(this.executor.getInfos());
			
			
			
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String parser() {
		return "CQL";
	}

}
