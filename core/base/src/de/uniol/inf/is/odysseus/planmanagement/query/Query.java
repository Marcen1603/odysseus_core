package de.uniol.inf.is.odysseus.planmanagement.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.SetOwnerGraphVisitor;

/**
 * Query is a standard implementation of a query in odysseus. Each query has an
 * unique ID and stores all relevant data (logical/physical plan, parser ID,
 * execution state etc.).
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class Query extends AbstractMonitoringDataProvider implements IQuery {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	/**
	 * Counter for ID creation.
	 */
	private static int idCounter = 0;

	/**
	 * Unique id of an ID. Used for identification of an query.
	 */
	private final int id;

	/**
	 * If available the text of the entered query
	 */
	private String queryText = null;

	/**
	 * The user who created this query
	 */
	private User user = null;

	/**
	 * List of all direct physical child operators. Stored separate because a
	 * root can contain operators which are part of an other query.
	 */
	private ArrayList<IPhysicalOperator> physicalChilds = new ArrayList<IPhysicalOperator>();

	/**
	 * Physical root operators of this query. Since we do not have trees any
	 * more, there can be more than one query.
	 */
	private List<IPhysicalOperator> roots;

	/**
	 * ID of the parser that should be used to translate the query string.
	 */
	private String parserID;

	/**
	 * Logical root operator of this query
	 */
	private ILogicalOperator logicalPlan;

	/**
	 * Indicates if this query is active.
	 */
	private boolean active;

	private boolean containsCycles = false;

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	private ArrayList<IQueryReoptimizeListener> queryReoptimizeListener = new ArrayList<IQueryReoptimizeListener>();

	/**
	 * List of rules for reoptimize requests.
	 */
	private ArrayList<AbstractQueryReoptimizeRule> queryReoptimizeRule = new ArrayList<AbstractQueryReoptimizeRule>();

	/**
	 * Priority of this query.
	 */
	private int priority = 0;

	/**
	 * Parameter for building this query.
	 */
	private QueryBuildConfiguration parameters = new QueryBuildConfiguration();

	/**
	 * EventListener
	 */

	Map<String, IPOEventListener> poEventListener = new HashMap<String, IPOEventListener>();

	@SuppressWarnings("rawtypes")
	public Map<String, IPlanMonitor> planmonitors = new HashMap<String, IPlanMonitor>();

	public Query() {
		this("", null, null, null);
	}

	/**
	 * Creates a query based on a physical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public Query(List<IPhysicalOperator> physicalPlan,
			QueryBuildConfiguration parameters) {
		this("", null, physicalPlan, parameters);
	}

	/**
	 * Creates a query based on a parser ID and {@link QueryBuildConfiguration}
	 * 
	 * @param parserID
	 *            ID of the parser to use
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public Query(String parserID, QueryBuildConfiguration parameters) {
		this(parserID, null, null, parameters);
	}

	/**
	 * Creates a query based on a logical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param logicalPlan
	 *            logical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public Query(ILogicalOperator logicalPlan,
			QueryBuildConfiguration parameters) {
		this("", logicalPlan, null, parameters);
	}

	/**
	 * Creates a query based on a parserID, a logical plan, a physical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param parserID
	 *            logical operator plan
	 * @param logicalPlan
	 *            logical operator plan
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	private Query(String parserID, ILogicalOperator logicalPlan,
			List<IPhysicalOperator> physicalPlan,
			QueryBuildConfiguration parameters) {
		this.id = idCounter++;
		this.active = true;
		this.parameters = parameters;
		this.parserID = parserID;
		this.logicalPlan = logicalPlan;
		this.parameters = parameters;

		if (this.parameters != null) {
			if (this.parameters.getPriority() != null) {
				this.priority = this.parameters.getPriority();
			}
		}

		if (physicalPlan != null) {
			// initialize physical plan, set root, store children etc.
			initializePhysicalRoots(physicalPlan);
		}
	}

	/**
	 * Provides an info string which describes the query.
	 * 
	 * @return info string which describes the query.
	 */
	public String getDebugInfo() {
		String info = "";
		info += "ID:" + this.id;
		info += "Started:" + this.active;
		info += "CompileLanguage:" + this.parserID;
		info += "LogicalAlgebra:" + AppEnv.LINE_SEPARATOR + this.logicalPlan;
		info += "PhysicalAlgebra:" + AppEnv.LINE_SEPARATOR + this.roots;
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * setLogicalPlan(de.uniol.inf.is.odysseus.ILogicalOperator)
	 */
	@Override
	public void setLogicalPlan(ILogicalOperator logicalPlan, boolean setOwner) {
		this.logicalPlan = logicalPlan;
		if (setOwner) {
			// Set Owner
			SetOwnerGraphVisitor<ILogicalOperator> visitor = new SetOwnerGraphVisitor<ILogicalOperator>(
					this);
			AbstractGraphWalker walker = new AbstractGraphWalker();
			walker.prefixWalk(logicalPlan, visitor);
		}else{
			if (!logicalPlan.hasOwner()){
				throw new IllegalArgumentException("LogicalPlan must have an owner");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.query.IQuery#setRoot
	 * (de.uniol.inf.is.odysseus.IPhysicalOperator)
	 */
	@Override
	public List<IPhysicalOperator> setRoots(List<IPhysicalOperator> roots) {
		getLogger().debug("setRoots " + roots);
		this.roots = roots;

		// evaluate built parameter
		if (this.parameters != null) {
			IPhysicalOperator defaultRoot = null;

			// set default root (e. g. defined Sinks)
			if (this.parameters.getDefaultRoot() != null) {
				defaultRoot = this.parameters.getDefaultRoot();

				ArrayList<IPhysicalOperator> newRoots = new ArrayList<IPhysicalOperator>();
				// default root must be set for each root now
				for (IPhysicalOperator oldRoot : this.roots) {
					// register default root TODO hier koennen fehler
					// uebersprungen
					// werden, wenn
					// root keine source ist
					if (defaultRoot != null && defaultRoot.isSink()
							&& oldRoot.isSource()) {
						IPhysicalOperator cloned = this.parameters
								.getDefaultRootStrategy()
								.connectDefaultRootToSource(
										(ISink<?>) defaultRoot, oldRoot);
						// ((ISink) defaultRoot).subscribeToSource((ISource)
						// oldRoot, 0,
						// 0, oldRoot.getOutputSchema());
						// this.root = defaultRoot;

						// only add the default root
						// to the list of new roots, if
						// it has been cloned. We only
						// want to have different roots
						// in the list of roots of this
						// query.
						if (!containsReference(newRoots, cloned)) {
							newRoots.add(cloned);
						}
					}
				}
				// this.roots = newRoots;
			}
		}
		getLogger().debug("setRoots " + roots);
		return this.roots;
	}

	/**
	 * Checks, whether a reference to an object is already contained in a list.
	 */
	private boolean containsReference(List<?> listOfObj, Object obj) {
		for (Object o : listOfObj) {
			if (o == obj) {
				return true;
			}
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * getPhysicalChilds()
	 */
	@Override
	public ArrayList<IPhysicalOperator> getPhysicalChilds() {
		return this.physicalChilds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * initializePhysicalPlan(de.uniol.inf.is.odysseus.IPhysicalOperator)
	 */
	@Override
	public void initializePhysicalRoots(List<IPhysicalOperator> roots) {
		// set root of this querie
		setRoots(roots);

		// Store each child in a list. And set this Query as owner of each child
		for (IPhysicalOperator root : roots) {
			// addPhysicalChildren(GraphHelper.getChildren(root));
			addPhysicalChildren(getChildren(root));
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<IPhysicalOperator> getChildren(IPhysicalOperator root) {
		ArrayList<IPhysicalOperator> children = new ArrayList<IPhysicalOperator>();
		Stack<IPhysicalOperator> operators = new Stack<IPhysicalOperator>();
		Set<IPhysicalOperator> visitedOps = new HashSet<IPhysicalOperator>();
		operators.push(root);

		while (!operators.isEmpty()) {
			IPhysicalOperator curOp = operators.pop();
			children.add(curOp);
			visitedOps.add(curOp);
			if (curOp.isSink()) {
				@SuppressWarnings("rawtypes")
				Collection<PhysicalSubscription<ISource<?>>> subsriptions = ((ISink) curOp)
						.getSubscribedToSource();
				for (PhysicalSubscription<ISource<?>> subscription : subsriptions) {
					ISource<?> target = subscription.getTarget();
					if (!visitedOps.contains(target)) {
						operators.push(target);
					}
				}
			}
		}

		return children;
	}

	/**
	 * Sets the physical children of this query. These children are the physical
	 * operators which are necessary for the execution of this query. It also
	 * sets the owner relationship between the query and the operators.
	 * 
	 * This method should be only used if special optimizations are processed.
	 * For initial setting the physical plan use
	 * {@link #initializePhysicalPlan(IPhysicalOperator)}.
	 * 
	 * @param physicalChilds
	 *            Physical operators which are necessary for the execution of
	 *            this query.
	 */
	private void addPhysicalChildren(ArrayList<IPhysicalOperator> children) {
		synchronized (this.physicalChilds) {
			// Store children, if not already done.
			for (IPhysicalOperator child : children) {
				addChild(child);
			}
		}
	}

	public void addChild(IPhysicalOperator child) {
		if (!this.physicalChilds.contains(child)) {
			this.physicalChilds.add(child);
			child.addOwner(this);
		}
	}

	private boolean removeChild(IPhysicalOperator child) {
		if (this.physicalChilds.remove(child)) {
			child.removeOwner(this);
			return true;
		}
		return false;
	}

	/**
	 * replace an physical operator of the query plan with another operator
	 * operator subscription are not touched, so correct subscriptions need to
	 * be set before replacement
	 * 
	 * @param oldOp
	 * @param newOp
	 */
	public void replaceOperator(IPhysicalOperator oldOp, IPhysicalOperator newOp) {
		if (removeChild(oldOp)) {
			addChild(newOp);
		}// TODO: Exception werfen?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * removeOwnerschip()
	 */
	@Override
	public void removeOwnerschip() {

		getLogger().debug("Remove ownership start");
		for (IPhysicalOperator physicalOperator : this.physicalChilds) {
			getLogger().debug("Remove Ownership for " + physicalOperator);
			physicalOperator.removeOwner(this);
			// if (!physicalOperator.hasOwner()) {
			// getLogger()
			// .debug("No more owners. Closing " + physicalOperator);
			// // physicalOperator.close();
			// getLogger().error("ATTENTION: CLOSING CURRENT NOT IMPLEMENTED");
			// if (physicalOperator.isSink()) {
			// getLogger().debug(
			// "Sink unsubscribe from all sources "
			// + physicalOperator);
			// ISink<?> sink = (ISink<?>) physicalOperator;
			// sink.unsubscribeFromAllSources();
			// }
			// }
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.query.IQuery#start()
	 */
	@Override
	public void start() {
		// synchronized (this.physicalChilds) {
		// for (IPhysicalOperator physicalOperator : this.physicalChilds) {
		// if (physicalOperator instanceof IIterableSource<?>) {
		// ((IIterableSource<?>) physicalOperator)
		// .activateRequest(this);
		// }
		// }
		// }
		this.active = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.query.IQuery#stop()
	 */
	@Override
	public void stop() {
		// synchronized (this.physicalChilds) {
		// for (IPhysicalOperator physicalOperator : this.physicalChilds) {
		// if (physicalOperator instanceof IIterableSource<?>) {
		// ((IIterableSource<?>) physicalOperator)
		// .deactivateRequest(this);
		// }
		// }
		// }
		this.active = false;
	}

	@Override
	public boolean isOpened() {
		for (IPhysicalOperator o : getRoots()) {
			if (!o.isOpen()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester#reoptimize
	 * ()
	 */
	@Override
	public void reoptimize() {
		for (IQueryReoptimizeListener reoptimizeListener : this.queryReoptimizeListener) {
			reoptimizeListener.reoptimize(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler#
	 * addReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void addReoptimizeListener(
			IQueryReoptimizeListener reoptimizationListener) {
		synchronized (this.queryReoptimizeListener) {
			if (!this.queryReoptimizeListener.contains(reoptimizationListener)) {
				this.queryReoptimizeListener.add(reoptimizationListener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler#
	 * removeReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void removeReoptimizeListener(
			IQueryReoptimizeListener reoptimizationListener) {
		synchronized (this.queryReoptimizeListener) {
			this.queryReoptimizeListener.remove(reoptimizationListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester#
	 * addReoptimzeRule
	 * (de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void addReoptimzeRule(AbstractQueryReoptimizeRule reoptimizeRule) {
		reoptimizeRule.addReoptimieRequester(this);
		synchronized (this.queryReoptimizeRule) {
			if (!this.queryReoptimizeRule.contains(reoptimizeRule)) {
				this.queryReoptimizeRule.add(reoptimizeRule);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester#
	 * removeReoptimzeRule
	 * (de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void removeReoptimzeRule(AbstractQueryReoptimizeRule reoptimizeRule) {
		reoptimizeRule.removeReoptimieRequester(this);
		synchronized (this.queryReoptimizeRule) {
			this.queryReoptimizeRule.remove(reoptimizeRule);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.query.IQuery#getPriority()
	 */
	@Override
	public int getPriority() {
		return this.priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.query.IQuery#setPriority
	 * (int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * getLogicalPlan()
	 */
	@Override
	public ILogicalOperator getLogicalPlan() {
		return this.logicalPlan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.query.IQuery#getRoot ()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		if (roots != null) {
			return Collections.unmodifiableList(this.roots);
		} else {
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * getBuildParameter()
	 */
	@Override
	public QueryBuildConfiguration getBuildParameter() {
		return this.parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner#getID()
	 */
	@Override
	public int getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.IOperatorControl#îsRunning()
	 */
	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public String getParserId() {
		return parserID;
	}

	@Override
	public String getQueryText() {
		return queryText;
	}

	@Override
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setParserId(String parserId) {
		this.parserID = parserId;
	}

	@Override
	public void setBuildParameter(QueryBuildConfiguration parameter) {
		this.parameters = parameter;
	}

	@Override
	public boolean containsCycles() {
		return containsCycles;
	}

	public void setContainsCycles(boolean containsCycles) {
		this.containsCycles = containsCycles;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addPlanMonitor(String name, IPlanMonitor planMonitor) {
		this.planmonitors.put(name, planMonitor);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPlanMonitor getPlanMonitor(String name) {
		return this.planmonitors.get(name);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Collection<IPlanMonitor> getPlanMonitors() {
		return Collections.unmodifiableCollection(planmonitors.values());
	}

	/**
	 * Replaces a Root in the Query with another Physical Operator (Has no
	 * effect, if the oldRoot-argument is no root for this Query)
	 * 
	 * @param oldRoot
	 *            The root that is being replaced
	 * @param newRoot
	 *            The replacement for the old root
	 * 
	 */
	public void replaceRoot(IPhysicalOperator oldRoot, IPhysicalOperator newRoot) {

		if (this.roots.contains(oldRoot)) {
			ArrayList<IPhysicalOperator> oldRoots = new ArrayList<IPhysicalOperator>(
					this.roots);
			oldRoots.remove(oldRoot);
			oldRoots.add(newRoot);
			this.setRoots(oldRoots);
		}
	}

	@Override
	public String toString() {
		return "Query " + getID();
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Query other = (Query) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
