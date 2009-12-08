package de.uniol.inf.is.odysseus.cep;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CepAO<T> extends AbstractLogicalOperator implements OutputSchemaSettable{

	private static final long serialVersionUID = 1L;

	public StateMachine<T> stateMachine = new StateMachine<T>();
	private SDFAttributeList outSchema;
	

	@Override
	public SDFAttributeList getOutputSchema() {
		return outSchema;
	}
	
	public StateMachine<T> getStateMachine() {
		return stateMachine;
	}
		
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outSchema = outputSchema;
	}

}
