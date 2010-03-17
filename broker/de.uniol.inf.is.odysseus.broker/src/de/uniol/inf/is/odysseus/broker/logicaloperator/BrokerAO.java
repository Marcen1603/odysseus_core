package de.uniol.inf.is.odysseus.broker.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerAO extends AbstractLogicalOperator{
	
	private static final long serialVersionUID = 6441896379097181325L;	
	private String identifier;
	private SDFAttributeList schema = null;
	private long generatedTime;
	private SDFAttributeList queueSchema = new SDFAttributeList();	
		
	protected BrokerAO(String identifier){
		this.identifier = identifier;
		this.generatedTime = System.currentTimeMillis();
	}
	
	

	protected BrokerAO(BrokerAO original){
		super(original);
		this.schema = original.schema.clone();
		this.identifier = original.identifier;
		this.generatedTime = original.generatedTime;
		this.queueSchema = original.queueSchema.clone();			
	}
	
	

	@Override	
	public synchronized SDFAttributeList getOutputSchema() {		
		return this.schema;		
	}		

	public void setSchema(SDFAttributeList outputSchema) {		
		//create alias schema
		SDFAttributeList aliasSchema = new SDFAttributeList();
		for(SDFAttribute attribute : outputSchema){
			SDFAttribute newAttribute = attribute.clone();
			newAttribute.setSourceName(null);
			aliasSchema.add(newAttribute);
		}
		this.schema = aliasSchema;		
	}
	
	public SDFAttributeList getQueueSchema() {
		return this.queueSchema;
	}
	
	public void setQueueSchema(SDFAttributeList schema){
		this.queueSchema = schema;
	}
	
	public String getIdentifier(){
		return this.identifier;		
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
