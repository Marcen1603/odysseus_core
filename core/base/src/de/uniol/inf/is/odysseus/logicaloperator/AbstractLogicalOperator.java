/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.logicaloperator;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.serialize.ISerializeProperty;
import de.uniol.inf.is.odysseus.logicaloperator.serialize.SerializeNode;
import de.uniol.inf.is.odysseus.logicaloperator.serialize.SerializePropertyItem;
import de.uniol.inf.is.odysseus.logicaloperator.serialize.SerializePropertyList;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */
public abstract class AbstractLogicalOperator implements Serializable, ILogicalOperator {

	private static final long serialVersionUID = -4425148851059140851L;

	transient private List<IOperatorOwner> owners;

	protected Map<Integer, LogicalSubscription> subscribedToSource = new HashMap<Integer, LogicalSubscription>();
	protected Vector<LogicalSubscription> subscriptions = new Vector<LogicalSubscription>();;

	protected boolean recalcOutputSchemata = false;

	private Map<Integer, Subscription<ISource<?>>> physSubscriptionTo = new HashMap<Integer, Subscription<ISource<?>>>();
	// cache access to bounded physOperators
	private Map<Integer, ISource<?>> physInputOperators = new HashMap<Integer, ISource<?>>();

	transient private List<Exception> errors = new ArrayList<Exception>();

	public Map<Integer, ISource<?>> getPhysInputOperators() {
		return physInputOperators;
	}

	private String name = null;

	private IPredicate<?> predicate = null;

	public AbstractLogicalOperator(AbstractLogicalOperator op) {
		predicate = (op.predicate == null) ? null : op.predicate.clone();
		setName(op.getName());
		initOwner();
		owners.addAll(op.owners);
		// physSubscriptionTo = op.physSubscriptionTo == null ? null
		// : new
		// HashMap<Integer,Subscription<ISource<?>>>(op.physSubscriptionTo);
	}

	public AbstractLogicalOperator() {
		initOwner();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#clone()
	 */
	@Override
	abstract public AbstractLogicalOperator clone();

	@Override
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> replaced) {
		if (predicate != null) {
			predicate.updateAfterClone(replaced);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getPredicate ()
	 */
	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#setPredicate
	 * (de.uniol.inf.is.odysseus.predicate.IPredicate)
	 */
	@Override
	public void setPredicate(IPredicate<?> predicate) {
		this.predicate = predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getInputSchema
	 * (int)
	 */
	@Override
	public SDFAttributeList getInputSchema(int pos) {
		LogicalSubscription s = subscribedToSource.get(pos);
		SDFAttributeList ret = null;
		if (s != null) {
			ret = s.getSchema();
			if (ret == null) {
				ILogicalOperator op = s.getTarget();
				if (op != null) {
					ret = op.getOutputSchema();
				}
			}
		}
		return ret;
	}

	@Override
	public SDFAttributeList getOutputSchema(int pos) {
		return getOutputSchema();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#setInputSchema
	 * (int,
	 * de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList)
	 */
	// public void setInputSchema(int pos, SDFAttributeList schema) {
	// subscribedTo.get(pos).setInputSchema(schema);
	// recalcOutputSchemata = true;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getPOName
	 * ()
	 */
	@Override
	public String getName() {
		if (name == null) {
			return this.getClass().getSimpleName();
		} else {
			return name;
		}
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		for (Integer i : this.subscribedToSource.keySet()) {
			if (this.physInputOperators.get(i) == null) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#setPOName
	 * (java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#setPhysInputPO
	 * (int, de.uniol.inf.is.odysseus.IPhysicalOperator)
	 */
	@Override
	public void setPhysSubscriptionTo(Subscription<ISource<?>> subscription) {
		this.physSubscriptionTo.put(subscription.getSinkInPort(), subscription);
		this.physInputOperators.put(subscription.getSinkInPort(), subscription.getTarget());
	}

	@Override
	public void setPhysSubscriptionTo(ISource<?> op, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		setPhysSubscriptionTo(new Subscription<ISource<?>>(op, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public Collection<Subscription<ISource<?>>> getPhysSubscriptionsTo() {
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
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getPhysInputPO
	 * (int)
	 */
	@Override
	public Subscription<ISource<?>> getPhysSubscriptionTo(int port) {
		return this.physSubscriptionTo.get(port);
	}

	@Override
	public void addOwner(IOperatorOwner owner) {
		if (this.owners == null) {
			initOwner();
		}
		if (!this.owners.contains(owner)) {
			this.owners.add(owner);
		}
	}

	private void initOwner() {
		owners = new IdentityArrayList<IOperatorOwner>();
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		this.owners.remove(owner);
	}

	@Override
	public void removeAllOwners() {
		this.owners.clear();
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return this.owners.contains(owner);
	}

	@Override
	public boolean hasOwner() {
		return this.owners.size() > 0;
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return Collections.unmodifiableList(this.owners);
	}

	/**
	 * Returns a ","-separated string of the owner IDs.
	 * 
	 * @param owners
	 *            Owner which have IDs.
	 * @return ","-separated string of the owner IDs.
	 */
	@Override
	public String getOwnerIDs() {
		StringBuffer result = new StringBuffer();
		if (owners != null) { // TODO: WARUM??
			for (IOperatorOwner iOperatorOwner : owners) {
				if (result.length() > 0) {
					result.append(", ");
				}
				result.append(iOperatorOwner.getID());
			}
		}
		return result.toString();
	}

	// "delegatable this", used for the delegate sink
	protected ILogicalOperator getInstance() {
		return this;
	}

	@Override
	public void subscribeToSource(ILogicalOperator source, int sinkInPort, int sourceOutPort, SDFAttributeList inputSchema) {
		LogicalSubscription sub = new LogicalSubscription(source, sinkInPort, sourceOutPort, inputSchema);
		// Finde den maximalen verwendeten Port
		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}
		synchronized (this.subscribedToSource) {
			if (!this.subscribedToSource.containsKey(sinkInPort)) {
				this.subscribedToSource.put(sinkInPort, sub);
				source.subscribeSink(getInstance(), sinkInPort, sourceOutPort, inputSchema);
				recalcOutputSchemata = true;
			}
		}

	}

	private int getNextFreeSinkInPort() {
		int sinkInPort = -1;
		for (Integer port : subscribedToSource.keySet()) {
			if (port > sinkInPort) {
				sinkInPort = port;
			}
		}
		// und erhöhe um eins
		sinkInPort++;
		return sinkInPort;
	}

	@Override
	public void unsubscribeFromSource(ILogicalOperator source, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		if (this.subscribedToSource.remove(sinkInPort) != null) {
			recalcOutputSchemata = true;
			source.unsubscribeSink(this, sinkInPort, sourceOutPort, schema);
		}
	}

	@Override
	public void unsubscribeFromAllSources() {
		synchronized (this.subscribedToSource) {
			Iterator<Entry<Integer, LogicalSubscription>> it = this.subscribedToSource.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, LogicalSubscription> next = it.next();
				it.remove();
				LogicalSubscription subscription = next.getValue();
				subscription.getTarget().unsubscribeSink(this, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
			}
			recalcOutputSchemata = true;
		}
	}

	@Override
	public void unsubscribeFromSource(LogicalSubscription subscription) {
		unsubscribeFromSource(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
	}

	@Override
	final public Collection<LogicalSubscription> getSubscribedToSource() {
		return this.subscribedToSource.values();
	}

	@Override
	public LogicalSubscription getSubscribedToSource(int i) {
		return this.subscribedToSource.get(i);
	}

	@Override
	public Collection<LogicalSubscription> getSubscribedToSource(ILogicalOperator a) {
		List<LogicalSubscription> subs = new ArrayList<LogicalSubscription>();
		for (LogicalSubscription l : subscribedToSource.values()) {
			if (l.getTarget() == a) {
				subs.add(l);
			}
		}
		return subs;
	}

	@Override
	public void subscribeSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFAttributeList inputSchema) {
		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}
		LogicalSubscription sub = new LogicalSubscription(sink, sinkInPort, sourceOutPort, inputSchema);
		if (!this.subscriptions.contains(sub)) {
			this.subscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, inputSchema);
			recalcOutputSchemata = true;
		}
	}

	@Override
	final public void unsubscribeSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		unsubscribeSink(new LogicalSubscription(sink, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void unsubscribeSink(LogicalSubscription subscription) {
		if (this.subscriptions.remove(subscription)) {
			subscription.getTarget().unsubscribeFromSource(this, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
			recalcOutputSchemata = true;
		}
	}

	@Override
	final public Collection<LogicalSubscription> getSubscriptions() {
		// TODO: Unterscheiden, ob mit der Liste Änderungen durchgeführt werden
		// sollen, oder ob nur
		// gelesen werden soll.
		return new Vector<LogicalSubscription>(this.subscriptions);
	}

	// @Override
	// public Collection<LogicalSubscription> getSubscriptions(ILogicalOperator
	// a) {
	// List<LogicalSubscription> subs = new ArrayList<LogicalSubscription>();
	// synchronized (subscriptions) {
	// for (LogicalSubscription l : subscriptions) {
	// if (l.getTarget() == a) {
	// subs.add(l);
	// }
	// }
	// }
	// return subs;
	// }

	@Override
	public int getNumberOfInputs() {
		return this.subscribedToSource.size();
	}

	@Override
	public void clearPhysicalSubscriptions() {
		this.physInputOperators.clear();
		this.physSubscriptionTo.clear();
	}

	@Override
	public void connectSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		// Nothing special in logical Operators
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}

	@Override
	public void disconnectSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		// Nothing special in logical Operators
		unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}

	@Override
	public String toString() {
		return getName() + "@" + hashCode() + " OwnerIDs: " + getOwnerIDs();
	}

	// TODO: Check if equals is needed in logical operators
	@Override
	final public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean isValid() {
		return true;
	}

	protected void addError(Exception e) {
		this.errors.add(e);
	}

	protected void clearErrors() {
		this.errors.clear();
	}

	public List<Exception> getErrors() {
		return Collections.unmodifiableList(this.errors);
	}

	@Override
	public SerializeNode serialize() {
		SerializeNode node = new SerializeNode(getClass());
		Map<String, ISerializeProperty<?>> values = new HashMap<String, ISerializeProperty<?>>();
		for (Method method : getClass().getMethods()) {
			Annotation[] annotations = method.getAnnotations();
			for (Annotation a : annotations) {
				if (a instanceof Parameter) {
					Parameter p = (Parameter) a;
					String name = method.getName();
					if (p.name() != null && !p.name().isEmpty()) {
						name = p.name();
					}
					Method otherMethod = getRelatedMethodByName(name);
					if (otherMethod == null) {
						otherMethod = getRelatedMethod(method);
					}
					try {
						if (otherMethod != null) {
							Object value = otherMethod.invoke(this);
							// it is e.g. a List<String>
							if (p.isList()) {								
								SerializePropertyList prop = new SerializePropertyList();
								if (value != null) {									
									for(Object o : (Collection<?>) value){
										prop.addItemValue(o);
									}
								}								
								values.put(name, prop);
							} else {
								// it is just a plain value
								values.put(name, new SerializePropertyItem(value));
							}
						} else {
							System.err.println("WARN: could not serialize setting " + name + " for " + getClass().getName());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		node.setProperties(values);
		return node;
	}

	private Method getRelatedMethod(Method other) {
		String setterName = other.getName();
		if (setterName.toUpperCase().startsWith("SET")) {
			// try with getXYZ for setXYZ
			String otherName = "g" + setterName.substring(1);
			Method m;
			try {
				m = getClass().getMethod(otherName);
				return m;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private Method getRelatedMethodByName(String name) {
		for (Method method : getClass().getMethods()) {
			for (Annotation a : method.getAnnotations()) {
				if (a instanceof GetParameter) {
					GetParameter gp = (GetParameter) a;
					if (gp.name().equals(name)) {
						return method;
					}
				}
			}
		}
		return null;
	}

	public void deserialize(SerializeNode node) {
//		for (Entry<String, ISerializeProperty<?>> prop : node.getProperties().entrySet()) {
//			String name = prop.getKey();
//			Object value = prop.getValue().getValue();
//
//			for (Method m : getClass().getDeclaredMethods()) {
//				if (m.getName().equalsIgnoreCase(name)) {
//					try {
//						Class<?> propertyClass = prop.getValue().getType();
//						boolean isList = prop.getValue().isList();
//						Object propertyContent = propertyClass.newInstance();
//						if (propertyContent instanceof IParameter) {
//							IParameter<?> para = (IParameter<?>) propertyContent;
//							para.setRequirement(REQUIREMENT.MANDATORY);
//							para.setInputValue(value);
//							para.validate();
//							Object val = para.getValue();
//							if (val != null) {
//								m.invoke(this, val);
//							}
//						} else if (propertyContent instanceof List) {
//							List<?> liste = new ArrayList<Object>();
//						}
//
////						Object op = parameter.newInstance();
////						if (op instanceof IParameter) {
////
////						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
	}
}
