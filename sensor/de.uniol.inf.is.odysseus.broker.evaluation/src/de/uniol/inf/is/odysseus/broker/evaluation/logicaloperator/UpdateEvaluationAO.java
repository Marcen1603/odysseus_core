package de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UpdateEvaluationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5288410428323884614L;
	private SDFAttributeList schema;

	private int number = 0;	

	public UpdateEvaluationAO() {

	}

	public UpdateEvaluationAO(int number){
		this.number = number;
	}
	
	public UpdateEvaluationAO(UpdateEvaluationAO original) {
		this.number = original.number;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.schema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new UpdateEvaluationAO(this);
	}

	public void setSchema(SDFAttributeList outputSchema) {
		this.schema = outputSchema;
	}
	
	@Override
	public boolean isAllPhysicalInputSet() {
		if(super.isAllPhysicalInputSet()){
			return true;
		}else{
			
		}
		for (Integer i : this.subscribedToSource.keySet()) {
			if(this.subscribedToSource.get(i).getTarget() instanceof BrokerAO){
				continue;
			}
			if (super.getPhysInputOperators().get(i) == null) {
				return false;
			}
		}
		return true;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + number;
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		UpdateEvaluationAO other = (UpdateEvaluationAO) obj;
//		if (number != other.number)
//			return false;
//		return true;
//	}	
	
	
	
}
