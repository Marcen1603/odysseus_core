package de.offis.scaiconnector.factory;

import java.util.List;
import java.util.Map;

import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;

/**
 * Wrapper for SCAI Update-Operations.
 *
 * @author Alexander Funk
 * 
 */
public final class UpdateScaiCmds {
	public BuilderSCAI20 ConfigurationParameterAtomic(BuilderSCAI20 builder, String oldName, String name, Boolean optional, String uom, Boolean identifier, String dataTypeName){            
		SCAIReference reference = new SCAIReference(oldName, false);
		String operationID = "createConfigurationParameterAtomic - ScaiFactory";
    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
        builder.addUpdateConfigurationParameterAtomicParameter(reference, name, optional, uom, identifier, dataType, operationID);
        return builder;
    }

    public BuilderSCAI20 ConfigurationParameterComplex(BuilderSCAI20 builder, String oldName, String name, Boolean optional, List<String> configurationParameterNames){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createConfigurationParameterComplex - ScaiFactory";
    	SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);    	
        builder.addUpdateConfigurationParameterComplexParameter(reference, name, optional, configurationParameters, operationID);
        return builder;
    }
    
    public BuilderSCAI20 ConfigurationParameterSequence(BuilderSCAI20 builder, String oldName, String name, Boolean optional, Integer min, Integer max, String configurationParameterName){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createConfigurationParameterSequence - ScaiFactory";
    	SCAIReference configurationParameter = new SCAIReference(configurationParameterName, false);        
        builder.addUpdateConfigurationParameterSequenceParameter(reference, name, optional, min, max, configurationParameter, operationID);
        return builder;
    }
    
    public BuilderSCAI20 DataElementAtomic(BuilderSCAI20 builder, String oldName, String name, String dataTypeName){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "craeteDataElementAtomic - ScaiFactory";
    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
    	builder.addUpdateDataElementAtomicElement(reference, name, dataType, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataElementComplex(BuilderSCAI20 builder, String oldName, String name, List<String> dataElementNames){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "craeteDataElementComplex - ScaiFactory";
    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
    	builder.addUpdateDataElementComplexElement(reference, name, dataElements, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataStreamType(BuilderSCAI20 builder, String oldName, String name, List<String> dataElementNames){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createDataStreamType - ScaiFactory";
    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
        builder.addUpdateDataStreamType(reference, name, dataElements, operationID);
        return builder;
    }
    
    public BuilderSCAI20 DataTypeString(BuilderSCAI20 builder, String oldName, String name, Long min, Long max, String regex, String defaultvalue){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createDataTypeString - ScaiFactory";
    	builder.addUpdateDataTypeStringType(reference, name, min, max, regex, defaultvalue, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataTypeBinary(BuilderSCAI20 builder, String oldName, String name, Long min, Long max, String defaultvalue){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createDataTypeBinary - ScaiFactory";
    	builder.addUpdateDataTypeBinaryType(reference, name, min, max, defaultvalue.getBytes(), operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataTypeDecimal(BuilderSCAI20 builder, String oldName, String name, Float min, Float max, Float scale, Float defaultvalue){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createDataTypeDecimal - ScaiFactory";
    	builder.addUpdateDataTypeDecimalType(reference, name, min, max, scale, defaultvalue, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 OperatorGroup(BuilderSCAI20 builder, String oldName, String name){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createOperatorGroup - ScaiFactory";
    	builder.addUpdateOperatorGroup(reference, name, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 OperatorType(BuilderSCAI20 builder, String oldName, String name, String metaType, Map<String, String> props, Map<String, Boolean> readOnly, String desc){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createOperatorType - ScaiFactory";
    	builder.addUpdateOperatorType(reference, name, metaType, props, readOnly, desc, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 Sensor(BuilderSCAI20 builder, String oldName, String oldDomain, String name, String sensorDomainName, String sensorTypeName, boolean virtual){
    	SCAISensorReference reference = new SCAISensorReference(oldName, oldDomain);
    	String operationID = "createSensor - ScaiFactory";
    	SCAIReference sensorDomain = new SCAIReference(sensorDomainName, false);
        SCAIReference sensorType = new SCAIReference(sensorTypeName, false);
        builder.addUpdateSensor(reference, name, sensorDomain, sensorType, virtual, operationID);         
    	return builder;
    }
    
    public BuilderSCAI20 SensorCategory(BuilderSCAI20 builder, String oldName, String name, String parentSensorReferenceName, List<String> sensorDomainNames){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createSensorCategory - ScaiFactory";
    	SCAIReference parentSensorReference = new SCAIReference(parentSensorReferenceName, false);
        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
        builder.addUpdateSensorCategory(reference, name, parentSensorReference, sensorDomains, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 SensorDomain(BuilderSCAI20 builder, String oldName, String name){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createSensorDomain - ScaiFactory";
    	builder.addUpdateSensorDomain(reference, name, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 SensorType(BuilderSCAI20 builder, String oldName, String name, String adapter, String dataStreamTypeName, List<String> configurationParameterNames, List<String> sensorCategoryNames, List<String> sensorDomainNames){
    	SCAIReference reference = new SCAIReference(oldName, false);
    	String operationID = "createSensorType - ScaiFactory";
    	SCAIReference dataStreamType = new SCAIReference(dataStreamTypeName, false);
        SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);
        SCAIReference[] sensorCategories = ScaiUtil.convertToSCAIReferences(sensorCategoryNames);
        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
        builder.addUpdateSensorType(reference, name, adapter, dataStreamType, configurationParameters, sensorCategories,sensorDomains, operationID);
    	return builder;
    }
}
