package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
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
			
			IPhysicalOperator op = query.getRoot();
			System.out.println(op.getName());
			
			query.reoptimize();
			
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void _n (CommandInterpreter ci) {
		try {
			this.executor.addQuery("CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )", 
					parser(),this.trafoConfigParam);
			Collection<Integer> queryIds = this.executor.addQuery("SELECT (a * 2) as value FROM test WHERE a > 2", 
					parser(), this.trafoConfigParam);
			queryIds.addAll(this.executor.addQuery("SELECT (a * 3) as value FROM test WHERE a > 1", 
					parser(), this.trafoConfigParam));
			//this.executor.startExecution();
			
			IEditablePlan plan = (IEditablePlan)this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(0);
			
			IPhysicalOperator p1 = query.getRoot();
			IPhysicalOperator p2 = plan.getQuery(1).getRoot();
			
			System.out.println("---------------------");
			System.out.println(p1.getName());
			System.out.println(p2.getName());
			
			List<IPhysicalOperator> sources1 = new ArrayList<IPhysicalOperator>();
			getSources(sources1, p1);
			IPhysicalOperator s1 = sources1.get(0);
			sources1.clear();
			getSources(sources1, p2);
			IPhysicalOperator s2 = sources1.get(0);
			
			System.out.println(s1==s2);
			
			for (PhysicalSubscription s : ((ISource<?>)s1).getSubscriptions()) {
				IPhysicalOperator op = (IPhysicalOperator)s.getTarget();
				System.out.println(op.getName());
				System.out.println(op.getOwner());
			}
			
			
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getSources(List<IPhysicalOperator> sources, IPhysicalOperator op) {
		if (!op.isSink()) {
			sources.add(op);
			return;
		}
		for ( PhysicalSubscription<?> sub : ((ISink<?>)op).getSubscribedToSource()) {
			getSources(sources, (IPhysicalOperator)sub.getTarget());
		}
	}
	
	

	private String parser() {
		return "CQL";
	}

}
