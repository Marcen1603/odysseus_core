package de.uniol.inf.is.odysseus.broker.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerAO extends AbstractLogicalOperator{
	
	private static final long serialVersionUID = 6441896379097181325L;	
	private String identifier;
	private SDFAttributeList outputSchema = null;
	private long generatedTime;	
	
	
	
	public BrokerAO(String identifier){
		this.identifier = identifier;
		this.generatedTime = System.currentTimeMillis();
	}
	
	public BrokerAO(BrokerAO original){
		super(original);
		this.outputSchema = original.outputSchema.clone();
		this.identifier = original.identifier;
		this.generatedTime = original.generatedTime;
	}
	
	@Override	
	public synchronized SDFAttributeList getOutputSchema() {		
		return this.outputSchema;		
	}	

	public void setOutputSchema(SDFAttributeList outputSchema) {
		//this.outputSchema = outputSchema.clone();
		//create alias schema
		SDFAttributeList aliasSchema = new SDFAttributeList();
		for(SDFAttribute attribute : outputSchema){
			SDFAttribute newAttribute = attribute.clone();
			newAttribute.setSourceName(null);
			aliasSchema.add(newAttribute);
		}
		this.outputSchema = aliasSchema;
		
	}
	
	public String getIdentifier(){
		return this.identifier;		
	}
	
	public List<ILogicalOperator> getAllChildren(){
		List<ILogicalOperator> childs = new ArrayList<ILogicalOperator>();
		for(LogicalSubscription sub : super.getSubscriptions()){
			System.err.println("AO-Sub: "+sub.getTarget()+" ("+this+")");
			childs.add(sub.getTarget());
		}		
		return childs;
	}		
	
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (generatedTime ^ (generatedTime >>> 32));
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BrokerAO other = (BrokerAO) obj;
		if (generatedTime != other.generatedTime)
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return super.toString()+" ("+this.identifier+")";
	}	
	
	public void setGeneratedTime(long newTime){
		this.generatedTime = newTime;
	}
	
	@Override 
	public BrokerAO clone() throws CloneNotSupportedException {	
		BrokerAO clone = new BrokerAO(this);		
		return clone;
	}
	
	@Override
	public boolean isAllPhysicalInputSet() {
		if(this.subscribedToSource.isEmpty()){
			return true;
		}else{
			return super.isAllPhysicalInputSet();
		}
	}
}
