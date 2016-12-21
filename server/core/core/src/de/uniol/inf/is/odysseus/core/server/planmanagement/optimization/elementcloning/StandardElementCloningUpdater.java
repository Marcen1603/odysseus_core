package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

/**
 * Default cloning updater
 *
 * @author Marco Grawunder
 *
 */

public class StandardElementCloningUpdater extends
		AbstractElementCloningUpdater {

	static private final ISession superUser = UserManagementProvider.getUsermanagement(true).getSessionManagement().loginSuperUser(null);

	static final StandardElementCloningUpdater instance = new StandardElementCloningUpdater();

	public static IElementCloningUpdater getInstance() {
		return instance;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateCloning(IExecutionPlan currentExecPlan) {
		Collection<IPhysicalQuery> queries = currentExecPlan.getQueries(superUser);
		GenericGraphWalker walker = new GenericGraphWalker();

		for (IPhysicalQuery q : queries) {
			List<IPhysicalOperator> roots = q.getRoots();
			// Walker
			// Updater
			for (IPhysicalOperator r : roots) {
				IGraphNodeVisitor visitor = new StandardCloningVisitor();
				walker.prefixWalkPhysical(r, visitor);
			}
		}
	}
}
