package de.uniol.inf.is.odysseus.cep;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CepAO<T> extends AbstractLogicalOperator implements OutputSchemaSettable{

	private static final long serialVersionUID = 1L;

	public StateMachine<T> stateMachine = new StateMachine<T>();
	private SDFAttributeList outSchema;
	private Map<Integer, String> portNames = new HashMap<Integer, String>();
	

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
	
	public void setInputTypeName(int port, String name){
		portNames.put(port, name);
	}
	
	public String getInputTypeName(int port){
		return portNames.get(port);
	}


}
