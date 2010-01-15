package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
	protected Vector<LogicalSubscription> subscriptions = new Vector<LogicalSubscription>();;
	
	protected boolean recalcOutputSchemata = false;
	
	private Map<Integer, Subscription<ISource<?>>> physSubscriptionTo = new HashMap<Integer, Subscription<ISource<?>>>();
	// cache access to bounded physOperators
	private Map<Integer, ISource<?>> physInputOperators = new HashMap<Integer, ISource<?>>();
	
	private String name = null;

	@SuppressWarnings("unchecked")
	private IPredicate predicate = null;;

	public AbstractLogicalOperator(AbstractLogicalOperator op) {
		this.subscribedTo = new HashMap<Integer, LogicalSubscription>(op.subscribedTo);
		this.subscriptions = new Vector<LogicalSubscription>(op.subscriptions);
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
	@Override
	public AbstractLogicalOperator clone() {
		AbstractLogicalOperator clone;
		try {
			clone = (AbstractLogicalOperator) super.clone();
			clone.subscribedTo = new HashMap<Integer, LogicalSubscription>(this.subscribedTo);
			clone.subscriptions = new Vector<LogicalSubscription>(this.subscriptions);
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
		SDFAttributeList ret = null;
		if (s!=null){
			ret =  s.getSchema();
			if (ret == null) {
				ILogicalOperator op = s.getTarget();
				if (op != null) {
					ret = op.getOutputSchema();
				}
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
//	public void setInputSchema(int pos, SDFAttributeList schema) {
//		subscribedTo.get(pos).setInputSchema(schema);
//		recalcOutputSchemata = true;
//	}

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

	@Override
	public boolean isAllPhysicalInputSet() {
		for(Integer i : this.subscribedTo.keySet()){
			if (this.physInputOperators.get(i) == null ) {
				return false;
			}
		}
		return true;
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
	
	public void setPhysSubscriptionTo(ISource<?> op, int sinkPort, int sourcePort, SDFAttributeList schema){
		setPhysSubscriptionTo(new Subscription<ISource<?>>(op, sinkPort, sourcePort, schema));
	}
	
	public Collection<Subscription<ISource<?>>> getPhysSubscriptionsTo(){
		return physSubscriptionTo.values();
	}

	@Override
	public Collection<ISource<?>> getPhysInputPOs() {
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
	public void subscribeToSource(ILogicalOperator source, int sinkPort,
			int sourcePort, SDFAttributeList inputSchema) {	
		LogicalSubscription sub = new LogicalSubscription(source, sinkPort, sourcePort, inputSchema);
		synchronized (this.subscribedTo) {
			if (!this.subscribedTo.containsKey(sinkPort)) {
				this.subscribedTo.put(sinkPort, sub);
				source.subscribeSink(getInstance(), sinkPort, sourcePort, inputSchema);
				recalcOutputSchemata = true;
			}
		}
		
	}
	
	@Override
	public void unsubscribeFromSource(ILogicalOperator source, int sinkPort, int sourcePort, SDFAttributeList schema) {
		if (this.subscribedTo.remove(sinkPort) != null) {
			recalcOutputSchemata = true;
			source.unsubscribeSink(this, sinkPort, sourcePort, schema);
		}
	}
	
	@Override
	public void unsubscribeFromSource(LogicalSubscription subscription) {
		unsubscribeFromSource(subscription.getTarget(), subscription.getSinkPort(), subscription.getSourcePort(), subscription.getSchema());
	}
		
	@Override
	final public Collection<LogicalSubscription> getSubscribedToSource() {
		return this.subscribedTo.values();
	}
	
	@Override
	public LogicalSubscription getSubscribedToSource(int i) {
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
	public void subscribeSink(ILogicalOperator sink, int sinkPort, int sourcePort, SDFAttributeList inputSchema) {
		LogicalSubscription sub = new LogicalSubscription(
				sink, sinkPort, sourcePort, inputSchema);
				if (!this.subscriptions.contains(sub)){
					this.subscriptions.add(sub);
					sink.subscribeToSource(this, sinkPort, sourcePort, inputSchema);
					recalcOutputSchemata = true;
				}
	}
	
	
	@Override
	final public void unsubscribeSink(ILogicalOperator sink, int sinkPort, int sourcePort, SDFAttributeList schema) {
		unsubscribeSink(new LogicalSubscription(sink, sinkPort, sourcePort, schema));
	}
	
	@Override
	public void unsubscribeSink(LogicalSubscription subscription) {
		if (this.subscriptions.remove(subscription)) {
			subscription.getTarget().unsubscribeFromSource(this, subscription.getSinkPort(), subscription.getSourcePort(), subscription.getSchema());
			recalcOutputSchemata = true;
		}
	}

	@Override
	final public Collection<LogicalSubscription> getSubscriptions() {
		//TODO: Unterscheiden, ob mit der Liste Änderungen durchgeführt werden sollen, oder ob nur
		// gelesen werden soll.
		return new Vector<LogicalSubscription>(this.subscriptions);
	}

	@Override
	public Collection<LogicalSubscription> getSubscriptions(ILogicalOperator a) {
		List<LogicalSubscription> subs = new ArrayList<LogicalSubscription>();
		synchronized(subscriptions){
			for(LogicalSubscription l:subscriptions){
				if (l.getTarget() == a){
					subs.add(l);	
				}
			}
		}
		return subs;
	}
	
	@Override
	public int getNumberOfInputs() {
		return this.subscribedTo.size();
	}
		
}
