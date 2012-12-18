/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.ISerializeProperty;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializeNode;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializePropertyItem;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializePropertyList;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Marco Grawunder
 */
public abstract class AbstractLogicalOperator implements Serializable,
		ILogicalOperator {

	private static final long serialVersionUID = -4425148851059140851L;

	final private OwnerHandler ownerHandler;
	
	protected Map<Integer, LogicalSubscription> subscribedToSource = new HashMap<Integer, LogicalSubscription>();
	protected Vector<LogicalSubscription> subscriptions = new Vector<LogicalSubscription>();

	protected boolean recalcOutputSchemata = false;

	private Map<Integer, Subscription<IPhysicalOperator>> physSubscriptionTo = new HashMap<Integer, Subscription<IPhysicalOperator>>();
	// cache access to bounded physOperators
	private Map<Integer, IPhysicalOperator> physInputOperators = new HashMap<Integer, IPhysicalOperator>();

	transient private List<Exception> errors = new ArrayList<Exception>();

	public Map<Integer, IPhysicalOperator> getPhysInputOperators() {
		return physInputOperators;
	}

	private String name = null;
	private String uniqueIdentifier = null;
	
	private List<IPredicate<?>> predicates = new LinkedList<IPredicate<?>>();

	private Map<Integer, SDFSchema> outputSchema = new HashMap<Integer, SDFSchema>();

	public AbstractLogicalOperator(AbstractLogicalOperator op) {
		for (IPredicate<?> pred : op.predicates) {
			this.predicates.add(pred.clone());
		}
		setName(op.getName());
		this.ownerHandler = new OwnerHandler(op.ownerHandler);
		this.outputSchema = op.outputSchema;
		this.uniqueIdentifier = op.uniqueIdentifier;
	}

	public AbstractLogicalOperator() {
		ownerHandler = new OwnerHandler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #clone()
	 */
	@Override
	abstract public AbstractLogicalOperator clone();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getPredicate ()
	 */
	@Override
	public IPredicate<?> getPredicate() {
		if (predicates.size() > 0) {
			return predicates.get(0);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #setPredicate (de.uniol.inf.is.odysseus.core.server.predicate.IPredicate)
	 */
	@Override
	public void setPredicate(IPredicate<?> predicate) {
		if (predicates.size() > 0){
			predicates.set(0, predicate);
		}else{
			predicates.add(predicate);
		}
	}

	@Override
	public void setPredicates(List<IPredicate<?>> predi){
		predicates.clear();
		predicates.addAll(predi);
	}
	
	@Override
	public void addPredicate(IPredicate<?> predicate) {
		predicates.add(predicate);
	}
	
	@Override
	public List<IPredicate<?>> getPredicates() {
		return predicates;
	}
	
	@Override
	public boolean providesPredicates() {
		return !predicates.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getInputSchema (int)
	 */
	@Override
	public SDFSchema getInputSchema(int pos) {
		LogicalSubscription s = subscribedToSource.get(pos);
		SDFSchema ret = null;
		if (s != null) {
			ret = s.getSchema();
			if (ret == null) {
				ILogicalOperator op = s.getTarget();
				if (op != null) {
					ret = op.getOutputSchema(s.getSourceOutPort());
				}
			}
		}
		return ret;
	}

	@Override
	final public SDFSchema getOutputSchema() {
		if (this.outputSchema.get(0) != null) {
			return outputSchema.get(0);
		} else {
			return getOutputSchemaIntern(0);
		}
	}

	@Override
	final public SDFSchema getOutputSchema(int pos) {
		if (this.outputSchema.get(pos) != null) {
			return outputSchema.get(pos);
		} else {
			return getOutputSchemaIntern(pos);
		}
	}

	// Default-Implementation: Get the input from the
	// input operator on port 0
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return getInputSchema(pos);
	}

	@Override
	final public void setOutputSchema(int pos, SDFSchema outputSchema) {
		if (outputSchema == null) {
			this.outputSchema.remove(pos);
		}
		this.outputSchema.put(pos, outputSchema);
	}

	@Override
	final public void setOutputSchema(SDFSchema outputSchema) {
		setOutputSchema(0, outputSchema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getPOName ()
	 */
	@Override
	public String getName() {
		if (name == null){
			name = this.getClass().getSimpleName();
			if (name.endsWith("AO")){
				name = name.substring(0,name.length()-2);
			}
		}
		
		return name;
	}

	// -----------------------------------------------------------------------------------------------
	// Owner delegates
	// -----------------------------------------------------------------------------------------------
	
	
	
	// -----------------------------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #setPOName (java.lang.String)
	 */
	@Override
	@Parameter(name="Name", type = StringParameter.class, optional = true)
	public void setName(String name) {
		this.name = name;
	}

	public void addOwner(IOperatorOwner owner) {
		ownerHandler.addOwner(owner);
	}

	public void addOwner(Collection<IOperatorOwner> owner) {
		ownerHandler.addOwner(owner);
	}

	public void removeOwner(IOperatorOwner owner) {
		ownerHandler.removeOwner(owner);
	}

	public void removeAllOwners() {
		ownerHandler.removeAllOwners();
	}

	public boolean isOwnedBy(IOperatorOwner owner) {
		return ownerHandler.isOwnedBy(owner);
	}

	public boolean isOwnedByAny(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAny(owners);
	}

	public boolean hasOwner() {
		return ownerHandler.hasOwner();
	}

	public List<IOperatorOwner> getOwner() {
		return ownerHandler.getOwner();
	}

	public String getOwnerIDs() {
		return ownerHandler.getOwnerIDs();
	}

	@Override
	@Parameter(name="Id", type = StringParameter.class, optional = true)
	final public void setUniqueIdenfier(String id) {
		this.uniqueIdentifier = id;
	}
	
	@Override
	final public String getUniqueIdentifier() {
		return uniqueIdentifier;
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
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #setPhysInputPO (int,
	 * de.uniol.inf.is.odysseus.core.server.IPhysicalOperator)
	 */
	@Override
	public void setPhysSubscriptionTo(
			Subscription<IPhysicalOperator> subscription) {
		this.physSubscriptionTo.put(subscription.getSinkInPort(), subscription);
		this.physInputOperators.put(subscription.getSinkInPort(),
				subscription.getTarget());
	}

	@Override
	public void setPhysSubscriptionTo(IPhysicalOperator op, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		setPhysSubscriptionTo(new Subscription<IPhysicalOperator>(op,
				sinkInPort, sourceOutPort, schema));
	}

	@Override
	public Collection<Subscription<IPhysicalOperator>> getPhysSubscriptionsTo() {
		return physSubscriptionTo.values();
	}

	@Override
	public Collection<IPhysicalOperator> getPhysInputPOs() {
		return physInputOperators.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getPhysInputPO (int)
	 */
	@Override
	public Subscription<IPhysicalOperator> getPhysSubscriptionTo(int port) {
		return this.physSubscriptionTo.get(port);
	}


	// "delegatable this", used for the delegate sink
	protected ILogicalOperator getInstance() {
		return this;
	}

	@Override
	public void subscribeToSource(ILogicalOperator source, int sinkInPort,
			int sourceOutPort, SDFSchema inputSchema) {
		LogicalSubscription sub = new LogicalSubscription(source, sinkInPort,
				sourceOutPort, inputSchema);
		// Finde den maximalen verwendeten Port
		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}
		synchronized (this.subscribedToSource) {
			if (!this.subscribedToSource.containsKey(sinkInPort)) {
				this.subscribedToSource.put(sinkInPort, sub);
				source.subscribeSink(getInstance(), sinkInPort, sourceOutPort,
						inputSchema);
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
		// und erh�he um eins
		sinkInPort++;
		return sinkInPort;
	}

	@Override
	public void unsubscribeFromSource(ILogicalOperator source, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		if (this.subscribedToSource.remove(sinkInPort) != null) {
			recalcOutputSchemata = true;
			source.unsubscribeSink(this, sinkInPort, sourceOutPort, schema);
		}
	}

	@Override
	public void unsubscribeFromAllSources() {
		synchronized (this.subscribedToSource) {
			Iterator<Entry<Integer, LogicalSubscription>> it = this.subscribedToSource
					.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, LogicalSubscription> next = it.next();
				it.remove();
				LogicalSubscription subscription = next.getValue();
				subscription.getTarget().unsubscribeSink(this,
						subscription.getSinkInPort(),
						subscription.getSourceOutPort(),
						subscription.getSchema());
			}
			recalcOutputSchemata = true;
		}
	}

	@Override
	public void unsubscribeFromSource(LogicalSubscription subscription) {
		unsubscribeFromSource(subscription.getTarget(),
				subscription.getSinkInPort(), subscription.getSourceOutPort(),
				subscription.getSchema());
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
	public Collection<LogicalSubscription> getSubscribedToSource(
			ILogicalOperator a) {
		List<LogicalSubscription> subs = new ArrayList<LogicalSubscription>();
		for (LogicalSubscription l : subscribedToSource.values()) {
			if (l.getTarget() == a) {
				subs.add(l);
			}
		}
		return subs;
	}

	@Override
	public void subscribeSink(ILogicalOperator sink, int sinkInPort,
			int sourceOutPort, SDFSchema inputSchema) {
		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}
		LogicalSubscription sub = new LogicalSubscription(sink, sinkInPort,
				sourceOutPort, inputSchema);
		if (!this.subscriptions.contains(sub)) {
			this.subscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, inputSchema);
			recalcOutputSchemata = true;
		}
	}

	@Override
	final public void unsubscribeSink(ILogicalOperator sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(new LogicalSubscription(sink, sinkInPort,
				sourceOutPort, schema));
	}

	@Override
	public void unsubscribeSink(LogicalSubscription subscription) {
		if (this.subscriptions.remove(subscription)) {
			subscription.getTarget().unsubscribeFromSource(this,
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
			recalcOutputSchemata = true;
		}
	}

	@Override
	final public Collection<LogicalSubscription> getSubscriptions() {
		// TODO: Unterscheiden, ob mit der Liste �nderungen durchgef�hrt
		// werden
		// sollen, oder ob nur
		// gelesen werden soll.
		return new Vector<LogicalSubscription>(this.subscriptions);
	}

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
	public void connectSink(ILogicalOperator sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		// Nothing special in logical Operators
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}

	@Override
	public void disconnectSink(ILogicalOperator sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		// Nothing special in logical Operators
		unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}
	
	@Override
	public void updateSchemaInfos(){
		updateSchemaInfos(this);
	}
	
	static private void updateSchemaInfos(ILogicalOperator sink) {
		for (int i = 0; i < sink.getNumberOfInputs(); i++) {
			LogicalSubscription sub = sink.getSubscribedToSource(i);
			ILogicalOperator source = sub.getTarget();
			if (sub.getSchema() == null) {
				if (source.getOutputSchema() == null) {
					updateSchemaInfos(source);
				} else {
					// Set Schema for both directions (sink to source and source
					// to sink)!
					sub.setSchema(source.getOutputSchema());
					for (LogicalSubscription sourceSub : source.getSubscriptions()) {
						if (sourceSub.getTarget() == sink) {
							sourceSub.setSchema(source.getOutputSchema());
						}
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return getName() + "@" + hashCode() + " OwnerIDs: " + getOwnerIDs();
	}

	@Override
	final public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	// ---------------------------------------------------------------------------------------
	// Methods mainly for GenericOperatorBuilder
	// ---------------------------------------------------------------------------------------

	@Override
	public void initialize() {
		// Can be overwritten if needed
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

	// ---------------------------------------------------------------------------------------
	// Serialization
	// ---------------------------------------------------------------------------------------

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
									for (Object o : (Collection<?>) value) {
										prop.addItemValue(o);
									}
								}
								values.put(name, prop);
							} else {
								// it is just a plain value
								values.put(name, new SerializePropertyItem(
										value));
							}
						} else {
							System.err
									.println("WARN: could not serialize setting "
											+ name
											+ " for "
											+ getClass().getName());
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

	@Override
	public void deserialize(SerializeNode node) {
		// for (Entry<String, ISerializeProperty<?>> prop :
		// node.getProperties().entrySet()) {
		// String name = prop.getKey();
		// Object value = prop.getValue().getValue();
		//
		// for (Method m : getClass().getDeclaredMethods()) {
		// if (m.getName().equalsIgnoreCase(name)) {
		// try {
		// Class<?> propertyClass = prop.getValue().getType();
		// boolean isList = prop.getValue().isList();
		// Object propertyContent = propertyClass.newInstance();
		// if (propertyContent instanceof IParameter) {
		// IParameter<?> para = (IParameter<?>) propertyContent;
		// para.setRequirement(REQUIREMENT.MANDATORY);
		// para.setInputValue(value);
		// para.validate();
		// Object val = para.getValue();
		// if (val != null) {
		// m.invoke(this, val);
		// }
		// } else if (propertyContent instanceof List) {
		// List<?> liste = new ArrayList<Object>();
		// }
		//
		// // Object op = parameter.newInstance();
		// // if (op instanceof IParameter) {
		// //
		// // }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
	}

	@Override
	public String getDoc() {
		return String.format(
				"No documentation available for the logical operator %s",
				getClass().getCanonicalName());
	}
}
