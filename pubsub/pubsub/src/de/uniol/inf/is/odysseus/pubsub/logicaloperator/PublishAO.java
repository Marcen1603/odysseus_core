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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="Publish", minInputPorts=1, maxInputPorts=1, doc="Publish Operator")
public class PublishAO extends UnaryLogicalOp{
	
	private String topologyType;
	private String domain;
	private List<String> topics = new ArrayList<>();
	
	public PublishAO(){
		super();
	}
	
	public PublishAO(PublishAO publish){
		super(publish);
		this.topologyType = publish.topologyType;
		this.domain = publish.domain;
		this.topics = new ArrayList<String>(publish.topics);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PublishAO(this);
	}
	
	@Parameter(name="topologyType", type=StringParameter.class, doc="")
	public void setTopologyType(String topologyType){
		this.topologyType = topologyType;
	}
	
	@Parameter(name="domain", type=StringParameter.class, doc="")
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	@Parameter(name="topics", type=StringParameter.class, isList=true, optional=true, doc="")
	public void setTopics(List<String> topics){
		this.topics = topics;
	}
	
	public String getTopologyType(){
		return topologyType;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public List<String> getTopics(){
		return topics;
	}

}
