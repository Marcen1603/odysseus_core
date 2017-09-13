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

import de.uniol.inf.is.odysseus.core.collection.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryFunction;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ACQueryParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PhysicalQuery implements IPhysicalQuery {

	transient protected static Logger _logger = LoggerFactory.getLogger(PhysicalQuery.class);

	/**
	 * The logical query, this physical query is build from
	 */
	private ILogicalQuery query;

	/**
	 * The name of the query
	 */
	private Resource name = null;

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
	 * Unique id of an ID. Used for identification of an query. Not final anymore,
	 * because there might arise the need to change it in rare cases. (i.e. creating
	 * a physical query based on a logical one and then later trying to construct
	 * one via a physical plan First one would take the logical query's ID, while
	 * the next constructor would start at the idCounter's current value)
	 */
	private int id;

	private AbstractMonitoringDataProvider mdP = new AbstractMonitoringDataProvider() {
	};

	/**
	 * List of all direct physical child operators. Stored separate because a root
	 * can contain operators which are part of an other query.
	 */
	transient private ArrayList<IPhysicalOperator> physicalChilds = new ArrayList<IPhysicalOperator>();

	/**
	 * Physical root operators of this query. Since we do not have trees any more,
	 * there can be more than one query.
	 */
	transient private List<IPhysicalOperator> roots;

	transient private List<IPhysicalOperator> doneRoots = new IdentityArrayList<>();

	/**
	 * Sources that should be scheduled.
	 */
	final private List<IIterableSource<?>> iterableSources = new ArrayList<IIterableSource<?>>();;

	/**
	 * Sources that are leafs
	 */
	final private List<IIterableSource<?>> iteratableLeafSources = new ArrayList<IIterableSource<?>>();

	final private List<IPhysicalOperator> leafSources = new ArrayList<>();

	/**
	 * Cache Ids for Sources to speed up getSourceID
	 */
	private Map<IIterableSource<?>, Integer> sourceIds;

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	transient private List<IQueryReoptimizeListener> queryReoptimizeListener = new ArrayList<IQueryReoptimizeListener>();

	/**
	 * List of rules for reoptimize requests.
	 */
	transient private List<AbstractQueryReoptimizeRule> queryReoptimizeRule = new ArrayList<AbstractQueryReoptimizeRule>();

	/**
	 * EventListener
	 */

	transient Map<String, IPOEventListener> poEventListener = new HashMap<String, IPOEventListener>();

	/**
	 * What monitors are installed on this plan
	 */
	transient public Map<String, IPlanMonitor<?>> planmonitors = new HashMap<String, IPlanMonitor<?>>();

	// /**
	// * Is the query running (open is called already)
	// */
	// private boolean opened = false;
	private QueryState queryState = QueryState.INACTIVE;
	private long queryStateChangeTS = System.currentTimeMillis(); // INACTIVE is also a state change...
	private long queryStartedTS = -1;

	/**
	 * Who has send the query
	 */
	private ISession user;

	/**
	 * To avoid dependencies, some values are only set as key value pairs
	 */
	final private Map<String, Object> parameters = new HashMap<String, Object>();

	private IQueryStarter queryListener;

	boolean markedToStopped = false;

	boolean isStarting = false;

	/**
	 * Notice can be used to annotade a query
	 */
	private String notice;

	final boolean acQuery;

	private int sheddingFactor;

	/**
	 * Creates a query based on a physical plan and {@link QueryBuildConfiguration}
	 *
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public PhysicalQuery(List<IPhysicalOperator> physicalPlan) {
		id = idCounter++;
		acQuery = false;
		initializePhysicalRoots(physicalPlan);
		determineIteratableSourcesAndLeafs(physicalPlan);
	}

	/**
	 * Create a new physical query
	 *
	 * @param query
	 *            The logical query that is the origin of the query
	 * @param physicalPlan
	 *            The physical plan
	 */
	public PhysicalQuery(ILogicalQuery query, ArrayList<IPhysicalOperator> physicalPlan) {
		// logical and physical query must have the same id!
		id = query.getID();
		this.query = query;
		this.user = query.getUser();
		this.basePriority = query.getPriority();
		this.currentPriority = query.getPriority();
		this.containsCycles = query.containsCycles();
		Object parameter = getLogicalQuery().getServerParameter(ACQueryParameter.class.getSimpleName());
		if (parameter != null && parameter instanceof ACQueryParameter) {
			this.acQuery = ((ACQueryParameter) parameter).getValue();
		} else {
			this.acQuery = false;
		}
		initializePhysicalRoots(physicalPlan);
		determineIteratableSourcesAndLeafs(physicalPlan);
	}

	/**
	 * Some operators need to be scheduled typically buffers To allow other
	 * processing of operators that are sources these iteratableleafSources are
	 * treated different
	 *
	 * @param physicalPlan
	 */
	private void determineIteratableSourcesAndLeafs(List<IPhysicalOperator> physicalPlan) {
		List<IPhysicalOperator> queryOps = new ArrayList<IPhysicalOperator>(getPhysicalChilds());
		queryOps.addAll(getRoots());
		iterableSources.clear();
		iteratableLeafSources.clear();
		leafSources.clear();
		Set<IOperatorOwner> owners = new HashSet<IOperatorOwner>();

		for (IPhysicalOperator operator : queryOps) {
			owners.addAll(operator.getOwner());
			IIterableSource<?> iterableSource = null;
			if (operator instanceof IIterableSource) {
				iterableSource = (IIterableSource<?>) operator;
				// IterableSource is a Pipe
				if (iterableSource.isSink() && !iterableSources.contains(iterableSource)) {
					iterableSources.add(iterableSource);
				} else if (!iterableSource.isSink() // IterableSource
													// is a
													// global Source
						&& !iteratableLeafSources.contains(iterableSource)) {
					iteratableLeafSources.add(iterableSource);
				}
			}
			// Determined leafSources
			if (!operator.hasInput()) {
				leafSources.add(operator);
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
	public Resource getName() {
		if (query != null) {
			return query.getName();
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#getRoot
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
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#setRoot
	 * (de.uniol.inf.is.odysseus.core.server.IPhysicalOperator)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<IPhysicalOperator> setRoots(List<IPhysicalOperator> roots) {
		List<IPhysicalOperator> newRoots = new ArrayList<>();
		for (IPhysicalOperator p : roots) {
			// there are sometimes cases where root operators are not really root operators
			// (e.g. when using append_to in PQL)
			if (p instanceof ISource) {
				if (((ISource) p).getSubscriptions() != null && ((ISource) p).getSubscriptions().size() > 0) {
					continue;
				}
			}
			newRoots.add(p);
		}
		this.roots = newRoots;
		return this.roots;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery# getPhysicalChilds()
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
		// set root of this query
		setRoots(roots);

		this.physicalChilds.clear();
		// Store each child in a list. And set this Query as owner of each child
		for (IPhysicalOperator root : roots) {
			// addPhysicalChildren(GraphHelper.getChildren(root));
			addPhysicalChildren(getChildren(root));
		}
		determineIteratableSourcesAndLeafs(roots);

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
				Collection<AbstractPhysicalSubscription<? extends ISource<?>,?>> subsriptions = ((ISink) curOp)
						.getSubscribedToSource();
				for (AbstractPhysicalSubscription<? extends ISource<?>,?> subscription : subsriptions) {
					ISource<?> target = subscription.getSource();
					if (!visitedOps.contains(target)) {
						operators.push(target);
					}
				}
			}
		}

		return children;
	}

	private List<IPhysicalOperator> getDeepestNonSharedOperators() {
		List<IPhysicalOperator> pos = new ArrayList<IPhysicalOperator>();
		for (IPhysicalOperator s : getLeafSources()) {
			pos.addAll(getNonSharedFathers(s));
		}
		return pos;
	}

	private List<? extends IPhysicalOperator> getNonSharedFathers(IPhysicalOperator s) {
		List<IPhysicalOperator> pos = new ArrayList<IPhysicalOperator>();
		if (s.getOwner().size() == 1 && s.isSink()) {
			pos.add(s);
		} else {
			if (s instanceof ISource) {
				for (AbstractPhysicalSubscription<?, ?> father : ((ISource<?>)s).getSubscriptions()) {
					pos.addAll(getNonSharedFathers(father.getSink()));
				}
			}
		}
		return pos;
	}

	/**
	 * Sets the physical children of this query. These children are the physical
	 * operators which are necessary for the execution of this query. It also sets
	 * the owner relationship between the query and the operators.
	 *
	 * This method should be only used if special optimizations are processed. For
	 * initial setting the physical plan use
	 * {@link #initializePhysicalPlan(IPhysicalOperator)}.
	 *
	 * @param physicalChilds
	 *            Physical operators which are necessary for the execution of this
	 *            query.
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
	 * replace an physical operator of the query plan with another operator operator
	 * subscription are not touched, so correct subscriptions need to be set before
	 * replacement
	 *
	 * @param oldOp
	 * @param newOp
	 */
	@Override
	public void replaceOperator(IPhysicalOperator oldOp, IPhysicalOperator newOp) {
		if (removeChild(oldOp)) {
			addChild(newOp);
		} // TODO: Exception werfen?
	}

	/**
	 * Replaces a Root in the Query with another Physical Operator (Has no effect,
	 * if the oldRoot-argument is no root for this Query)
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
			ArrayList<IPhysicalOperator> oldRoots = new ArrayList<IPhysicalOperator>(this.roots);
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
		Set<IPhysicalOperator> ops2 = otherQuery.getAllOperators();

		ops1.retainAll(ops2);

		return ops1;
	}

	@Override
	public IPhysicalOperator getOperator(String name) {
		Set<IPhysicalOperator> allOps = this.getAllOperators();
		for (IPhysicalOperator p : allOps) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery# removeOwnerschip()
	 */
	@Override
	public void removeOwnerschip() {

		_logger.debug("Remove ownership start");
		for (IPhysicalOperator physicalOperator : this.physicalChilds) {
			_logger.debug("Remove Ownership for " + physicalOperator);
			physicalOperator.removeOwner(this);
		}
	}

	@Override
	public void start(IQueryStarter queryListener) throws OpenFailedException {
		try {
			QueryState nextState = QueryState.next(queryState, QueryFunction.START);

			this.markedToStopped = false;
			this.isStarting = true;
			doneRoots.clear();
			this.queryListener = queryListener;
			_logger.debug("Calling open on query " + getID());
			for (IPhysicalOperator curRoot : getRoots()) {
				// this also works for cyclic plans,
				// since if an operator is already open/started, the
				// following roots will not be called any more.
				curRoot.open(this);
			}
			_logger.debug("Calling start on query " + getID());
			queryStartedTS = System.currentTimeMillis();
			for (IPhysicalOperator curRoot : getRoots()) {
				// this also works for cyclic plans,
				// since if an operator is already open/started, the
				// following roots will not be called any more.
				curRoot.start(this);
			}
			_logger.debug("Query " + getID() + " started.");
			setState(nextState);
			this.isStarting = false;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void suspend() {
		try {
			QueryState nextState = QueryState.next(queryState, QueryFunction.SUSPEND);
			for (IPhysicalOperator curRoot : getRoots()) {
				// this also works for cyclic plans,
				// since if an operator is already open, the
				// following sources will not be called any more.
				if (curRoot.isSink()) {
					((ISink<?>) curRoot).suspend(this);
				}

			}
			setState(nextState);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resume() {
		try {
			QueryState nextState = QueryState.next(queryState, QueryFunction.RESUME);
			for (IPhysicalOperator curRoot : getRoots()) {
				// this also works for cyclic plans,
				// since if an operator is already open, the
				// following sources will not be called any more.
				if (curRoot.isSink()) {
					((ISink<?>) curRoot).resume(this);
				}
			}
			setState(nextState);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void partial(int sheddingFactor) {
		try {
			this.sheddingFactor = sheddingFactor;
			final QueryState nextState;
			if (sheddingFactor > 0) {
				nextState = QueryState.next(queryState, QueryFunction.PARTIAL);
			} else {
				nextState = QueryState.next(queryState, QueryFunction.FULL);
			}
			for (IPhysicalOperator leaf : getDeepestNonSharedOperators()) {
				((ISink<?>) leaf).partial(this, sheddingFactor);
			}
			setState(nextState);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		try {
			QueryState nextState = QueryState.next(queryState, QueryFunction.STOP);
			for (IPhysicalOperator curRoot : getRoots()) {
				// this also works for cyclic plans,
				// since if an operator is already closed, the
				// following sources will not be called any more.
				curRoot.close(this);
			}
			setState(nextState);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void done(IOwnedOperator op) {
		IPhysicalOperator po = (IPhysicalOperator) op;
		if (roots.contains(po) && !doneRoots.contains(po)) {
			doneRoots.add(po);
			if (doneRoots.size() == roots.size() && queryListener != null) {
				queryListener.done(this);
			}
		}
	}

	@Override
	public boolean isOpened() {
		return this.queryState != QueryState.INACTIVE;
	}

	@Override
	public boolean isStarting() {
		return isStarting;
	}

	@Override
	public QueryState getState() {
		return queryState;
	}

	protected final void setState(QueryState state) {
		queryState = state;

		queryStateChangeTS = System.currentTimeMillis();
	}

	@Override
	public final long getLastQueryStateChangeTS() {
		return queryStateChangeTS;
	}

	@Override
	public final long getQueryStartTS() {
		return queryStartedTS;
	}

	@Override
	public int getSheddingFactor() {
		return sheddingFactor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester
	 * #reoptimize ()
	 */
	@Override
	public void reoptimize() {
		// FIXME: Reoptimization currently not supported
		throw new IllegalArgumentException("Reoptimization currently not implemented");
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
	public void addReoptimizeListener(IQueryReoptimizeListener reoptimizationListener) {
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
	public void removeReoptimizeListener(IQueryReoptimizeListener reoptimizationListener) {
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
	public void createAndAddMonitoringData(@SuppressWarnings("rawtypes") IPeriodicalMonitoringData item, long period) {
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
		if (session == null || user == null) {
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
	public String getQueryText() {
		if (query != null) {
			return query.getQueryText();
		}
		return null;
	}

	@Override
	public String getParserId() {
		if (query != null) {
			return query.getParserId();
		}
		return null;
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

	// /-------------------------------------------------------
	// Iteratable Sources for Scheduling
	// -------------------------------------------------------
	@Override
	public boolean hasIteratableSources() {
		return iterableSources != null && iterableSources.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seede.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IPhysicalQuery
	 * # getIterableSource()
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
	public List<IIterableSource<?>> getIteratableLeafSources() {
		return Collections.unmodifiableList(iteratableLeafSources);
	}

	@Override
	public List<IPhysicalOperator> getLeafSources() {
		return Collections.unmodifiableList(leafSources);
	}

	@Override
	public String toString() {
		return "PQuery Id " + getID();
	}

	@Override
	public int compareTo(IOperatorOwner query) {
		if (this.id < query.getID()) {
			return -1;
		}
		if (this.id > query.getID()) {
			return 1;
		}
		return 0;
	}

	@Override
	public void setLogicalQuery(ILogicalQuery q) {
		this.query = q;
	}

	@Override
	public void setLogicalQueryAndAdoptItsID(ILogicalQuery q) {
		this.query = q;
		this.id = q.getID();
	}

	@Override
	public synchronized boolean isMarkedAsStopping() {
		return markedToStopped;
	}

	@Override
	public synchronized void setAsStopping(boolean isStopping) {
		this.markedToStopped = isStopping;
	}

	@Override
	public String getNotice() {
		return query != null ? query.getNotice() : notice;
	}

	@Override
	public void setNotice(String notice) {
		if (query != null) {
			query.setNotice(notice);
		} else {
			this.notice = notice;
		}
	}

	@Override
	public boolean isACquery() {
		return this.acQuery;
	}
	
	// ----------------------------------------------
	// Moved from PhysicalRestructHelper
	// ----------------------------------------------
	
	/**
	 * Appends the binaryOp to both childs with input ports 0 -> child1, 1 -> child2.
	 * Happens via subscribeToSource.
	 * 
	 * @param binaryOp
	 * @param child1
	 * @param child2
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void appendBinaryOperator(IPhysicalOperator binaryOp, ISource child1, ISource child2) {
		((ISink<?>)binaryOp).subscribeToSource(child1, 0, 0, child1.getOutputSchema());
		((ISink<?>)binaryOp).subscribeToSource(child2, 1, 0, child2.getOutputSchema());
	}
	
	/**
	 * Appends the parent to the child via subscribeToSource.
	 * 
	 * @param parent
	 * @param child
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void appendOperator(IPhysicalOperator parent, ISource child) {
		((ISink<?>)parent).subscribeToSource(child, 0, 0, child.getOutputSchema());		
	}
	
	/**
	 * Unsubscribes the the parent from the child and returns the subscription.
	 * 
	 * @param parent
	 * @param child
	 * @return Subscription parent -> child
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static AbstractPhysicalSubscription<?,?> removeSubscription(IPhysicalOperator parent, IPhysicalOperator child) {
		for (AbstractPhysicalSubscription<?,?> sub : ((ISink<?>)parent).getSubscribedToSource()) {
			if (sub.getSource().equals(child)) {
				((ISink<?>)parent).unsubscribeFromSource((AbstractPhysicalSubscription)sub);
				return sub;
			}
		}
		return null;
	}

	/**
	 * Remove the operator which is between source and sinks.
	 * @param source
	 * @param sourceOutPort
	 * @param sinks
	 * @param sinkInPorts
	 * @param buffer
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends IPipe> void removeOperator(ISource source,
			int sourceOutPort, List<ISink> sinks, List<Integer> sinkInPorts,
			T buffer) {
		if (sinks.size() != sinkInPorts.size()) {
			throw new IllegalArgumentException(
					"Amount of sinks and sinkinports must be equal");
		}

		// disconnect old connections
		for (int i = 0; i < sinks.size(); i++) {
			buffer.unsubscribeSink(sinks.get(i), sinkInPorts.get(i), 0, buffer.getOutputSchema());
		}
		
		for (int i = 0; i < sinks.size(); i++) {
			source.subscribeSink(sinks.get(i), sinkInPorts.get(i),
					sourceOutPort, source.getOutputSchema());
		}
		
		Set<AbstractPhysicalSubscription<?,?>> toSinks = new HashSet<AbstractPhysicalSubscription<?,?>>();
		Set<AbstractPhysicalSubscription<?,?>> toBuffer = new HashSet<AbstractPhysicalSubscription<?,?>>();
		for(AbstractPhysicalSubscription<?,?> sub : ((ISource<?>)source).getSubscriptions()) {
			if(sub.getSink().equals(buffer)) {
				toBuffer.add(sub);
				continue;
			}
			if(sinks.contains(sub.getSink())) {
				toSinks.add(sub);
			}
		}
		((AbstractSource) source).replaceActiveSubscriptions(toBuffer, toSinks);
		
		source.unsubscribeSink(buffer, 0, sourceOutPort, source.getOutputSchema());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void replaceChild(IPhysicalOperator parent, IPhysicalOperator child, IPhysicalOperator newChild) {
		AbstractPhysicalSubscription<?,?> sub = removeSubscription(parent, child);
		((ISink<?>)parent).subscribeToSource((ISource)newChild, sub.getSinkInPort(), sub.getSourceOutPort(),
				newChild.getOutputSchema());
	}
}
