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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Marco Grawunder
 */
public abstract class AbstractLogicalOperator implements Serializable, ILogicalOperator {

	private static final long serialVersionUID = -4425148851059140851L;

	private transient OwnerHandler ownerHandler;

	// Connection to sources
	protected Map<Integer, LogicalSubscription> subscribedToSource = new HashMap<Integer, LogicalSubscription>();

	protected List<LogicalSubscription> subscriptions = new CopyOnWriteArrayList<LogicalSubscription>();

	protected boolean recalcOutputSchemata = false;

	private Map<Integer, Subscription<IPhysicalOperator, ILogicalOperator>> physSubscriptionTo = new HashMap<>();
	// cache access to bounded physOperators
	private Map<Integer, IPhysicalOperator> physInputOperators = new HashMap<>();
	private boolean recalcAllPhyInputSet = true;
	private boolean cachedAllPhysicalInputSet = false;

	transient private List<String> errors = new ArrayList<String>();

	private Map<String, String> infos = Maps.newTreeMap();

	private String name = null;
	private boolean debug = false;
	private String destinationName = null;
	private String uniqueIdentifier = null;
	private boolean suppressPunctuation = false;

	private Map<Integer, SDFSchema> outputSchema = new HashMap<>();
	private TimeUnit baseTimeUnit = null;
	
	private final Optional<ILogicalOperator> clonedFrom;
	private Map<String, Object> transformationHints = new HashMap<>();

	/**
	 * Contains a value, if {@link #determineBaseTimeUnit()} has been called once;
	 * null else.
	 */
	private IMetaAttribute metaattribute = null;

	private final OptionMap optionsMap = new OptionMap();

	private List<Option> optionsList;
	
	private InputOrderRequirement inputOrderRequirement = InputOrderRequirement.STRICT;

	public AbstractLogicalOperator(AbstractLogicalOperator op) {
//		for (IPredicate<?> pred : op.predicates) {
//			this.predicates.add(pred.clone());
//		}
		this.clonedFrom = Optional.of(op);
		this.name = op.getName();
		this.ownerHandler = new OwnerHandler(op.ownerHandler);
		this.outputSchema = new HashMap<>(op.outputSchema);
		// this.outputSchema = op.outputSchema;
		this.uniqueIdentifier = op.uniqueIdentifier;
		this.infos = copyParameterInfos(op.infos);
		this.destinationName = op.destinationName;
		this.debug = op.debug;
		this.suppressPunctuation = op.suppressPunctuation;
		this.baseTimeUnit = op.baseTimeUnit;
		optionsMap.addAll(op.optionsMap);
		if (op.optionsList != null) {
			this.optionsList = new ArrayList<>(op.optionsList);
		}
		this.transformationHints = new HashMap<String, Object>(op.transformationHints);
		this.inputOrderRequirement = op.inputOrderRequirement;
	}

	public AbstractLogicalOperator() {
		ownerHandler = new OwnerHandler();
		clonedFrom = Optional.empty();
	}

	@Override
	final public Optional<ILogicalOperator> getClonedFrom(){
		return clonedFrom;
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
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
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
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getInputSchema (int)
	 */
	@Override
	public SDFSchema getInputSchema(int pos) {
		LogicalSubscription s = subscribedToSource.get(pos);
		SDFSchema ret = null;
		if (s != null) {
			ret = s.getSchema();
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
	public void recalcOutputSchema() {
		recalcOutputSchemata = true;
		outputSchema.clear();
		getOutputSchema();
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

		SDFConstraint c = getInputSchema(0).getConstraint(SDFConstraint.BASE_TIME_UNIT);
		if (c != null) {
			baseTimeUnit = (TimeUnit) c.getValue();
		} else {

			// Find input schema attribute with type start timestamp
			// It provides the correct base unit
			// if not given use MILLISECONDS as default
			Collection<SDFAttribute> attrs = getInputSchema(0).getSDFDatatypeAttributes(SDFDatatype.START_TIMESTAMP);
			if (attrs.isEmpty()) {
				attrs = getInputSchema(0).getSDFDatatypeAttributes(SDFDatatype.START_TIMESTAMP_STRING);
			}
			if (attrs.size() > 0) {
				SDFAttribute attr = attrs.iterator().next();
				SDFConstraint constr = attr.getDtConstraint(SDFConstraint.BASE_TIME_UNIT);
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
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getPOName ()
	 */
	@Override
	public String getName() {
		if (name == null) {
			String name = this.getClass().getSimpleName();
			if (name.endsWith("AO")) {
				name = name.substring(0, name.length() - 2);
			}
			return name;
		}

		return name;
	}

	@Override
	public boolean isNameSet() {
		return name != null;
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
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #setPOName (java.lang.String)
	 */
	@Override
	@Parameter(name = "Name", type = StringParameter.class, optional = true, doc = "Name of the operator (e.g. for visulization).")
	final public void setName(String name) {
		this.name = name;
	}

	@Parameter(name = "suppressPunctuations", type = BooleanParameter.class, optional = true, doc = "If set to true, no punctuations will be delivered from this operator. Default is false")
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
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #setPhysInputPO (int, de.uniol.inf.is.odysseus.core.server.IPhysicalOperator)
	 */
	@Override
	public void setPhysSubscriptionTo(Subscription<IPhysicalOperator, ILogicalOperator> subscription) {
		this.physSubscriptionTo.put(subscription.getSinkInPort(), subscription);
		this.physInputOperators.put(subscription.getSinkInPort(), subscription.getSource());
		this.recalcAllPhyInputSet = true;
	}

	@Override
	public void setPhysSubscriptionTo(IPhysicalOperator op, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		setPhysSubscriptionTo(
				new Subscription<IPhysicalOperator, ILogicalOperator>(op, this, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public Collection<Subscription<IPhysicalOperator, ILogicalOperator>> getPhysSubscriptionsTo() {
		return physSubscriptionTo.values();
	}

	@Override
	public Collection<IPhysicalOperator> getPhysInputPOs() {
		return physInputOperators.values();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #getPhysInputPO (int)
	 */
	@Override
	public Subscription<IPhysicalOperator, ILogicalOperator> getPhysSubscriptionTo(int port) {
		return this.physSubscriptionTo.get(port);
	}

	// "delegatable this", used for the delegate sink
	protected ILogicalOperator getInstance() {
		return this;
	}

	@Override
	public void subscribeToSource(ILogicalOperator source, int sinkInPort, int sourceOutPort, SDFSchema inputSchema) {
		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}
		LogicalSubscription sub = new LogicalSubscription(source, this, sinkInPort, sourceOutPort, inputSchema);
		subscribeToSource(sub);
	}

	@Override
	public void subscribeToSource(LogicalSubscription sub) {
		synchronized (this.subscribedToSource) {
			if (!this.subscribedToSource.containsKey(sub.getSinkInPort())) {
				this.subscribedToSource.put(sub.getSinkInPort(), sub);
				sub.getSource().subscribeSink(sub);
				recalcOutputSchemata = true;
				this.recalcAllPhyInputSet = true;
			} else {
				if (sub.getSource() != subscribedToSource.get(sub.getSinkInPort()).getSource()) {
					throw new IllegalArgumentException(
							"SinkInPort " + sub.getSinkInPort() + " already bound to logical operator '"
									+ subscribedToSource.get(sub.getSinkInPort()).getSource() + "'. Cannot bind '"
									+ sub.getSource() + "'");
				}
			}
		}
	}

	private synchronized int getNextFreeSinkInPort() {
		int sinkInPort = -1;
		for (Integer port : subscribedToSource.keySet()) {
			if (port > sinkInPort) {
				sinkInPort = port;
			}
		}
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
				subscription.getSource().unsubscribeSink(this, subscription.getSinkInPort(),
						subscription.getSourceOutPort(), subscription.getSchema());
			}
			recalcOutputSchemata = true;
			this.recalcAllPhyInputSet = true;
		}
	}

	@Override
	public void unsubscribeFromSource(LogicalSubscription subscription) {
		unsubscribeFromSource(subscription.getSource(), subscription.getSinkInPort(), subscription.getSourceOutPort(),
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
	public Collection<LogicalSubscription> getSubscribedToSource(ILogicalOperator a) {
		List<LogicalSubscription> subs = new ArrayList<LogicalSubscription>();
		for (LogicalSubscription l : subscribedToSource.values()) {
			if (l.getSource() == a) {
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
		LogicalSubscription sub = new LogicalSubscription(this, sink, sinkInPort, sourceOutPort, inputSchema);
		subscribeSink(sub);
	}

	@Override
	public void subscribeSink(LogicalSubscription sub) {
		if (!this.subscriptions.contains(sub)) {
			this.subscriptions.add(sub);
			sub.getSink().subscribeToSource(sub);
			recalcOutputSchemata = true;
			this.recalcAllPhyInputSet = true;
		}
	}

	@Override
	public void subscribeSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema schema,
			boolean asActive, int openCalls) {
		throw new IllegalArgumentException("This method cannot be called on Logical Operators");
	}

	@Override
	final public void unsubscribeSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(new LogicalSubscription(this, sink, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void unsubscribeSink(LogicalSubscription subscription) {
		if (this.subscriptions.remove(subscription)) {
			subscription.getSink().unsubscribeFromSource(this, subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
			recalcOutputSchemata = true;
			this.recalcAllPhyInputSet = true;
		}
	}

	@Override
	public void unsubscribeFromAllSinks() {
		for (LogicalSubscription subscription : this.subscriptions) {
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
	public Collection<LogicalSubscription> getConnectedSinks() {
		return getSubscriptions();
	}

	@Override
	public void disconnectSink(ILogicalOperator sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// Nothing special in logical Operators
		unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}

	@Override
	public void disconnectSink(LogicalSubscription subscription) {
		unsubscribeSink(subscription);
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

	protected void addError(boolean condition, String e) {
		if (condition) {
			this.addError(e);
		}
	}

	protected void addError(String e) {
		this.errors.add(e);
	}

	@Deprecated
	protected void addError(Exception e) {
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
	public boolean isRoot() {
		return subscriptions.isEmpty() || !isOutputPortBound(0);
	}

	private boolean isOutputPortBound(int port) {
		boolean output0Bound = false;
		for (LogicalSubscription sub : subscriptions) {
			if (sub.getSourceOutPort() == port) {
				output0Bound = true;
			}
		}
		return output0Bound;
	}

	@Override
	public OperatorStateType getStateType() {
		return OperatorStateType.UNKNOWN;
	}

	@Parameter(type = EnumParameter.class, name ="inputOrderRequirement", optional = true, possibleValues = "getInputOrderRequirementValues")
	public void setInputOrderRequirement(InputOrderRequirement inputOrderRequirement) {
		this.inputOrderRequirement = inputOrderRequirement;
	}
	
	@SuppressWarnings("unused")
	private InputOrderRequirement[] getInputOrderRequirementValues(){
		return InputOrderRequirement.values();
	}
	
	/**
	 * Requirements for input. In most cases this is STRICT. Could be overwritten in
	 * subclasses
	 * 
	 */
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return inputOrderRequirement;
	}

	// ---------------------------------------------------------------------------------------------------
	// Option-Handling
	// ---------------------------------------------------------------------------------------------------

	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	public void setOptions(List<Option> value) {
		for (Option option : value) {
			optionsMap.setOption(option.getName().toLowerCase(), option.getValue());
		}
		optionsList = value;
	}

	public List<Option> getOptions() {
		return optionsList;
	}

	protected void setOption(String key, Object value) {
		optionsMap.setOption(key, value);
	}

	protected void addOption(String key, String value) {
		optionsMap.overwriteOption(key, value);
	}

	protected String getOption(String key) {
		return optionsMap.get(key);
	}

	public void setOptionMap(OptionMap options) {
		this.optionsMap.clear();
		this.optionsMap.addAll(options);
	}

	public OptionMap getOptionsMap() {
		return optionsMap;
	}
	
	@Override
	public void setTransformationHint(String key, Object value) {
		this.transformationHints.put(key, value);
	}
	
	@Override
	public Optional<Object> getTransformationHint(String key) {
		Object val = transformationHints.get(key);
		return val != null?Optional.of(val):Optional.empty();
	}

}
