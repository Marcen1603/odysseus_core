package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */
public abstract class AbstractLogicalOperator implements Serializable,
		ILogicalOperator {
	
	private static final long serialVersionUID = -4425148851059140851L;

	private ArrayList<IOperatorOwner> owner = new ArrayList<IOperatorOwner>();
	
	protected Map<Integer, LogicalSubscription> subscribedTo = new HashMap<Integer, LogicalSubscription>();
	protected Map<Integer, LogicalSubscription> subscriptions = new HashMap<Integer,LogicalSubscription>();;
	
	private Map<Integer, Subscription<ISource<?>>> physSubscriptionTo = new HashMap<Integer, Subscription<ISource<?>>>();
	// cache access to bounded physOperators
	private Map<Integer, ISource<?>> physInputOperators = new HashMap<Integer, ISource<?>>();
	
	private String name = null;

	private SDFAttributeList outputSchema = null;

	@SuppressWarnings("unchecked")
	private IPredicate predicate = null;;

	public AbstractLogicalOperator(AbstractLogicalOperator op) {
		this.subscribedTo = new HashMap<Integer, LogicalSubscription>(op.subscribedTo);
		this.subscriptions = new HashMap<Integer, LogicalSubscription>(op.subscriptions);
		outputSchema = new SDFAttributeList(op.outputSchema);
		predicate =  op.predicate;
		setName(op.getName());
		physSubscriptionTo = op.physSubscriptionTo == null ? null
				: new HashMap<Integer,Subscription<ISource<?>>>(op.physSubscriptionTo);
	}

	public AbstractLogicalOperator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#clone()
	 */
	public AbstractLogicalOperator clone() {
		AbstractLogicalOperator clone;
		try {
			clone = (AbstractLogicalOperator) super.clone();
			clone.subscribedTo = new HashMap<Integer, LogicalSubscription>(this.subscribedTo);
			clone.subscriptions = new HashMap<Integer, LogicalSubscription>(this.subscriptions);
			if (this.outputSchema != null)
				clone.outputSchema = this.outputSchema.clone();
			clone.physSubscriptionTo = new HashMap<Integer, Subscription<ISource<?>>>(
					this.physSubscriptionTo);
			clone.name = this.name;
			if (this.predicate != null)
				clone.predicate = this.predicate.clone();

			// TODO ueberall kopien von anlegen
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#
	 * getOutputSchema()
	 */
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#
	 * setOutputSchema
	 * (de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList)
	 */
	public void setOutputSchema(SDFAttributeList outElements) {
		this.outputSchema = outElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getPredicate
	 * ()
	 */
	@SuppressWarnings("unchecked")
	public IPredicate getPredicate() {
		return predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setPredicate
	 * (de.uniol.inf.is.odysseus.base.predicate.IPredicate)
	 */
	@SuppressWarnings("unchecked")
	public void setPredicate(IPredicate predicate) {
		this.predicate = predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getInputSchema
	 * (int)
	 */
	public SDFAttributeList getInputSchema(int pos) {
		LogicalSubscription s = subscribedTo.get(pos);
		SDFAttributeList ret =  s.getInputSchema();
		if (ret == null) {
			ILogicalOperator op = s.getTarget();
			if (op != null) {
				ret = op.getOutputSchema();
			}
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setInputSchema
	 * (int,
	 * de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList)
	 */
	public void setInputSchema(int pos, SDFAttributeList schema) {
		subscribedTo.get(pos).setInputSchema(schema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getPOName
	 * ()
	 */
	public String getName() {
		if (name == null) {
			return this.getClass().getSimpleName();
		} else {
			return name;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setPOName
	 * (java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setPhysInputPO
	 * (int, de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	public void setPhysSubscriptionTo(Subscription<ISource<?>> subscription) {
		this.physSubscriptionTo.put(subscription.getSinkPort(), subscription);
		this.physInputOperators.put(subscription.getSinkPort(), subscription.getTarget());
	}
	
	public void setPhysSubscriptionTo(ISource<?> op, int sinkPort, int sourcePort){
		setPhysSubscriptionTo(new Subscription<ISource<?>>(op, sinkPort, sourcePort));
	}
	
	public Collection<Subscription<ISource<?>>> getPhysSubscriptionsTo(){
		return physSubscriptionTo.values();
	}

	@Override
	public Collection<ISource<?>> getPhysInputPO() {
		return physInputOperators.values();
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getPhysInputPO
	 * (int)
	 */
	public Subscription<ISource<?>> getPhysSubscriptionTo(int port) {
		return this.physSubscriptionTo.get(port);
	}

	@Override
	public void addOwner(IOperatorOwner owner) {
		this.owner.add(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		this.owner.remove(owner);
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return this.owner.contains(owner);
	}

	@Override
	public boolean hasOwner() {
		return this.owner.size() > 0;
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return this.owner;
	}

	//"delegatable this", used for the delegate sink
	protected ILogicalOperator getInstance() {
		return this;
	}
	
	@Override
	public void subscribeTo(ILogicalOperator source, int sinkPort, int sourcePort) {
		LogicalSubscription sub = new LogicalSubscription(source, sinkPort, sourcePort);
		synchronized (this.subscribedTo) {
			if (!this.subscribedTo.containsKey(sinkPort)) {
				this.subscribedTo.put(sinkPort, sub);
				source.subscribe(getInstance(), sinkPort, sourcePort);
			}
		}
	}

	@Override
	public void unsubscribeSubscriptionTo(ILogicalOperator source, int sinkPort, int sourcePort) {
		if (this.subscribedTo.remove(sinkPort) != null) {
			source.unsubscribe(this, sinkPort, sourcePort);
		}
	}
	
	@Override
	public void unsubscribeTo(LogicalSubscription subscription) {
		unsubscribeSubscriptionTo(subscription.getTarget(), subscription.getSinkPort(), subscription.getSourcePort());
	}
		
	@Override
	final public Collection<LogicalSubscription> getSubscribedTo() {
		return this.subscribedTo.values();
	}
	
	@Override
	public LogicalSubscription getSubscribedTo(int i) {
		return this.subscribedTo.get(i);
	}
	
	@Override
	public Collection<LogicalSubscription> getSubscribedTo(ILogicalOperator a) {
		List<LogicalSubscription> subs = new ArrayList<LogicalSubscription>();
		for(LogicalSubscription l:subscribedTo.values()){
			if (l.getTarget() == a){
				subs.add(l);
			}
		}
		return subs;
	}

	
	@Override
	final public void subscribe(ILogicalOperator sink, int sinkPort, int sourcePort) {
		LogicalSubscription sub = new LogicalSubscription(
				sink, sinkPort, sourcePort);
		synchronized (this.subscriptions) {
			if (!this.subscriptions.containsKey(sinkPort)) {
				this.subscriptions.put(sinkPort, sub);
				sink.subscribeTo(this, sinkPort, sourcePort);
			}
		}
	}
	
	@Override
	final public void unsubscribe(ILogicalOperator sink, int sinkPort, int sourcePort) {
		synchronized (this.subscriptions) {
			if (this.subscriptions.remove(sinkPort) != null) {
				sink.unsubscribeSubscriptionTo(this, sinkPort, sourcePort);
			}
		}
	}
	
	@Override
	public void unsubscribe(LogicalSubscription subscription) {
		unsubscribe(subscription.getTarget(), subscription.getSinkPort(), subscription.getSourcePort());
	}

	@Override
	final public Collection<LogicalSubscription> getSubscribtions() {
		synchronized (this.subscriptions) {
			return this.subscriptions.values();
		}
	}

	@Override
	public Collection<LogicalSubscription> getSubscribtions(ILogicalOperator a) {
		List<LogicalSubscription> subs = new ArrayList<LogicalSubscription>();
		for(LogicalSubscription l:subscriptions.values()){
			if (l.getTarget() == a){
				subs.add(l);
			}
		}
		return subs;
	}
	
	@Override
	public int getNumberOfInputs() {
		return this.subscribedTo.size();
	}
		
}
