package de.uniol.inf.is.odysseus.cep;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class CepAO extends AbstractLogicalOperator implements OutputSchemaSettable{

	private static final long serialVersionUID = 1L;

	public StateMachine stateMachine = new StateMachine();
	private SDFAttributeList outSchema;
	

	@Override
	public SDFAttributeList getOutputSchema() {
		return outSchema;
	}
	
	public StateMachine getStateMachine() {
		return stateMachine;
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outSchema = outputSchema;
	}

}
