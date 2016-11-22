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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.ISerializeProperty;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializeNode;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializePropertyItem;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializePropertyList;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Marco Grawunder
 */
public abstract class AbstractLogicalOperator implements Serializable, ILogicalOperator{

	private static final long serialVersionUID = -4425148851059140851L;

	private transient OwnerHandler ownerHandler;

	protected Map<Integer, LogicalSubscription> subscribedToSource = new HashMap<Integer, LogicalSubscription>();
	protected List<LogicalSubscription> subscriptions = new CopyOnWriteArrayList<LogicalSubscription>();

	protected boolean recalcOutputSchemata = false;

	private Map<Integer, Subscription<IPhysicalOperator>> physSubscriptionTo = new HashMap<Integer, Subscription<IPhysicalOperator>>();
	// cache access to bounded physOperators
	private Map<Integer, IPhysicalOperator> physInputOperators = new HashMap<Integer, IPhysicalOperator>();
	private boolean recalcAllPhyInputSet = true;
	private boolean cachedAllPhysicalInputSet = false;

	transient private List<String> errors = new ArrayList<String>();

	private Map<String, String> infos = Maps.newTreeMap();

	private String name = null;
	private boolean debug = false;
	private String destinationName = null;
	private String uniqueIdentifier = null;
	private boolean suppressPunctuation = false;

//	private List<IPredicate<?>> predicates = new LinkedList<IPredicate<?>>();

	private Map<Integer, SDFSchema> outputSchema = new HashMap<Integer, SDFSchema>();
	private TimeUnit baseTimeUnit = null;

	/**
	 * Contains a value, if {@link #determineBaseTimeUnit()} has been called once; null else.
	 */
	private IMetaAttribute metaattribute = null;


	public AbstractLogicalOperator(AbstractLogicalOperator op) {
//		for (IPredicate<?> pred : op.predicates) {
//			this.predicates.add(pred.clone());
//		}
		setName(op.getName());
		this.ownerHandler = new OwnerHandler(op.ownerHandler);
		this.outputSchema = new HashMap<>(op.outputSchema);
		// this.outputSchema = op.outputSchema;
		this.uniqueIdentifier = op.uniqueIdentifier;
		this.infos = copyParameterInfos(op.infos);
		this.destinationName = op.destinationName;
		this.debug = op.debug;
		this.suppressPunctuation = op.suppressPunctuation;
		this.baseTimeUnit = op.baseTimeUnit;

	}

	public AbstractLogicalOperator() {
		ownerHandler = new OwnerHandler();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
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

	public Map<Integer, IPhysicalOperator> getPhysInputOperators() {
		return physInputOperators;
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
		return getOutputSchema(0);
	}

	@Override
	final public SDFSchema getOutputSchema(int pos) {
//		if (physInputOperators.get(pos) != null){
//			return physInputOperators.get(pos).getOutputSchema();
//		}
		if (this.outputSchema.get(pos) != null) {
			return outputSchema.get(pos);
		} else {
			SDFSchema schema = getOutputSchemaIntern(pos);
			return schema;
		}
	}

	@Override
	public void setMetadata(IMetaAttribute metaattribute) {
		this.metaattribute = metaattribute;
	}

	protected IMetaAttribute getMetaAttribute() {
		return metaattribute;
	}

	@Override
	public Map<Integer, SDFSchema> getOutputSchemaMap() {
		return Collections.unmodifiableMap(outputSchema);
	}

	// Default-Implementation: Get the input from the
	// input operator on port 0
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return getInputSchema(0);
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

	public TimeUnit getBaseTimeUnit() {
		if (baseTimeUnit == null) {
			determineBaseTimeUnit();
		}
		return baseTimeUnit;
	}

	public void determineBaseTimeUnit() {
		baseTimeUnit = TimeUnit.MILLISECONDS;

		SDFConstraint c = getInputSchema(0).getConstraint(
				SDFConstraint.BASE_TIME_UNIT);
		if (c != null) {
			baseTimeUnit = (TimeUnit) c.getValue();
		} else {

			// Find input schema attribute with type start timestamp
			// It provides the correct base unit
			// if not given use MILLISECONDS as default
			Collection<SDFAttribute> attrs = getInputSchema(0)
					.getSDFDatatypeAttributes(SDFDatatype.START_TIMESTAMP);
			if (attrs.isEmpty()) {
				attrs = getInputSchema(0).getSDFDatatypeAttributes(
						SDFDatatype.START_TIMESTAMP_STRING);
			}
			if (attrs.size() > 0) {
				SDFAttribute attr = attrs.iterator().next();
				SDFConstraint constr = attr
						.getDtConstraint(SDFConstraint.BASE_TIME_UNIT);
				if (constr != null) {
					baseTimeUnit = (TimeUnit) constr.getValue();
				}
			}
		}
	}

	public void setBaseTimeUnit(TimeUnit baseTimeUnit) {
		this.baseTimeUnit = baseTimeUnit;
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
		if (name == null) {
			name = this.getClass().getSimpleName();
			if (name.endsWith("AO")) {
				name = name.substring(0, name.length() - 2);
			}
		}

		return name;
	}

	@Override
	public String getDestinationName() {
		return destinationName;
	}

	@Override
	@Parameter(name = "Destination", type = StringParameter.class, optional = true, doc = "Name of the place to execute the operator")
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #setPOName (java.lang.String)
	 */
	@Override
	@Parameter(name = "Name", type = StringParameter.class, optional = true, doc = "Name of the operator (e.g. for visulization).")
	public void setName(String name) {
		this.name = name;
	}

	@Parameter(name="suppressPunctuations", type= BooleanParameter.class, optional = true, doc ="If set to true, no punctuations will be delivered from this operator. Default is false")
	public void setSuppressPunctuations(boolean suppressPunctuation) {
		this.suppressPunctuation = suppressPunctuation;
	}

	@Override
	public boolean isSuppressPunctuations() {
		return suppressPunctuation;
	}

	@Override
	@Parameter(name = "Debug", type = BooleanParameter.class, optional = true, doc = "Flag, that this operator should be debuged.")
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public void addOwner(IOperatorOwner owner) {
		ownerHandler.addOwner(owner);
	}

	@Override
	public void addOwner(Collection<IOperatorOwner> owner) {
		ownerHandler.addOwner(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		ownerHandler.removeOwner(owner);
	}

	@Override
	public void removeAllOwners() {
		ownerHandler.removeAllOwners();
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return ownerHandler.isOwnedBy(owner);
	}

	@Override
	public boolean isOwnedByAny(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAny(owners);
	}

	@Override
	public boolean isOwnedByAll(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAll(owners);
	}

	@Override
	public boolean hasOwner() {
		return ownerHandler.hasOwner();
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return ownerHandler.getOwner();
	}

	@Override
	public String getOwnerIDs() {
		return ownerHandler.getOwnerIDs();
	}

	@Override
	@Parameter(name = "Id", type = StringParameter.class, optional = true, doc = "Systemwide unique id")
	final public void setUniqueIdentifier(String id) {
		this.uniqueIdentifier = id;
	}

	@Override
	final public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		if (recalcAllPhyInputSet) {
			cachedAllPhysicalInputSet = true;
			for (Integer i : this.subscribedToSource.keySet()) {
				if (this.physInputOperators.get(i) == null) {
					cachedAllPhysicalInputSet = false;
					break;
				}
			}
			recalcAllPhyInputSet = false;
		}
		return cachedAllPhysicalInputSet;

		// if (subscribedInputSizeCache !=
		// this.subscribedToSource.keySet().size()) {
		// for (Integer i : this.subscribedToSource.keySet()) {
		// if (this.physInputOperators.get(i) == null) {
		// isAllPhysicalSetCache = false;
		// break;
		// }
		// }
		// isAllPhysicalSetCache = true;
		// subscribedInputSizeCache = this.subscribedToSource.keySet().size();
		// }
		// return isAllPhysicalSetCache;
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
	public void setPhysSubscriptionTo(Subscription<IPhysicalOperator> subscription) {
		this.physSubscriptionTo.put(subscription.getSinkInPort(), subscription);
		this.physInputOperators.put(subscription.getSinkInPort(), subscription.getTarget());
		this.recalcAllPhyInputSet = true;
	}

	@Override
	public void setPhysSubscriptionTo(IPhysicalOperator op, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		setPhysSubscriptionTo(new Subscription<IPhysicalOperator>(op, sinkInPort, sourceOutPort, schema));
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
	public void subscribeToSource(ILogicalOperator source, int sinkInPort, int sourceOutPort, SDFSchema inputSchema) {
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
				this.recalcAllPhyInputSet = true;
			} else {
				if( source != subscribedToSource.get(sinkInPort).getTarget()) {
					throw new IllegalArgumentException("SinkInPort " + sinkInPort + " already bound to logical operator '" + subscribedToSource.get(sinkInPort).getTarget() + "'. Cannot bind '" + source + "'");
				}
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
	public void unsubscribeFromSource(ILogicalOperator source, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		if (this.subscribedToSource.remove(sinkInPort) != null) {
			recalcOutputSchemata = true;
			this.recalcAllPhyInputSet = true;
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
			this.recalcAllPhyInputSet = true;
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
	public void subscribeSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema inputSchema) {
		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}
		LogicalSubscription sub = new LogicalSubscription(sink, sinkInPort, sourceOutPort, inputSchema);
		if (!this.subscriptions.contains(sub)) {
			this.subscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, inputSchema);
			recalcOutputSchemata = true;
			this.recalcAllPhyInputSet = true;
		}
	}

	@Override
	public void subscribeSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema schema, boolean asActive, int openCalls) {
		throw new IllegalArgumentException("This method cannot be called on Logical Operators");
	}

	@Override
	final public void unsubscribeSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(new LogicalSubscription(sink, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void unsubscribeSink(LogicalSubscription subscription) {
		if (this.subscriptions.remove(subscription)) {
			subscription.getTarget().unsubscribeFromSource(this, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
			recalcOutputSchemata = true;
			this.recalcAllPhyInputSet = true;
		}
	}

	@Override
	public void unsubscribeFromAllSinks() {
			for(LogicalSubscription subscription: this.subscriptions){
				unsubscribeSink(subscription);
			}
	}

//	@Override
//	final public Collection<LogicalSubscription> getSubscriptions() {
//		return new Vector<LogicalSubscription>(this.subscriptions);
//	}

	@Override
	final public Collection<LogicalSubscription> getSubscriptions() {
		return Collections.unmodifiableCollection(this.subscriptions);
	}


	@Override
	public int getNumberOfInputs() {
		return this.subscribedToSource.size();
	}

	@Override
	public void clearPhysicalSubscriptions() {
		this.physInputOperators.clear();
		this.physSubscriptionTo.clear();
		this.recalcAllPhyInputSet = true;
	}

	@Override
	public void connectSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// Nothing special in logical Operators
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}

	@Override
	public void disconnectSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// Nothing special in logical Operators
		unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}

//	@Override
//	public void updateSchemaInfos() {
//		updateSchemaInfos(this);
//	}
//
//	static private void updateSchemaInfos(ILogicalOperator sink) {
//		for (int i = 0; i < sink.getNumberOfInputs(); i++) {
//			LogicalSubscription sub = sink.getSubscribedToSource(i);
//			ILogicalOperator source = sub.getTarget();
//			if (sub.getSchema() == null) {
//				if (source.getOutputSchema() == null) {
//					updateSchemaInfos(source);
//				} else {
//					// Set Schema for both directions (sink to source and source
//					// to sink)!
//					sub.setSchema(source.getOutputSchema());
//					for (LogicalSubscription sourceSub : source.getSubscriptions()) {
//						if (sourceSub.getTarget() == sink) {
//							sourceSub.setSchema(source.getOutputSchema());
//						}
//					}
//				}
//			}
//		}
//	}

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

    protected void addError(boolean condition, String e) {
        if (condition) {
            this.addError(e);
        }
    }

	protected void addError(String e) {
		this.errors.add(e);
	}

	@Deprecated
	protected void addError(Exception e){
		addError(e.getMessage());
	}

	protected void clearErrors() {
		this.errors.clear();
	}

	public List<String> getErrors() {
		return Collections.unmodifiableList(this.errors);
	}

    public boolean hasErrors() {
        return !this.errors.isEmpty();
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
	public Map<String, String> getParameterInfos() {
		return Collections.unmodifiableMap(infos);
	}

	@Override
	public void addParameterInfo(String key, Object value) {
		this.infos.put(key.toUpperCase(), value != null ? value.toString() : null);
	}

	@Override
	public void removeParameterInfo(String key) {
		this.infos.remove(key.toUpperCase());
	}

	@Override
	public void setParameterInfos(Map<String, String> infos) {
		this.infos = infos;
	}

	private static Map<String, String> copyParameterInfos(Map<String, String> infos) {
		Map<String, String> copy = Maps.newTreeMap();
		for (String key : infos.keySet()) {
			copy.put(key, infos.get(key));
		}
		return copy;
	}

	@Override
	public boolean isPipeOperator() {
		return isSinkOperator() && isSourceOperator();
	}

	@Override
	public boolean isSinkOperator() {
		return true;
	}

	@Override
	public boolean isSourceOperator() {
		return true;
	}
	
	@Override
	public OperatorStateType getStateType() {
		return OperatorStateType.UNKNOWN;
	}
}
