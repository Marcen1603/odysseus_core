package de.offis.scaiconnector.factory;

import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;

/**
 * Wrapper for SCAI Remove-Operations.
 *
 * @author Alexander Funk
 * 
 */
public final class RemoveScaiCmds {
	
	public RemoveScaiCmds(ScaiFactory factory) {
		
	}

	public BuilderSCAI20 ConfigurationParameter(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
        builder.addRemoveConfigurationParameter(reference, operationId);
        return builder;
    }
    
    public BuilderSCAI20 DataElement(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
    	builder.addRemoveDataElement(reference, operationId);
    	return builder;
    }
    
    public BuilderSCAI20 DataStreamType(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
        builder.addRemoveDataStreamType(reference, operationId);
        return builder;
    }
    
    public BuilderSCAI20 DataType(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
    	builder.addRemoveDataType(reference, operationId);
    	return builder;
    }
    
    public BuilderSCAI20 OperatorGroup(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
    	builder.addRemoveOperatorGroup(reference, operationId);
    	return builder;
    }
            
    public BuilderSCAI20 OperatorType(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
    	builder.addRemoveOperatorType(reference, operationId);
    	return builder;
    }
    
    public BuilderSCAI20 Sensor(BuilderSCAI20 builder, String name, String domain, String operationId){
    	SCAISensorReference reference = new SCAISensorReference(name, domain);
        builder.addRemoveSensor(reference, operationId);         
    	return builder;
    }
    
    public BuilderSCAI20 SensorCategory(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
        builder.addRemoveSensorCategory(reference, operationId);
    	return builder;
    }
    
    public BuilderSCAI20 SensorDomain(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
    	builder.addRemoveSensorDomain(reference, operationId);
    	return builder;
    }
    
    public BuilderSCAI20 SensorType(BuilderSCAI20 builder, String name, String operationId){
    	SCAIReference reference = new SCAIReference(name, false);
        builder.addRemoveSensorType(reference, operationId);
    	return builder;
    }
}
