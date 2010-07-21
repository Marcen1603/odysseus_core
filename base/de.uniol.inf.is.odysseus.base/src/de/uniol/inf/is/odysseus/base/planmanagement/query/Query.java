package de.uniol.inf.is.odysseus.base.planmanagement.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

/**
 * Query is a standard implementation of a query in odysseus. Each query has an
 * unique ID and stores all relevant data (logical/physical plan, parser ID,
 * execution state etc.).
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class Query implements IQuery {

	protected Logger _logger = null;

	protected Logger getLogger() {
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
	 * Indicates if this query is started.
	 */
	private boolean started;

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
	private QueryBuildParameter parameters = new QueryBuildParameter();

	public Query() {
		this("", null, null, null);
	}

	/**
	 * Creates a query based on a physical plan and {@link QueryBuildParameter}
	 * 
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildParameter} for creating the query
	 */
	public Query(List<IPhysicalOperator> physicalPlan,
			QueryBuildParameter parameters) {
		this("", null, physicalPlan, parameters);
	}

	/**
	 * Creates a query based on a parser ID and {@link QueryBuildParameter}
	 * 
	 * @param parserID
	 *            ID of the parser to use
	 * @param parameters
	 *            {@link QueryBuildParameter} for creating the query
	 */
	public Query(String parserID, QueryBuildParameter parameters) {
		this(parserID, null, null, parameters);
	}

	/**
	 * Creates a query based on a logical plan and {@link QueryBuildParameter}
	 * 
	 * @param logicalPlan
	 *            logical operator plan
	 * @param parameters
	 *            {@link QueryBuildParameter} for creating the query
	 */
	public Query(ILogicalOperator logicalPlan, QueryBuildParameter parameters) {
		this("", logicalPlan, null, parameters);
	}

	/**
	 * Creates a query based on a parserID, a logical plan, a physical plan and
	 * {@link QueryBuildParameter}
	 * 
	 * @param parserID
	 *            logical operator plan
	 * @param logicalPlan
	 *            logical operator plan
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildParameter} for creating the query
	 */
	private Query(String parserID, ILogicalOperator logicalPlan,
			List<IPhysicalOperator> physicalPlan, QueryBuildParameter parameters) {
		this.id = idCounter++;
		this.started = true;
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
		info += "Started:" + this.started;
		info += "CompileLanguage:" + this.parserID;
		info += "LogicalAlgebra:" + AppEnv.LINE_SEPARATOR + this.logicalPlan;
		info += "PhysicalAlgebra:" + AppEnv.LINE_SEPARATOR + this.roots;
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#
	 * setLogicalPlan(de.uniol.inf.is.odysseus.base.ILogicalOperator)
	 */
	@Override
	public void setLogicalPlan(ILogicalOperator logicalPlan) {
		this.logicalPlan = logicalPlan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#setRoot
	 * (de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	@Override
	public List<IPhysicalOperator> setRoots(List<IPhysicalOperator> roots) {
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
								.subscribeDefaultRootToSource((ISink<?>) defaultRoot,
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
				this.roots = newRoots;
			}
		}

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
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#
	 * getIntialPhysicalPlan()
	 */
	@Override
	public ArrayList<IPhysicalOperator> getIntialPhysicalPlan() {
		return this.physicalChilds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#
	 * initializePhysicalPlan(de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	@Override
	public void initializePhysicalRoots(List<IPhysicalOperator> roots) {
		// set root of this querie
		setRoots(roots);

		// FIXME: Hier sollte in Zukunft mit Strategien gearbeitet werden
		// Store each child in a list. And set this Query as owner of each child
		for (IPhysicalOperator root : roots) {
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
			visitedOps.add(root);
			if (curOp.isSink()) {
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
				if (!this.physicalChilds.contains(child)) {
					this.physicalChilds.add(child);
					child.addOwner(this);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#
	 * removeOwnerschip()
	 */
	@Override
	public void removeOwnerschip() {
		getLogger().debug("Remove ownership start");
		for (IPhysicalOperator physicalOperator : this.physicalChilds) {
			getLogger().debug("Remove Ownership for " + physicalOperator);
			physicalOperator.removeOwner(this);
			if (!physicalOperator.hasOwner()) {
				getLogger()
						.debug("No more owners. Closing " + physicalOperator);
				physicalOperator.close();
				if (physicalOperator.isSink()) {
					getLogger().debug(
							"Sink unsubscribe from all sources "
									+ physicalOperator);
					ISink<?> sink = (ISink<?>) physicalOperator;
					sink.unsubscribeFromAllSources();
				}
			}
		}
		WrapperPlanFactory.removeClosedSources();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#start()
	 */
	@Override
	public void start() {
		synchronized (this.physicalChilds) {
			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				if (physicalOperator instanceof IIterableSource<?>) {
					((IIterableSource<?>) physicalOperator)
							.activateRequest(this);
				}
			}
		}
		this.started = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#stop()
	 */
	@Override
	public void stop() {
		synchronized (this.physicalChilds) {
			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				if (physicalOperator instanceof IIterableSource<?>) {
					((IIterableSource<?>) physicalOperator)
							.deactivateRequest(this);
				}
			}
		}
		this.started = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester#reoptimize
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
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler#
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
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler#
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
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester#
	 * addReoptimzeRule
	 * (de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRule)
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
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester#
	 * removeReoptimzeRule
	 * (de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRule)
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
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#getPriority()
	 */
	@Override
	public int getPriority() {
		return this.priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#setPriority
	 * (int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#
	 * getLogicalPlan()
	 */
	@Override
	public ILogicalOperator getLogicalPlan() {
		return this.logicalPlan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#getRoot ()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		return this.roots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#
	 * getBuildParameter()
	 */
	@Override
	public QueryBuildParameter getBuildParameter() {
		return this.parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IOperatorOwner#getID()
	 */
	@Override
	public int getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl#�sRunning()
	 */
	@Override
	public boolean isRunning() {
		return this.started;
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
	public void setBuildParameter(QueryBuildParameter parameter) {
		this.parameters = parameter;
	}
}
