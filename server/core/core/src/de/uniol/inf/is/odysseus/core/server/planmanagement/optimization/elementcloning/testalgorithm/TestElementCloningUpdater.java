package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning.testalgorithm;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning.AbstractElementCloningUpdater;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning.IElementCloningUpdater;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.ExtendedGraphWalker;
import de.uniol.inf.is.odysseus.core.util.IExtendedGraphNodeVisitor;

/**
 * Test cloning updater
 * 
 * @author Henrik Surm
 *
 */

public class TestElementCloningUpdater extends AbstractElementCloningUpdater 
{
	private static final TestElementCloningUpdater instance = new TestElementCloningUpdater();	

	public static IElementCloningUpdater getInstance() 
	{
		return instance;
	}
	
	Map<IPhysicalOperator, TestOperatorData> operatorDataMap = new IdentityHashMap<>();

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateCloning(IExecutionPlan currentExecPlan) {
		Collection<IPhysicalQuery> queries = currentExecPlan.getQueries();
		ExtendedGraphWalker walker = new ExtendedGraphWalker(true, false);

		System.out.println("updateCloning");
		for (IPhysicalQuery q : queries) 
		{
			List<IPhysicalOperator> roots = q.getRoots();
			for (IPhysicalOperator r : roots) 
			{
				if (!operatorDataMap.containsKey(r))
				{				
					operatorDataMap.put(r, new TestOperatorData());
					
					System.out.println("New root operator: " + r.getName());
					IExtendedGraphNodeVisitor visitor = new TestCloningVisitor(this);
					walker.prefixWalkPhysical(r, visitor);
				}
			}
		}
	}
}
