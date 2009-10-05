package de.uniol.inf.is.odysseus.planmanagement.executor.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.query.AbstractQueryReoptimizeRule;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQueryReoptimizeListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;

/**
 * Query is a standard implementation of a query in odysseus. Each query has an
 * unique ID and stores all relevant data (logical/physical plan, parser ID,
 * execution state etc.).
 * 
 * @author Wolf Bauer
 * 
 */
/**
 * @author Wolf Bauer
 * 
 */
public class Query implements IEditableQuery {
	/**
	 * Counter for ID creation.
	 */
	private static int idCounter = 0;

	/**
	 * Unique id of an ID. Used for identification of an query.
	 */
	private final int id;

	/**
	 * List of all direct physical child operators. Stored separate because a
	 * root can contain operators which are part of an other query.
	 */
	private ArrayList<IPhysicalOperator> physicalChilds = new ArrayList<IPhysicalOperator>();

	/**
	 * Physical root operator of this query.
	 */
	private IPhysicalOperator root;

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

	/**
	 * Creates a query based on a physical plan and {@link QueryBuildParameter}
	 * 
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildParameter} for creating the query
	 */
	public Query(IPhysicalOperator physicalPlan, QueryBuildParameter parameters) {
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
			IPhysicalOperator physicalPlan, QueryBuildParameter parameters) {
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
			initializePhysicalPlan(physicalPlan);
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
		info += "PhysicalAlgebra:" + AppEnv.LINE_SEPARATOR + this.root;
		return info;
	}

	/**
	 * Get all physical children of a {@link ISource}.
	 * 
	 * @param source
	 *            root of physical plan.
	 * @return all physical children of source.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<IPhysicalOperator> getChildren(ISource<?> source) {
		ArrayList<IPhysicalOperator> ret = new ArrayList<IPhysicalOperator>();

		Stack<ISource<?>> sources = new Stack<ISource<?>>();
		sources.push(source);
		while (!sources.isEmpty()) {
			ISource<?> curSource = sources.pop();
			if (curSource instanceof ISource<?>) {
				ret.add(curSource);
			}
			if (curSource instanceof IPipe) {
				IPipe<?, ?> pipe = (IPipe<?, ?>) curSource;
				for (Subscription<? extends ISource<?>> subscription : pipe
						.getSubscribedTo()) {
					sources.push(subscription.target);
				}
			}
		}
		return ret;
	}

	/**
	 * Get all physical children of a {@link ISink}.
	 * 
	 * @param sink
	 *            root of physical plan.
	 * @return all physical children of source.
	 */
	private ArrayList<IPhysicalOperator> getChildren(ISink<?> sink) {
		if (sink instanceof ISource<?>) {
			return getChildren((ISource<?>) sink);
		}
		List<? extends Subscription<? extends ISource<?>>> slist = sink
				.getSubscribedTo();
		ArrayList<IPhysicalOperator> ret = new ArrayList<IPhysicalOperator>();
		for (Subscription<? extends ISource<?>> s : slist) {
			ret.addAll(getChildren(s.target));
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#
	 * setLogicalPlan(de.uniol.inf.is.odysseus.base.ILogicalOperator)
	 */
	@Override
	public void setLogicalPlan(ILogicalOperator logicalPlan) {
		this.logicalPlan = logicalPlan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#
	 * getSealedLogicalPlan()
	 */
	@Override
	public ILogicalOperator getSealedLogicalPlan() {
		return this.logicalPlan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#setRoot
	 * (de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator setRoot(IPhysicalOperator root) {
		this.root = root;

		// evaluate built parameter
		if (this.parameters != null) {
			IPhysicalOperator defaultRoot = null;

			// set default root (e. g. defined Sinks)
			if (this.parameters.getDefaultRoot() != null) {
				defaultRoot = this.parameters.getDefaultRoot();

				// register default root
				if (defaultRoot != null && defaultRoot.isSink()
						&& root.isSource()) {
					((ISink) defaultRoot).subscribeTo((ISource) root, 0,0);
				}
			}
		}

		return this.root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery#getSealedRoot()
	 */
	@Override
	public IPhysicalOperator getSealedRoot() {
		return this.root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#
	 * getIntialPhysicalPlan()
	 */
	@Override
	public ArrayList<IPhysicalOperator> getIntialPhysicalPlan() {
		return this.physicalChilds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#
	 * initializePhysicalPlan(de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	@Override
	public void initializePhysicalPlan(IPhysicalOperator root) {
		// set root of this querie
		setRoot(root);
		// FIXME: Hier sollte in Zukunft mit Strategien gearbeitet werden
		// Store each child in a list. And set this Query as owner of each child
		if (this.root.isSink()) {
			setPhysicalChilds(getChildren((ISink<?>) this.root));
		} else {
			setPhysicalChilds(getChildren((ISource<?>) this.root));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#
	 * setPhysicalChilds(java.util.ArrayList)
	 */
	@Override
	public void setPhysicalChilds(
			ArrayList<IPhysicalOperator> initialPhysicalPlan) {
		synchronized (this.physicalChilds) {
			// Store children.
			this.physicalChilds = new ArrayList<IPhysicalOperator>(
					initialPhysicalPlan);
			// Set this query as owner of each child.
			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				physicalOperator.addOwner(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#
	 * removeOwnerschip()
	 */
	@Override
	public void removeOwnerschip() {
		for (IPhysicalOperator physicalOperator : this.physicalChilds) {
			physicalOperator.removeOwner(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#start()
	 */
	@Override
	public void start() {
		synchronized (this.physicalChilds) {
			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				if (physicalOperator instanceof IIterableSource<?>) {
					((IIterableSource<?>)physicalOperator).activateRequest(this);
				}
			}
		}
		this.started = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#stop()
	 */
	@Override
	public void stop() {
		synchronized (this.physicalChilds) {
			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				if (physicalOperator instanceof IIterableSource<?>) {
					((IIterableSource<?>)physicalOperator).deactivateRequest(this);
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
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#
	 * getLogicalPlan()
	 */
	@Override
	public ILogicalOperator getLogicalPlan() {
		return this.logicalPlan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#getRoot
	 * ()
	 */
	@Override
	public IPhysicalOperator getRoot() {
		return this.root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery#
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
	 * de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl#isStarted()
	 */
	@Override
	public boolean isStarted() {
		return this.started;
	}
}
