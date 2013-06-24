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

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="Subscribe", minInputPorts=0, maxInputPorts=0, doc="Subscribe Operator")
public class SubscribeAO extends UnaryLogicalOp{

	private SDFSchema schema;
	private String source;
	private String brokername;
	private String domain;
	private List<String> topics;
	
	public SubscribeAO(){
		super();
	}
	
	public SubscribeAO(SubscribeAO subscribeAO){
		super(subscribeAO);
		this.schema = subscribeAO.schema;
		this.source = subscribeAO.source;
		this.brokername = subscribeAO.brokername;
		this.domain = subscribeAO.domain;
		this.topics = new ArrayList<String>(subscribeAO.topics);
	}
	
	@Parameter(name="schema", type=CreateSDFAttributeParameter.class, isList=true, doc="")
	public void setSchema_(List<SDFAttribute> sdfAttributes){
		this.schema = new SDFSchema(source, sdfAttributes);
	}
	
	@Parameter(name="source", type=StringParameter.class)
	public void setSource(String source){
		this.source = source;
	}
	
	@Parameter(name="brokername", type=StringParameter.class)
	public void setBrokerName(String brokerName){
		this.brokername = brokerName;
	}
	
	@Parameter(name="domain", type=StringParameter.class, doc="")
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	@Override
    @Parameter(name="predicates", type=PredicateParameter.class, isList=true, optional=true, doc="")
    public void setPredicates(List<IPredicate<?>> predicates) {
        super.setPredicates(predicates);
    }
	
	@Parameter(name="topics", type=StringParameter.class, isList=true, optional=true, doc="")
	public void setTopics(List<String> topics){
		this.topics = topics;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public List<String> getTopics(){
		return topics;
	}
	
	public SDFSchema getSchema(){
		return schema;
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
