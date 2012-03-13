package de.uniol.inf.is.odysseus.core.server.planmanagement.query;

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

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IDefaultRootStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.sla.SLA;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PhysicalQuery implements IPhysicalQuery {

	transient protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(PhysicalQuery.class);
		}
		return _logger;
	}

	private ILogicalQuery query;
	private SLA sla;
	final private IPhysicalOperator defaultRoot;
	final private IDefaultRootStrategy defaultRootStrategy;
	private int priority;
	private boolean containsCycles;

	/**
	 * Counter for ID creation.
	 */
	private static int idCounter = 0;

	/**
	 * Unique id of an ID. Used for identification of an query.
	 */
	private final int id;

	private AbstractMonitoringDataProvider mdP = new AbstractMonitoringDataProvider() {
	};

	/**
	 * List of all direct physical child operators. Stored separate because a
	 * root can contain operators which are part of an other query.
	 */
	transient private ArrayList<IPhysicalOperator> physicalChilds = new ArrayList<IPhysicalOperator>();

	/**
	 * Physical root operators of this query. Since we do not have trees any
	 * more, there can be more than one query.
	 */
	transient private List<IPhysicalOperator> roots;

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	transient private ArrayList<IQueryReoptimizeListener> queryReoptimizeListener = new ArrayList<IQueryReoptimizeListener>();

	/**
	 * List of rules for reoptimize requests.
	 */
	transient private ArrayList<AbstractQueryReoptimizeRule> queryReoptimizeRule = new ArrayList<AbstractQueryReoptimizeRule>();

	/**
	 * EventListener
	 */

	transient Map<String, IPOEventListener> poEventListener = new HashMap<String, IPOEventListener>();

	@SuppressWarnings("rawtypes")
	transient public Map<String, IPlanMonitor> planmonitors = new HashMap<String, IPlanMonitor>();

	private boolean opened = false;
	private ISession user;

	/**
	 * Creates a query based on a physical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public PhysicalQuery(List<IPhysicalOperator> physicalPlan,
			IPhysicalOperator defaultRoot,
			IDefaultRootStrategy defaultRootStrategy) {
		id = idCounter++;
		this.defaultRoot = defaultRoot;
		this.defaultRootStrategy = defaultRootStrategy;
		initializePhysicalRoots(physicalPlan);
	}

	public PhysicalQuery(ILogicalQuery query,
			ArrayList<IPhysicalOperator> physicalPlan,
			IPhysicalOperator defaultRoot,
			IDefaultRootStrategy defaultRootStrategy) {
		id = idCounter++;
		this.query = query;
		this.defaultRoot = defaultRoot;
		this.defaultRootStrategy = defaultRootStrategy;
		this.sla = query.getSLA();
		this.user = query.getUser();
		this.priority = query.getPriority();
		this.containsCycles = query.containsCycles();
		initializePhysicalRoots(physicalPlan);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#getRoot
	 * ()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		if (roots != null) {
			return Collections.unmodifiableList(this.roots);
		}
        return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#setRoot
	 * (de.uniol.inf.is.odysseus.core.server.IPhysicalOperator)
	 */
	@Override
	public List<IPhysicalOperator> setRoots(List<IPhysicalOperator> roots) {
		getLogger().debug("setRoots " + roots);
		this.roots = roots;
		// set default root (e. g. defined Sinks)
		if (defaultRoot != null) {

			ArrayList<IPhysicalOperator> newRoots = new ArrayList<IPhysicalOperator>();
			// default root must be set for each root now
			for (IPhysicalOperator oldRoot : this.roots) {
				// register default root TODO hier koennen fehler
				// uebersprungen
				// werden, wenn
				// root keine source ist
				if (defaultRoot != null && defaultRoot.isSink()
						&& oldRoot.isSource()) {
					IPhysicalOperator cloned = defaultRootStrategy
							.connectDefaultRootToSource((ISink<?>) defaultRoot,
									oldRoot);
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
		// getLogger().debug("setRoots " + roots);
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
	 * initializePhysicalPlan
	 * (de.uniol.inf.is.odysseus.core.server.IPhysicalOperator)
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

	@Override
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
	@Override
	public void replaceOperator(IPhysicalOperator oldOp, IPhysicalOperator newOp) {
		if (removeChild(oldOp)) {
			addChild(newOp);
		}// TODO: Exception werfen?
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
	@Override
	public void replaceRoot(IPhysicalOperator oldRoot, IPhysicalOperator newRoot) {

		if (this.roots.contains(oldRoot)) {
			ArrayList<IPhysicalOperator> oldRoots = new ArrayList<IPhysicalOperator>(
					this.roots);
			oldRoots.remove(oldRoot);
			oldRoots.add(newRoot);
			this.setRoots(oldRoots);
		}
	}

	/**
	 * returns a set of physical operators, that a query shares with the given
	 * query. in fact this is the intersection of the physical operators of this
	 * query with the given query
	 * 
	 * @param otherQuery
	 * @return
	 */
	@Override
	public Set<IPhysicalOperator> getSharedOperators(IPhysicalQuery otherQuery) {
		Set<IPhysicalOperator> ops1 = this.getAllOperators();
		Set<IPhysicalOperator> ops2 = this.getAllOperators();

		ops1.retainAll(ops2);

		return ops1;
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
		}
	}

	@Override
	public void open() throws OpenFailedException {
		for (IPhysicalOperator curRoot : getRoots()) {
			// this also works for cyclic plans,
			// since if an operator is already open, the
			// following sources will not be called any more.
			if (curRoot.isSink()) {
				((ISink<?>) curRoot).open();
			} else {
				throw new IllegalArgumentException(
						"Open cannot be called on a source");
			}
		}
		opened = true;
	}

	@Override
	public void close() {
		for (IPhysicalOperator curRoot : getRoots()) {
			// this also works for cyclic plans,
			// since if an operator is already closed, the
			// following sources will not be called any more.
			if (curRoot.isSink()) {
				if (curRoot.isOpen()) {
					((ISink<?>) curRoot).close();
				}
			} else {
				throw new IllegalArgumentException(
						"Close cannot be called on a a source");
			}
		}
		opened = false;
	}

	@Override
	public boolean isOpened() {
		return opened;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester
	 * #reoptimize ()
	 */
	@Override
	public void reoptimize() {
		// FIXME: Reoptimization currently not supported
		throw new IllegalArgumentException(
				"Reoptimization currently not implemented");
		// for (IQueryReoptimizeListener reoptimizeListener :
		// this.queryReoptimizeListener) {
		// reoptimizeListener.reoptimize(this);
		// }
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
	 * (de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRule)
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
	 * (de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void removeReoptimzeRule(AbstractQueryReoptimizeRule reoptimizeRule) {
		reoptimizeRule.removeReoptimieRequester(this);
		synchronized (this.queryReoptimizeRule) {
			this.queryReoptimizeRule.remove(reoptimizeRule);
		}
	}

	/**
	 * returns a set of all operators used by the query
	 * 
	 * @return
	 */
	@Override
	public Set<IPhysicalOperator> getAllOperators() {
		Set<IPhysicalOperator> ops = new HashSet<IPhysicalOperator>();
		for (IPhysicalOperator root : this.roots) {
			List<IPhysicalOperator> children = this.getChildren(root);
			ops.addAll(children);
			ops.add(root);
		}

		return ops;
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

	// Delegates
	@Override
	public Collection<String> getProvidedMonitoringData() {
		return mdP.getProvidedMonitoringData();
	}

	@Override
	public boolean providesMonitoringData(String type) {
		return mdP.providesMonitoringData(type);
	}

	@Override
	public <T> IMonitoringData<T> getMonitoringData(String type) {
		return mdP.getMonitoringData(type);
	}

	@Override
	public void createAndAddMonitoringData(
			@SuppressWarnings("rawtypes") IPeriodicalMonitoringData item,
			long period) {
		mdP.createAndAddMonitoringData(item, period);
	}

	@Override
	public void addMonitoringData(String type, IMonitoringData<?> item) {
		mdP.addMonitoringData(type, item);
	}

	@Override
	public void removeMonitoringData(String type) {
		mdP.removeMonitoringData(type);
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public SLA getSLA() {
		return sla;
	}

	@Override
	public void setSLA(SLA sla) {
		this.sla = sla;
	}

	@Override
	public void setUser(ISession user) {
		this.user = user;
	}

	@Override
	public ISession getUser() {
		return user;
	}

	// @Override
	// public QueryBuildConfiguration getBuildParameter() {
	// return qbConfig;
	// }

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean containsCycles() {
		return containsCycles;
	}

	@Override
	public ILogicalQuery getLogicalQuery() {
		return query;
	}
}
