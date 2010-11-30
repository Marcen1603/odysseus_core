package de.uniol.inf.is.odysseus.cep;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CepAO<T> extends AbstractLogicalOperator implements OutputSchemaSettable{

	private static final long serialVersionUID = 1L;

	public StateMachine<T> stateMachine = new StateMachine<T>();
	private SDFAttributeList outSchema;
	private Map<Integer, String> portNames = new HashMap<Integer, String>();
	

	public CepAO(CepAO<T> cepAO) {
		this.stateMachine = cepAO.stateMachine;
		this.outSchema = cepAO.outSchema == null? null: new SDFAttributeList(cepAO.outSchema);
		this.portNames = new HashMap<Integer, String>(cepAO.portNames);
	}
	
	public CepAO() {}

	@Override
	public SDFAttributeList getOutputSchema() {
		return outSchema;
	}
	
	public StateMachine<T> getStateMachine() {
		return stateMachine;
	}
		
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outSchema = outputSchema.clone();
	}
	
	@Override 
    public void setOutputSchema(SDFAttributeList outputSchema, int port) { 
         if(port==0){ 
              setOutputSchema(outputSchema); 
         }else{ 
              throw new IllegalArgumentException("no such port: " + port); 
         }    
    }
	
	public void setInputTypeName(int port, String name){
		portNames.put(port, name);
	}
	
	public String getInputTypeName(int port){
		return portNames.get(port);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CepAO<T>(this);
	}

}
