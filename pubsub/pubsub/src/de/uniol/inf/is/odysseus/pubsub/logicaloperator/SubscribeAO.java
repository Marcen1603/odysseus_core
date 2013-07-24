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
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IPredicateBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;

/**
 * Logical Subscribe Operator. The Operator provides the subscribe functionality in publish/Subscribe systems.
 * 
 * @author ChrisToenjesDeye
 *
 */
@LogicalOperator(name="Subscribe", minInputPorts=0, maxInputPorts=0, doc="This Operator provides the subscribe functionality in publish/Subscribe systems.")
public class SubscribeAO extends UnaryLogicalOp{

	private List<SDFAttribute> sdfAttributes;
	private String source;
	private String brokername;
	private String domain;
	private List<String> topics;
	private List<String> predicateStrings;
	private String predicateType;
	
	public SubscribeAO(){
		super();
	}
	
	public SubscribeAO(SubscribeAO subscribeAO){
		super(subscribeAO);
		this.sdfAttributes = subscribeAO.sdfAttributes;
		this.source = subscribeAO.source;
		this.brokername = subscribeAO.brokername;
		this.domain = subscribeAO.domain;
		this.topics = new ArrayList<String>(subscribeAO.topics);
		this.predicateStrings = new ArrayList<String>(subscribeAO.predicateStrings);
		this.predicateType = subscribeAO.predicateType;
	}
	
	@Override
	public boolean isValid() {
		// If predicates and topics are empty, subscription doesn't make sense
		if (predicateStrings.isEmpty() && topics.isEmpty()){
			addError(new IllegalParameterException(
					"Empty subscription not allowed. Please add Topics or Predicates or both."));
			return false;
		}
		
		if (!predicateStrings.isEmpty() && predicateType == null){
			addError(new IllegalParameterException(
					"Predicates needs a predicateType."));
			return false;
		}
		
		// Convert Predicate strings to predicates
		IAttributeResolver attributeResolver = new DirectAttributeResolver(
				getOutputSchemaIntern(0));
		IPredicateBuilder predicateBuilder = OperatorBuilderFactory.getPredicateBuilder(predicateType);

		if (predicateBuilder == null) {
			throw new IllegalArgumentException("unkown type of predicate: "
					+ predicateType);
		}
		
		for (String predicateString : predicateStrings) {
			super.getPredicates().add(predicateBuilder.createPredicate(attributeResolver, predicateString));
		}
		
		return true;
	}

	@Parameter(name="source", type=StringParameter.class)
	public void setSource(String source){
		this.source = source;
	}
	
	@Parameter(name="schema", type=CreateSDFAttributeParameter.class, isList=true, doc="")
	public void setSchema_(List<SDFAttribute> sdfAttributes){
		this.sdfAttributes = sdfAttributes;
	}
	
	
	@Parameter(name="brokername", type=StringParameter.class)
	public void setBrokerName(String brokerName){
		this.brokername = brokerName;
	}
	
	@Parameter(name="domain", type=StringParameter.class, doc="domain, on which you want to subscribe")
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	@Parameter(name="predicateType", type=StringParameter.class, optional=true, doc="predicateType, needed if predicates are set")
	public void setPredicateType(String predicateType){
		this.predicateType = predicateType;
	}
	
    @Parameter(name="predicates", type=StringParameter.class, isList=true, optional=true, doc="filter incomming objects by predicates")
    public void setPredicateStrings(List<String> predicates) {
        this.predicateStrings = predicates;
    }
	
	@Parameter(name="topics", type=StringParameter.class, isList=true, optional=true, doc="filter incomming objects by topics")
	public void setTopics(List<String> topics){
		this.topics = topics;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (source != null){
			return new SDFSchema(source, sdfAttributes);
		}else if (getInputSchema() != null) {
			return new SDFSchema(getInputSchema().getURI(), sdfAttributes);
		} else {
			return new SDFSchema("", sdfAttributes);
		}
	}
	
	@Override
	public SDFSchema getInputSchema(int pos)  {
		return getOutputSchemaIntern(pos);
	}
	
	public String getDomain(){
		return domain;
	}
	
	public List<String> getTopics(){
		return topics;
	}
	
	public String getBrokerName(){
		return brokername;
	}
	
	public String getSource(){
		return source;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SubscribeAO(this);
	}

}
