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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.pubsub.broker.routing.RoutingBrokerRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;

/**
 * Logical Publish Operator. The Operator provides the publish functionality in publish/Subscribe systems.
 * 
 * @author ChrisToenjesDeye
 *
 */
@LogicalOperator(name="Publish", minInputPorts=1, maxInputPorts=1, doc="This Operator provides the publish functionality in publish/Subscribe systems.", category={LogicalOperatorCategory.PUPSUB})
public class PublishAO extends UnaryLogicalOp{

	private static final long serialVersionUID = 6718305011922367185L;
	private String topologyType;
	private String domain;
	private List<String> topics = new ArrayList<>();
	private String routing;
	
	public PublishAO(){
		super();
	}
	
	public PublishAO(PublishAO publish){
		super(publish);
		this.topologyType = publish.topologyType;
		this.domain = publish.domain;
		this.routing = publish.routing;
		this.topics = new ArrayList<String>(publish.topics);
	}
	
	@Override
	public boolean isValid() {
		// Check if Topology type is valid
		List<String> validTypes = BrokerTopologyRegistry.getValidTopologyTypes();
		if (!validTypes.contains(topologyType.toLowerCase())){
			addError(
					"Topology Type: '"+ topologyType +"' is not valid. Available Types are: "+validTypes.toString());
			return false;
		} else {
			// Check if topology needs routing
			if (BrokerTopologyRegistry.needsTopologyRouting(topologyType)){
				if (routing == null){
					addError("Topology: '"+topologyType+"' needs a routing rule.");
					return false;					
				} else {
					// Check if routing type is valid (only if needed)
					List<String> routingTypes = RoutingBrokerRegistry.getValidRoutingTypes();
					if (!routingTypes.contains(routing.toLowerCase())){
						addError(
								"Routing Type: '"+ routing +"' is not valid. Available Types are: "+routingTypes.toString());
						return false;
					}
				}
			}
		}
		// Check if combination of type and domain is valid
		if (!BrokerTopologyRegistry.isDomainTypeCombinationValid(topologyType, domain)){
			addError(
					"Domain: '"+ domain +"' already exists with a different topology type. It's not possible to register a Topology with the same domain and a different topologyType.");
			return false;
		}
		
		
		
		return true;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PublishAO(this);
	}
	
	@Parameter(name="topologyType", type=StringParameter.class, doc="the used topology type")
	public void setTopologyType(String topologyType){
		this.topologyType = topologyType;
	}
	
	@Parameter(name="routing", type=StringParameter.class, doc="if routing topology is selected, a routing algorithm must be added", optional=true)
	public void setRouting(String routing){
		this.routing = routing;
	}
	
	@Parameter(name="domain", type=StringParameter.class, doc="domain, where published objects will be processed")
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	@Parameter(name="topics", type=StringParameter.class, isList=true, optional=true, doc="advertise, which topics the processed objects match")
	public void setTopics(List<String> topics){
		this.topics = topics;
	}
	
	public String getTopologyType(){
		return topologyType;
	}
	
	public String getRouting(){
		return routing;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public List<String> getTopics(){
		return topics;
	}

}
