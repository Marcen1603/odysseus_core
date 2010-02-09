package de.uniol.inf.is.odysseus.broker.logicaloperator;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerAO extends AbstractLogicalOperator{

	private static final long serialVersionUID = 6441896379097181325L;

	private String identifier;
	private SDFAttributeList outputSchema = null;
	
	
	public BrokerAO(String identifier){
		this.identifier = identifier;
	}
	
	public BrokerAO(BrokerAO original){
		super(original);
		this.outputSchema = original.outputSchema.clone();
		this.identifier = original.identifier;
	}
	
	public ILogicalOperator getInput(int number){
		return getSubscribedToSource(number).getTarget();
	}
	
	
	public void subscribeTo(ILogicalOperator source, SDFAttributeList inputSchema){
		int nextPort = this.getNumberOfInputs();
		System.out.println("Adding operator ("+source.toString()+") to Broker "+this.identifier+" on Port "+nextPort);
		subscribeToSource(source, nextPort, 0, inputSchema);
	}
	
	
	@Override	
	public synchronized SDFAttributeList getOutputSchema() {
		return this.outputSchema;		
	}

	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
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
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result
				+ ((outputSchema == null) ? 0 : outputSchema.hashCode());
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
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (outputSchema == null) {
			if (other.outputSchema != null)
				return false;
		} else if (!outputSchema.equals(other.outputSchema))
			return false;
		return true;
	}

}
