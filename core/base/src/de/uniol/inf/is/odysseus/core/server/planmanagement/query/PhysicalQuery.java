/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PhysicalQuery implements IPhysicalQuery {

	transient protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(PhysicalQuery.class);
		}
		return _logger;
	}

	/**
	 * The logical query, this physical query is build from
	 */
	private ILogicalQuery query;

	/**
	 * The name of the query
	 */
	private String name = "";
	
	/**
	 * Priority for the query
	 */
	private int basePriority;
	
	/**
	 * Current priority, e.g. used by scheduling
	 */
	private long currentPriority;

	/**
	 * If the query contains cycles, other handling is needed
	 */
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
	 * Sources that should be scheduled.
	 */
	final private ArrayList<IIterableSource<?>> iterableSources = new ArrayList<IIterableSource<?>>();;
	
	/**
	 * Sources that are leafs
	 */
	final private ArrayList<IIterableSource<?>> leafSources = new ArrayList<IIterableSource<?>>();

	/**
	 * Cache Ids for Sources to speed up getSourceID
	 */
	private Map<IIterableSource<?>, Integer> sourceIds;
	
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

	/**
	 * What monitors are installed on this plan
	 */
	transient public Map<String, IPlanMonitor<?>> planmonitors = new HashMap<String, IPlanMonitor<?>>();

	/**
	 * Is the query running (open is called already)
	 */
	private boolean opened = false;
	
	/**
	 * Who has send the query
	 */
	private ISession user;
	
	/**
	 * To avoid dependencies, some values are only set as key value pairs
	 */
	final private Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * Creates a query based on a physical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public PhysicalQuery(List<IPhysicalOperator> physicalPlan) {
		// Query created directly from physical plans get a negative query id to
		// distinct from query created from logical queries (and garantee that
		// logical and corresponding physical queries have the same id)
		id = (-1)*idCounter++;
		initializePhysicalRoots(physicalPlan);
		determineIteratableSourcesAndLeafs(physicalPlan);
	}

	/**
	 * Create a new physical query 
	 * @param query The logical query that is the origin of the query
	 * @param physicalPlan The physical plan
	 */
	public PhysicalQuery(ILogicalQuery query,
			ArrayList<IPhysicalOperator> physicalPlan) {
		// logical and physical query must have the same id!
		id = query.getID();
		this.query = query;
		this.user = query.getUser();
		this.basePriority = query.getPriority();
		this.currentPriority = query.getPriority();
		this.containsCycles = query.containsCycles();
		initializePhysicalRoots(physicalPlan);
		determineIteratableSourcesAndLeafs(physicalPlan);
	}
	
	/**
	 * Some operators need to be scheduled typically buffers
	 * To allow other processing of operators that are sources
	 * these leafSources are treated different
	 * @param physicalPlan
	 */
	private void determineIteratableSourcesAndLeafs(
			List<IPhysicalOperator> physicalPlan) {
		List<IPhysicalOperator> queryOps = new ArrayList<IPhysicalOperator>(
				getPhysicalChilds());
		queryOps.addAll(getRoots());
		Set<IOperatorOwner> owners = new HashSet<IOperatorOwner>();
		
		for (IPhysicalOperator operator : queryOps) {
			owners.addAll(operator.getOwner());
			IIterableSource<?> iterableSource = null;
			if (operator instanceof IIterableSource) {
				iterableSource = (IIterableSource<?>) operator;
				// IterableSource is a Pipe
				if (iterableSource.isSink()
						&& !iterableSources.contains(iterableSource)) {
					iterableSources.add(iterableSource);
				} else if (!iterableSource.isSink() // IterableSource
													// is a
													// global Source
						&& !leafSources.contains(iterableSource)) {
					leafSources.add(iterableSource);
				}
			}
		}
		
		this.sourceIds = new HashMap<IIterableSource<?>, Integer>();
		for (int i = 0; i < iterableSources.size(); i++) {
			sourceIds.put(iterableSources.get(i), i); // Iterator does not
														// garantee order ...
														// (?)
		}

	}

	@Override
	public String getName() {
		if (query != null){
			return query.getName();
		}
		return name;
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
		this.roots = roots;
		return this.roots;
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
	private static ArrayList<IPhysicalOperator> getChildren(IPhysicalOperator root) {
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
				((ISink<?>) curRoot).open(this);
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
					((ISink<?>) curRoot).close(this);
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
			List<IPhysicalOperator> children = PhysicalQuery.getChildren(root);
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

	@Override
	public IPlanMonitor<?> getPlanMonitor(String name) {
		return this.planmonitors.get(name);
	}

	@Override
	public Collection<IPlanMonitor<?>> getPlanMonitors() {
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
	public void setSession(ISession user) {
		this.user = user;
	}

	@Override
	public ISession getSession() {
		return user;
	}
	
	@Override
	public boolean isOwner(ISession session) {
		if (session == null || user == null){
			return false;
		}
		return user.getUser().getName().equals(session.getUser().getName());
	}

	// @Override
	// public QueryBuildConfiguration getBuildParameter() {
	// return qbConfig;
	// }

	// --------------------------------------------------------
	// Query Priority
	// --------------------------------------------------------
	@Override
	public int getPriority() {
		return basePriority;
	}
	
	@Override
	public void setCurrentPriority(long newPriority) {
		this.currentPriority = newPriority;
	}
	
	@Override
	public long getCurrentPriority() {
		return currentPriority;
	}

	@Override
	public long getBasePriority() {
		return this.basePriority;
	}

	@Override
	public boolean containsCycles() {
		return containsCycles;
	}

	@Override
	public ILogicalQuery getLogicalQuery() {
		return query;
	}
	
	@Override
	public void setParameter(String key, Object value) {
		parameters.put(key, value);
	}

	@Override
	public Object getParameter(String key) {
		Object param = parameters.get(key);
		if (param == null) {
			param = query.getParameter(key);
		}
		return param;
	}
	
	///-------------------------------------------------------
	// Iteratable Sources for Scheduling
	// -------------------------------------------------------
	@Override
	public boolean hasIteratableSources() {
		return iterableSources != null && iterableSources.size() > 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IPhysicalQuery#
	 * getIterableSource()
	 */
	@Override
	public List<IIterableSource<?>> getIterableSources() {
		return Collections.unmodifiableList(iterableSources);
	}

	@Override
	public IIterableSource<?> getIterableSource(int id) {
		return iterableSources.get(id);
	}

	@Override
	public synchronized int getSourceId(IIterableSource<?> source) {
		Integer id = sourceIds.get(source);
		return id != null ? id : -1;
	}

	@Override
	public List<IIterableSource<?>> getLeafSources() {
		return Collections.unmodifiableList(leafSources);
	}
	
}
