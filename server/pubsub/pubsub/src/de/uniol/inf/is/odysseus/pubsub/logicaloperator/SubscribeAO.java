/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.logicaloperator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;

/**
 * Logical Subscribe Operator. The Operator provides the subscribe functionality
 * in publish/Subscribe systems.
 *
 * @author ChrisToenjesDeye
 *
 */
@LogicalOperator(name = "Subscribe", minInputPorts = 0, maxInputPorts = 0, doc = "This Operator provides the subscribe functionality in publish/Subscribe systems.", category = { LogicalOperatorCategory.PUPSUB })
public class SubscribeAO extends UnaryLogicalOp implements IHasPredicates {

	/**
	 *
	 */
	private static final long serialVersionUID = 4060380514816738851L;
	private List<SDFAttribute> sdfAttributes;
	private String source;
	private boolean newBrokerNeeded = false;
	private String domain;
	private List<String> topics = new ArrayList<String>();
	private List<String> predicateStrings = new ArrayList<String>();;
	private String predicateType;
	private List<IPredicate<?>> predicates = new LinkedList<IPredicate<?>>();

	public SubscribeAO() {
		super();
	}

	public SubscribeAO(SubscribeAO subscribeAO) {
		super(subscribeAO);
		this.sdfAttributes = subscribeAO.sdfAttributes;
		this.source = subscribeAO.source;
		this.newBrokerNeeded = subscribeAO.newBrokerNeeded;
		this.domain = subscribeAO.domain;
		this.topics = new ArrayList<String>(subscribeAO.topics);
		this.predicateStrings = new ArrayList<String>(
				subscribeAO.predicateStrings);
		this.predicateType = subscribeAO.predicateType;
		if (subscribeAO.predicates != null) {
			for (IPredicate<?> pred : subscribeAO.predicates) {
				this.predicates.add(pred.clone());
			}
		}
	}

	@Override
	public boolean isValid() {
		// If predicates and topics are empty, subscription doesn't make sense
		if (predicateStrings.isEmpty() && topics.isEmpty()) {
			addError("Empty subscription not allowed. Please add Topics or Predicates or both.");
			return false;
		}
		// TODO: Why not using predicate parameter directly?

		if (!predicateStrings.isEmpty() && predicateType == null) {
			addError("Predicates needs a predicateType.");
			return false;
		}

		// Convert Predicate strings to predicates
		IAttributeResolver attributeResolver = new DirectAttributeResolver(
				getOutputSchemaIntern(0));
		IExpressionBuilder<?, ?> predicateBuilder = OperatorBuilderFactory
				.getExpressionBuilder(predicateType);

		if (predicateBuilder == null) {
			throw new IllegalArgumentException("unkown type of predicate: "
					+ predicateType);
		}

		for (String predicateString : predicateStrings) {
			predicates.add(predicateBuilder.createPredicate(attributeResolver,
					predicateString));
		}

		return true;
	}

	@Parameter(name = "source", type = StringParameter.class)
	public void setSource(String source) {
		this.source = source;
	}

	@Parameter(name = "schema", type = CreateSDFAttributeParameter.class, isList = true)
	public void setSchema_(List<SDFAttribute> sdfAttributes) {
		this.sdfAttributes = sdfAttributes;
	}

	@Parameter(name = "newBroker", type = BooleanParameter.class, optional = true, doc = "Specifies if a new broker should be created")
	public void setNewBrokerNeeded(boolean newBrokerNeeded) {
		this.newBrokerNeeded = newBrokerNeeded;
	}

	@Parameter(name = "domain", type = StringParameter.class, doc = "domain, on which you want to subscribe")
	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Parameter(name = "predicateType", type = StringParameter.class, optional = true, doc = "predicateType, needed if predicates are set")
	public void setPredicateType(String predicateType) {
		this.predicateType = predicateType;
	}

	@Parameter(name = "predicates", type = StringParameter.class, isList = true, optional = true, doc = "filter incomming objects by predicates")
	public void setPredicateStrings(List<String> predicates) {
		this.predicateStrings = predicates;
	}

	@Override
	public List<IPredicate<?>> getPredicates() {
		return predicates;
	}

	@Parameter(name = "topics", type = StringParameter.class, isList = true, optional = true, doc = "filter incomming objects by topics")
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (source != null) {
			return SDFSchemaFactory.createNewSchema(source,
					Tuple.class,
					sdfAttributes);
		} else if (getInputSchema() != null) {
			return SDFSchemaFactory.createNewWithAttributes(sdfAttributes,
					getInputSchema());
		} else {
			return SDFSchemaFactory.createNewSchema("",
					Tuple.class,
					sdfAttributes);
		}
	}

	@Override
	public SDFSchema getInputSchema(int pos) {
		return getOutputSchemaIntern(pos);
	}

	public String getDomain() {
		return domain;
	}

	public List<String> getTopics() {
		return topics;
	}

	public boolean isNewBrokerNeeded() {
		return newBrokerNeeded;
	}

	public String getSource() {
		return source;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SubscribeAO(this);
	}

	@Override
	public void setPredicates(List<IPredicate<?>> predicates) {
		//TODO
	}

}
