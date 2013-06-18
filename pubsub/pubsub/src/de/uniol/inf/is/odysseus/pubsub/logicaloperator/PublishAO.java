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
	private List<String> topics;
	
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
