package de.uniol.inf.is.odysseus.broker.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * The BrokerAO is an abstract operator for the logical algebra.
 * 
 * @author Dennis Geesen
 */
public class BrokerAO extends AbstractLogicalOperator{
	
	/** The serialVersionUID. */
	private static final long serialVersionUID = 6441896379097181325L;	
	
	/** The unique identifier. */
	private String identifier;
	
	/** The data schema. */
	private SDFAttributeList schema = null;
	
	/** The generated time will be used to distinguish between two BrokerAOs. */
	private long generatedTime;
	
	/** The queue schema. */
	private SDFAttributeList queueSchema = new SDFAttributeList();	
		
	/**
	 * Instantiates a new logical broker.
	 *
	 * @param identifier the identifier
	 */
	protected BrokerAO(String identifier){
		this.identifier = identifier;
		this.generatedTime = System.currentTimeMillis();
	}
	
	

	/**
	 * Creates a new broker from another one.
	 *
	 * @param original the original where to copy from
	 */
	protected BrokerAO(BrokerAO original){
		super(original);
		this.schema = original.schema.clone();
		this.identifier = original.identifier;
		this.generatedTime = original.generatedTime;
		this.queueSchema = original.queueSchema.clone();			
	}
	
	

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.ILogicalOperator#getOutputSchema()
	 */
	@Override	
	public synchronized SDFAttributeList getOutputSchema() {		
		return this.schema;		
	}		

	/**
	 * Sets the data schema.
	 *
	 * @param outputSchema the new schema
	 */
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
	
	/**
	 * Gets the queue schema.
	 *
	 * @return the queue schema
	 */
	public SDFAttributeList getQueueSchema() {
		return this.queueSchema;
	}
	
	/**
	 * Sets the queue schema.
	 *
	 * @param schema the new queue schema
	 */
	public void setQueueSchema(SDFAttributeList schema){
		this.queueSchema = schema;
	}
	
	/**
	 * Gets the identifier.
	 *
	 * @return the identifier
	 */
	public String getIdentifier(){
		return this.identifier;		
	}		
	
	/**
	 * Sets the identifier.
	 *
	 * @param identifier the new identifier
	 */
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}
			
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return super.toString()+" ("+this.identifier+")";
	}	
	
	/**
	 * Sets the generated time.
	 *
	 * @param newTime the new generated time
	 */
	public void setGeneratedTime(long newTime){
		this.generatedTime = newTime;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator#clone()
	 */
	@Override 
	public BrokerAO clone() throws CloneNotSupportedException {	
		BrokerAO clone = new BrokerAO(this);		
		return clone;
	}	
}
