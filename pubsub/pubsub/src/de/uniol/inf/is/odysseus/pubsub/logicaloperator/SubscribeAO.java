package de.uniol.inf.is.odysseus.pubsub.logicaloperator;


import java.util.List;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="Subscribe", minInputPorts=1, maxInputPorts=1, doc="Subscribe Operator")
public class SubscribeAO extends UnaryLogicalOp{

	private SDFSchema schema;
	private String brokername;
	private String topologyType;
	
	public SubscribeAO(){
		super();
	}
	
	public SubscribeAO(SubscribeAO subscribeAO){
		super(subscribeAO);
		this.schema = subscribeAO.schema;
		this.brokername = subscribeAO.brokername;
		this.topologyType = subscribeAO.topologyType;
	}
	
	@Override
    @Parameter(name="predicates", type=PredicateParameter.class, isList=true)
    public void setPredicates(List<IPredicate<?>> predicates) {
        super.setPredicates(predicates);
    }
	
	@Parameter(name="schema", type=ResolvedSDFAttributeParameter.class, isList=true)
	public void setSchema(List<SDFAttribute> sdfAttributes){
		this.schema = new SDFSchema("bla", sdfAttributes);
	}
	
	@Parameter(name="brokername", type=StringParameter.class)
	public void setBrokerName(String brokerName){
		this.brokername = brokerName;
	}
	
	@Parameter(name="topologyType", type=StringParameter.class)
	public void setTopologyType(String topologyType){
		this.topologyType = topologyType;
	}
	
	public SDFSchema getSchema(){
		return schema;
	}
	
	public String getBrokerName(){
		return brokername;
	}
	
	public String getTopologyType(){
		return topologyType;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SubscribeAO(this);
	}

}
