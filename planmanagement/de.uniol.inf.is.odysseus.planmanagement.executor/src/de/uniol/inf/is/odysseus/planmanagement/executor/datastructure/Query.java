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
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;

public class Query implements IEditableQuery {
	private static int idCounter = 0;

	private final int id;

	private ArrayList<IPhysicalOperator> physicalChilds = new ArrayList<IPhysicalOperator>();

	private IPhysicalOperator root;

	private String compilerID;

	private ILogicalOperator logicalPlan;

	private boolean started;

	private ArrayList<IQueryReoptimizeListener> queryReoptimizeListener = new ArrayList<IQueryReoptimizeListener>();

	private ArrayList<AbstractQueryReoptimizeRule> queryReoptimizeRule = new ArrayList<AbstractQueryReoptimizeRule>();

	private int priority = 0;

	private QueryBuildParameter parameters = new QueryBuildParameter();

	public Query(IPhysicalOperator physicalPlan, QueryBuildParameter parameters) {
		this("", null, physicalPlan, parameters);
	}

	public Query(String compilerID, QueryBuildParameter parameters) {
		this(compilerID, null, null, parameters);
	}

	public Query(ILogicalOperator logicalPlan, QueryBuildParameter parameters) {
		this("", logicalPlan, null, parameters);
	}

	private Query(String compilerID, ILogicalOperator logicalPlan,
			IPhysicalOperator physicalPlan, QueryBuildParameter parameters) {
		this.id = idCounter++;
		this.started = true;
		this.parameters = parameters;
		this.compilerID = compilerID;
		this.logicalPlan = logicalPlan;
		this.parameters = parameters;

		if (this.parameters != null) {
			if (this.parameters.getPriority() != null) {
				this.priority = this.parameters.getPriority();
			}
		}

		if (physicalPlan != null) {
			initializePhysicalPlan(physicalPlan);
		}
	}

	public String getDebugInfo() {
		String info = "";
		info += "ID:" + this.id;
		info += "Started:" + this.started;
		info += "CompileLanguage:" + this.compilerID;
		info += "LogicalAlgebra:" + AppEnv.LINE_SEPERATOR + this.logicalPlan;
		info += "PhysicalAlgebra:" + AppEnv.LINE_SEPERATOR + this.root;
		return info;
	}

	public int getID() {
		return this.id;
	}

	public boolean isStarted() {
		return this.started;
	}

	@Override
	public void setLogicalPlan(ILogicalOperator logicalPlan) {
		this.logicalPlan = logicalPlan;
	}

	@Override
	public ILogicalOperator getSealedLogicalPlan() {
		return this.logicalPlan;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator setRoot(IPhysicalOperator root) {
		this.root = (IPhysicalOperator) root.clone();

		if (this.parameters != null) {
			IPhysicalOperator defaultRoot = null;
			if (this.parameters.getDefaultRoot() != null) {
				defaultRoot = this.parameters.getDefaultRoot();
				
				if (defaultRoot != null && defaultRoot.isSink()
						&& root.isSource()) {
					((ISink) defaultRoot).subscribeTo((ISource) root, 0);
				}
			}
		}

		return this.root;
	}

	@Override
	public IPhysicalOperator getSealedRoot() {
		return this.root;
	}

	@Override
	public ArrayList<IPhysicalOperator> getIntialPhysicalPlan() {
		return this.physicalChilds;
	}

	@Override
	public void initializePhysicalPlan(IPhysicalOperator root) {
		setRoot(root);
		// FIXME: Hier sollte in Zukunft mit Strategien gearbeitet werden
		if (this.root.isSink()) {
			setPhysicalChilds(getChidren((ISink<?>) this.root));
		} else {
			setPhysicalChilds(getChidren((ISource<?>) this.root));
		}
	}

	@Override
	public void setPhysicalChilds(
			ArrayList<IPhysicalOperator> initialPhysicalPlan) {
		synchronized (this.physicalChilds) {
			this.physicalChilds = new ArrayList<IPhysicalOperator>(
					initialPhysicalPlan);

			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				physicalOperator.addOwner(this);
			}
		}
	}

	@Override
	public void removeOwnerschip() {
		for (IPhysicalOperator physicalOperator : this.physicalChilds) {
			physicalOperator.removeOwner(this);
		}
	}

	@Override
	public void start() {
		synchronized (this.physicalChilds) {
			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				physicalOperator.activateRequest(this);
			}
		}
		this.started = true;
	}

	@Override
	public void stop() {
		synchronized (this.physicalChilds) {
			for (IPhysicalOperator physicalOperator : this.physicalChilds) {
				physicalOperator.deactivateRequest(this);
			}
		}
		this.started = false;
	}

	@Override
	public void reoptimize() {
		for (IQueryReoptimizeListener reoptimizeListener : this.queryReoptimizeListener) {
			reoptimizeListener.reoptimizeRequest(this);
		}
	}

	@Override
	public void addReoptimizeListener(
			IQueryReoptimizeListener reoptimizationListener) {
		synchronized (this.queryReoptimizeListener) {
			if (!this.queryReoptimizeListener.contains(reoptimizationListener)) {
				this.queryReoptimizeListener.add(reoptimizationListener);
			}
		}
	}

	@Override
	public void removeReoptimizeListener(
			IQueryReoptimizeListener reoptimizationListener) {
		synchronized (this.queryReoptimizeListener) {
			this.queryReoptimizeListener.remove(reoptimizationListener);
		}
	}

	@Override
	public void addReoptimzeRule(AbstractQueryReoptimizeRule reoptimizeRule) {
		reoptimizeRule.addReoptimieRequester(this);
		synchronized (this.queryReoptimizeRule) {
			if (!this.queryReoptimizeRule.contains(reoptimizeRule)) {
				this.queryReoptimizeRule.add(reoptimizeRule);
			}
		}
	}

	@Override
	public void removeReoptimzeRule(AbstractQueryReoptimizeRule reoptimizeRule) {
		reoptimizeRule.removeReoptimieRequester(this);
		synchronized (this.queryReoptimizeRule) {
			this.queryReoptimizeRule.remove(reoptimizeRule);
		}
	}

	@Override
	public int getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<IPhysicalOperator> getChidren(ISource<?> source) {
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

	private ArrayList<IPhysicalOperator> getChidren(ISink<?> sink) {
		if (sink instanceof ISource<?>) {
			return getChidren((ISource<?>) sink);
		}
		List<? extends Subscription<? extends ISource<?>>> slist = sink
				.getSubscribedTo();
		ArrayList<IPhysicalOperator> ret = new ArrayList<IPhysicalOperator>();
		for (Subscription<? extends ISource<?>> s : slist) {
			ret.addAll(getChidren(s.target));
		}
		return ret;
	}

	@Override
	public ILogicalOperator getLogicalPlan() {
		return this.logicalPlan;
	}

	@Override
	public IPhysicalOperator getRoot() {
		return this.root;
	}

	@Override
	public QueryBuildParameter getBuildParameter() {
		return this.parameters;
	}
}
